package com.truong.controller;

import com.truong.payload.JobFilter;
import com.truong.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/job")
public class JobAPI {

  @Autowired
  private JobService jobService;

  // search job
  @PostMapping("/searchJob")
  public ResponseEntity<?> search(@RequestBody JobFilter filter, @RequestParam Long userId) {
    try {
      return ResponseEntity.ok(jobService.search(userId, filter));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }
}
