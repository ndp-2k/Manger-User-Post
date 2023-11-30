package com.manager.social_network.report.controller;

import com.manager.social_network.common.function.Common;
import com.manager.social_network.report.Service.ReportService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/report")
public class ReportController {
    Common common;

    @Autowired
    private ReportService reportService;

    @GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> downloadExcelFile(HttpServletRequest request) throws IOException {
        // Thực hiện tạo và lưu tệp Excel
        reportService.createReportExcelFile(common.getUserIdByToken(request), ReportService.EXCEL_FILE_PATH);

        // Tạo Resource từ tệp Excel đã tạo
        Resource resource = new FileSystemResource(ReportService.EXCEL_FILE_PATH);

        // Trả về phản hồi để tải xuống tệp Excel
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.getFile().length())
                .body(resource);
    }
}
