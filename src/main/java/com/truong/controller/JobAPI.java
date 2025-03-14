package com.truong.controller;
import com.truong.dto.JobDTO;
import com.truong.payload.JobFilter;
import com.truong.service.JobService;
import com.truong.service.import_export.JobExport;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/job")
public class JobAPI {

  private static final Logger logger = Logger.getLogger(JobAPI.class.getName());

  @Autowired
  private JobService jobService;

  @Autowired
  private JobExport jobExport;

  // API xuất báo cáo Excel và tải về trực tiếp
  @PostMapping("/export-excel")
  public ResponseEntity<?> exportJobToExcel(@RequestBody JobFilter filter, @RequestParam Long userId) {
    try {
      List<JobDTO> jobs = jobService.search(userId, filter);
      if (jobs.isEmpty()) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Không có công việc để xuất");
      }

      String filePath = jobExport.exportJobsToExcel(jobs);

      Path path = Paths.get(filePath);
      byte[] excelData = Files.readAllBytes(path);

      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Job_Report.xlsx")
          .contentType(MediaType.APPLICATION_OCTET_STREAM)
          .body(excelData);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  // API tìm kiếm công việc
  @PostMapping("/searchJob")
  public ResponseEntity<?> search(@RequestBody JobFilter filter, @RequestParam Long userId) {
    try {
      return ResponseEntity.ok(jobService.search(userId, filter));
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(e.getMessage());
    }
  }
}
