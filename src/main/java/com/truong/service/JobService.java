package com.truong.service;

import com.truong.dto.JobDTO;
import com.truong.dto.UserDTO;
import com.truong.entities.Department;
import com.truong.entities.Job;
import com.truong.entities.JobStatus;
import com.truong.entities.User;
import com.truong.payload.JobFilter;
import com.truong.repo.JobRepository;
import com.truong.repo.JobStatusRepository;
import com.truong.repo.UserRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private JobRepository jobRepository;
	@Autowired
	private JobStatusRepository jobStatusRepository;
	@Autowired
	private DepartmentService departmentService;
	private final Random random = new Random();

	// tạo job cho cấp dưới
	public Job createJob(String jobName, LocalDate deadline, Long createdUserId, List<Long> executedUserIds) {
		if (executedUserIds == null || executedUserIds.isEmpty()) {
			throw new RuntimeException("Phải có ít nhất một người thực hiện!");
		}

		User createdUser = userRepository.findById(createdUserId)
				.orElseThrow(() -> new RuntimeException("User tạo không tồn tại!"));
		UserDTO createdUserDTO = UserDTO.fromEntity(createdUser);

		Long departmentId = createdUser.getDepartment().getDepartmentId();
		List<UserDTO> allowedExecutors = departmentService.getUsersByDepartment(departmentId);

		Set<User> executedUsers = new HashSet<>();
		for (Long executedUserId : executedUserIds) {
			if (createdUserId.equals(executedUserId)) {
				throw new RuntimeException("Người tạo không được trùng với người thực hiện!");
			}
			User executedUser = userRepository.findById(executedUserId)
					.orElseThrow(() -> new RuntimeException("User thực hiện không tồn tại!"));
			UserDTO executedUserDTO = UserDTO.fromEntity(executedUser);

			if (allowedExecutors.stream().noneMatch(user -> user.getId().equals(executedUserDTO.getId()))) {
				throw new RuntimeException("Người thực hiện không thuộc danh sách phòng ban con của người tạo!");
			}

			executedUsers.add(executedUser);
		}

		JobStatus defaultStatus = jobStatusRepository.findByJobStatusName("IN_PROGRESS")
				.orElseThrow(() -> new RuntimeException("Trạng thái mặc định không tồn tại!"));

		Job job = new Job();
		job.setJobName(jobName);
		job.setDeadline(deadline);
		job.setJobStatus(defaultStatus);
		job.setApproverId(createdUser);
		job.setExecutedUsers(executedUsers);

		return jobRepository.save(job);
	}

	// tao yeu cau duyet job
	@Transactional
	public Job createApproveJob(Long id, String jobName, LocalDate deadline) {
		User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Người dùng không tồn tại!"));

		Department userDepartment = user.getDepartment();
		if (userDepartment == null || userDepartment.getParentDepartment() == null) {
			throw new RuntimeException("Không tìm thấy phòng ban cấp trên!");
		}

		Department parentDepartment = userDepartment.getParentDepartment();

		List<User> approvers = userRepository.findByDepartment(parentDepartment);
		if (approvers.isEmpty()) {
			throw new RuntimeException("Không có người duyệt trong phòng ban cấp trên!");
		}

		User approver = approvers.get(random.nextInt(approvers.size()));

		JobStatus defaultStatus = jobStatusRepository.findById(2L)
				.orElseThrow(() -> new RuntimeException("Trạng thái mặc định không tồn tại!"));

		Job job = new Job();
		job.setJobName(jobName);
		job.setDeadline(deadline);
		job.setJobStatus(defaultStatus);
		job.setApproverId(approver);
		job.getExecutedUsers().add(user);

		return jobRepository.save(job);
	}

	public List<Map<String, Object>> getJobsByExecutedId(Long id) {
		User executedUser = userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Người thực hiện không tồn tại!"));

		List<Job> jobs = jobRepository.findByExecutedUsersContaining(executedUser);

		return jobs.stream().map(job -> {
			Map<String, Object> jobInfo = new HashMap<>();
			jobInfo.put("id", job.getJobId());
			jobInfo.put("name", job.getJobName());
			jobInfo.put("jobStatusName", job.getJobStatus() != null ? job.getJobStatus().getJobStatusName() : null);
			jobInfo.put("approverUsername", job.getApproverId() != null ? job.getApproverId().getFullName() : null);
			jobInfo.put("deadline", job.getDeadline() != null ? job.getDeadline().toString() : null);
			return jobInfo;
		}).collect(Collectors.toList());
	}

	// danh sách job của cấp dưới user
	public List<Job> getJobsOfSubordinates(Long userId) {
		// Tìm người duyệt theo userId
		User approver = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("Người duyệt không tồn tại!"));

		// Lấy danh sách nhân viên cấp dưới dưới dạng DTO
		List<UserDTO> subordinateDTOs = departmentService
				.getUsersByDepartment(approver.getDepartment().getDepartmentId());

		// Nếu không có nhân viên cấp dưới, trả về danh sách rỗng
		if (subordinateDTOs.isEmpty()) {
			return List.of();
		}

		// Chuyển đổi danh sách UserDTO thành danh sách User
		List<Long> subordinateIds = subordinateDTOs.stream().map(UserDTO::getId).toList();
		List<User> subordinates = userRepository.findAllById(subordinateIds);

		return jobRepository.findJobsOfSubordinates(approver, subordinates);
	}

	@Transactional
	public Job updateJobStatus(Long jobId, Long userId, Long newStatusId) {
		// Lấy công việc từ DB
		Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Công việc không tồn tại!"));

		JobStatus currentStatus = job.getJobStatus();
		JobStatus newStatus = jobStatusRepository.findById(newStatusId)
				.orElseThrow(() -> new RuntimeException("Trạng thái mới không tồn tại!"));

		// Nếu job đã bị từ chối trước đó, không thể chỉnh sửa
		if (currentStatus.getJobStatusId() == 3) {
			throw new RuntimeException("Job đã bị từ chối, không thể chỉnh sửa!");
		}

		// Nếu job đã quá hạn deadline, tự động cập nhật sang trạng thái "Quá hạn" (ID =
		// 5)
		if (LocalDate.now().isAfter(job.getDeadline())) {
			JobStatus overdueStatus = jobStatusRepository.findById(5L)
					.orElseThrow(() -> new RuntimeException("Trạng thái Quá hạn không tồn tại!"));
			job.setJobStatus(overdueStatus);
			return jobRepository.save(job);
		}

		boolean isExecutor = job.getExecutedUsers().stream().anyMatch(user -> user.getId().equals(userId));
		boolean isApprover = job.getApproverId().getId().equals(userId);

		if (isExecutor && currentStatus.getJobStatusId() == 1 && newStatusId == 4) {
			job.setJobStatus(newStatus);
			return jobRepository.save(job);
		}

		if (isApprover && currentStatus.getJobStatusId() == 2 && newStatusId == 1) {
			job.setJobStatus(newStatus);
			return jobRepository.save(job);
		}

		if (isApprover && currentStatus.getJobStatusId() == 2 && newStatusId == 3) {
			job.setJobStatus(newStatus);
			return jobRepository.save(job);
		}

		throw new RuntimeException("Không có quyền thay đổi trạng thái này!");
	}

	// thống kê cá nhân
	public Map<String, Long> countJobsByExecutedUser(Long userId) {
	    User user = userRepository.findById(userId)
	            .orElseThrow(() -> new RuntimeException("Không tồn tại user"));

	    List<Object[]> rs = jobRepository.countJobByExecutedUserStatus(user);

	    return rs.stream().collect(Collectors.toMap(
	            row -> ((JobStatus) row[0]).getJobStatusName(), 
	            row -> (Long) row[1]                           
	    ));
	}
	
	// thống kê phòng ban con
	public Map<String, Long> countJobsByStatusForSubordinates(Long approverId) {
		User approver = userRepository.findById(approverId)
				.orElseThrow(() -> new RuntimeException("Người duyệt không tồn tại!"));

		if (approver.getDepartment() == null) {
			throw new RuntimeException("Người duyệt không thuộc phòng ban nào!");
		}

		// Lấy danh sách userId của cấp dưới trong cùng phòng ban (trừ approverId)
		List<Long> subordinateIds = departmentService.getUsersByDepartment(approver.getDepartment().getDepartmentId())
				.stream().map(UserDTO::getId).filter(id -> !id.equals(approverId)).toList();

		if (subordinateIds.isEmpty()) {
			return Collections.emptyMap();
		}

		// Gọi query để đếm số lượng công việc theo trạng thái của cấp dưới
		List<Object[]> result = jobRepository.countJobsByStatusForSubordinates(subordinateIds);

		return result.stream().collect(Collectors.toMap(row -> ((JobStatus) row[0]).getJobStatusName(),

				row -> (Long) row[1]));
	}

	public JobDTO convertToDTO(Job job) {
		JobDTO dto = new JobDTO();
		dto.setJobId(job.getJobId());
		dto.setJobName(job.getJobName());
		dto.setJobStatusName(job.getJobStatus() != null ? job.getJobStatus().getJobStatusName() : null);
		dto.setApproverName(job.getApproverId() != null ? job.getApproverId().getFullName() : null);
		List<String> executedNames = job.getExecutedUsers().stream()
				.map(User::getFullName)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
		dto.setExecutedName(executedNames);
		dto.setDeadline(job.getDeadline());
		return dto;
	}

	// tim kiem job
	public List<JobDTO> search(Long userId, JobFilter filter) {
		List<Job> jobs = getJobsOfSubordinates(userId);
		return jobs.stream()
				.filter(job -> job.getJobName().toLowerCase().contains(filter.getJobName().toLowerCase()))
				.map(this::convertToDTO)
				.collect(Collectors.toList());
	}

}
