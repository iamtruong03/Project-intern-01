package com.truong.service.import_export;

import com.truong.dto.JobDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;
import java.io.*;
import java.nio.file.*;

@Service
public class JobExport {

  private static final String EXPORT_DIR = "exported_files/";

  public String exportJobsToExcel(List<JobDTO> jobs) throws IOException {
    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("Job Report");

    // Tạo tiêu đề
    Row headerRow = sheet.createRow(0);
    String[] columns = {"ID", "Tên Công Việc", "Trạng Thái", "Người Phê Duyệt", "Người Thực Hiện", "Hạn Chót"};

    CellStyle headerStyle = getHeaderCellStyle(workbook);
    for (int i = 0; i < columns.length; i++) {
      Cell cell = headerRow.createCell(i);
      cell.setCellValue(columns[i]);
      cell.setCellStyle(headerStyle);
    }

    int rowNum = 1;
    for (JobDTO job : jobs) {
      Row row = sheet.createRow(rowNum++);
      row.createCell(0).setCellValue(job.getJobId());
      row.createCell(1).setCellValue(job.getJobName());
      row.createCell(2).setCellValue(job.getJobStatusName() != null ? job.getJobStatusName() : "");
      row.createCell(3).setCellValue(job.getApproverName() != null ? job.getApproverName() : "");

      String executedNames = (job.getExecutedName() != null) ? String.join(", ", job.getExecutedName()) : "";
      row.createCell(4).setCellValue(executedNames);
      row.createCell(5).setCellValue(job.getDeadline() != null ? job.getDeadline().toString() : "");
    }
    for (int i = 0; i < columns.length; i++) {
      sheet.autoSizeColumn(i);
    }
    Path exportPath = Paths.get(EXPORT_DIR);
    if (!Files.exists(exportPath)) {
      Files.createDirectories(exportPath);
    }
    String fileName = "Job_Report_" + System.currentTimeMillis() + ".xlsx";
    String filePath = EXPORT_DIR + fileName;
    try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
      workbook.write(fileOut);
    }
    workbook.close();

    return filePath; // Trả về đường dẫn file
  }

  private CellStyle getHeaderCellStyle(Workbook workbook) {
    CellStyle style = workbook.createCellStyle();
    Font font = workbook.createFont();
    font.setBold(true);
    style.setFont(font);
    style.setAlignment(HorizontalAlignment.CENTER);
    style.setBorderBottom(BorderStyle.THIN);
    style.setBorderTop(BorderStyle.THIN);
    style.setBorderLeft(BorderStyle.THIN);
    style.setBorderRight(BorderStyle.THIN);
    return style;
  }
}

