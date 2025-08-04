package com.tuandat.antifraudwp.service;

import com.tuandat.antifraudwp.model.Report;
import com.tuandat.antifraudwp.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public Report getReport(Long id) {
        Optional<Report> report = reportRepository.findById(id);
        if (report.isPresent()) {
            return report.get();
        }
        throw new RuntimeException("Report not found with id: " + id);
    }

    public Report saveReport(Report report) {
        return reportRepository.save(report);
    }

    public void deleteReport(Long id) {
        reportRepository.deleteById(id);
    }
} 