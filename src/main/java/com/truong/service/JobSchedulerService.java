package com.truong.service;

import com.truong.repo.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobSchedulerService {

  @Autowired
  private JobRepository jobRepository;

//  @Scheduled(cron = "0 0 0 * * ?") // Chạy lúc 00:00 mỗi ngày
//  public void checkOverdueJobs() {
//    List<Job> jobs = jobReponsitory.findAll();
//    for (Job job : jobs) {
//      if (job.getStatus() == JobStatus.IN_PROGRESS && LocalDate.now().isAfter(job.getDeadline())) {
//        job.setStatus(JobStatus.OVERDUE);
//        jobReponsitory.save(job);
//      }
//    }
//  }
}
