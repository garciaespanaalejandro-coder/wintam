package com.wintam.controller;

import com.wintam.dto.MessageResponse;
import com.wintam.dto.ReportProfileResponse;
import com.wintam.dto.ReportRequest;
import com.wintam.dto.ResolveReportRequest;
import com.wintam.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/report")
public class ReportRestController {
    private final ReportService reportService;

    public ReportRestController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/reportUser")
    public ResponseEntity<MessageResponse> reportUser(@Valid @RequestBody ReportRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(reportService.reportUser(request));
    }

    @GetMapping("/getReport")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReportProfileResponse>> getReport(){
        return ResponseEntity.ok(reportService.getReport());
    }

    @PatchMapping("/resolveReport")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> resolveReport(@Valid @RequestBody ResolveReportRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(reportService.resolveReport(request));
    }

    @PatchMapping("/dismissReport/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> dismissReport(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(reportService.dismissReport(id));
    }
}
