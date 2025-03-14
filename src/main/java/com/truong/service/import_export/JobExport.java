package com.truong.service.import_export;

import com.truong.service.JobService;
import java.awt.print.Pageable;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobExport {
  final JobService jobService;

//  public ResponseEntity<?> exportData(Long userId, MultipartFile file, String jsonData, Pageable pageable) throws Exception {
//    if (ObjectUtils.isEmpty(jsonData) && (ObjectUtils.isEmpty(file) || file.isEmpty())) {
//      throw new Exception("export.validate.required");
//    }
//  }
  }
