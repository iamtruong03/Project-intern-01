package com.truong.dto;

import com.truong.entities.Job;
import com.truong.entities.User;
import java.time.LocalDate;
import java.util.Objects;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobDTO {
  Long jobId;
  String jobName;
  String jobStatusName;
  String approverName;
  List<String> executedName;
  LocalDate deadline;

  public JobDTO(Job job) {
    this.jobId = job.getJobId();
    this.jobName = job.getJobName();
    this.jobStatusName = (job.getJobStatus() != null) ? job.getJobStatus().getJobStatusName() : null;
    this.deadline = job.getDeadline();
    this.approverName = (job.getApproverId() != null) ? job.getApproverId().getFullName() : null;
    this.executedName = job.getExecutedUsers().stream()
        .map(User::getFullName)
        .collect(Collectors.toList());
  }

}

