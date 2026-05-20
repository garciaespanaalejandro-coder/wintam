package com.wintam.service;

import com.wintam.dto.MessageResponse;
import com.wintam.dto.ReportProfileResponse;
import com.wintam.dto.ReportRequest;
import com.wintam.dto.ResolveReportRequest;
import com.wintam.exception.ReportNotFoundException;
import com.wintam.exception.UserNotFoundException;
import com.wintam.model.Report;
import com.wintam.model.ReportStatus;
import com.wintam.model.User;
import com.wintam.repository.ReportRepository;
import com.wintam.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportServiceSpirng implements ReportService{
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final ApplySanctionService applySanctionService;
    public ReportServiceSpirng(ReportRepository reportRepository, UserRepository userRepository, ApplySanctionService applySanctionService) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.applySanctionService = applySanctionService;
    }

    @Override
    @Transactional
    public MessageResponse reportUser(ReportRequest request) {
        User reported =  userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UserNotFoundException());
        User reporter= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Report report= Report.builder()
                        .reported(reported)
                        .reporter(reporter)
                        .reason(request.getReason())
                        .build();
        reportRepository.save(report);
        return new  MessageResponse("Usuario reportado correctamente.");
    }

    @Override
    @Transactional
    public List<ReportProfileResponse> getReport() {
        List<ReportProfileResponse> responseList= new ArrayList<>();
        List<Report> reportList= this.reportRepository.findAll();

        for (Report report : reportList) {
            ReportProfileResponse response= new ReportProfileResponse(
                    report.getId(),
                    report.getReporter().getUsername(),
                    report.getReported().getUsername(),
                    report.getReason(),
                    report.getCreatedAt()
            );
            responseList.add(response);
        }
        return responseList;
    }

    @Override
    @Transactional
    public MessageResponse resolveReport(ResolveReportRequest request) {
        Report report= reportRepository.findById(request.getReportId())
                .orElseThrow(ReportNotFoundException::new);
        report.setStatus(ReportStatus.RESOLVED);
        report.setSanctionType(request.getSanctionType());
        reportRepository.save(report);
        applySanctionService.applySanction(request.getSanctionType(),report.getReported());
        return new MessageResponse("Reporte resuelto correctamente.");
    }

    @Override
    @Transactional
    public MessageResponse dismissReport(Long id) {
        Report report= reportRepository.findById(id)
                .orElseThrow(ReportNotFoundException::new);
        report.setStatus(ReportStatus.DISMISSED);
        reportRepository.save(report);
        return new MessageResponse("Reporte cancelado correctamente.");
    }
}
