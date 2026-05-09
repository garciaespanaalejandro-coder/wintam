package com.wintam.service;

import com.wintam.dto.MessageResponse;
import com.wintam.dto.ReportProfileResponse;
import com.wintam.dto.ReportRequest;
import com.wintam.dto.ResolveReportRequest;

import java.util.List;

public interface ReportService {
    MessageResponse reportUser(ReportRequest request);
    List<ReportProfileResponse> getReport();
    MessageResponse resolveReport(ResolveReportRequest request);
    MessageResponse dismissReport(Long id);
}
