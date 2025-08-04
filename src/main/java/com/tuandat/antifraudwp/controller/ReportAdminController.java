package com.tuandat.antifraudwp.controller;

import com.tuandat.antifraudwp.model.Report;
import com.tuandat.antifraudwp.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin/reports")
public class ReportAdminController {

    @Autowired
    private ReportService reportService;

    @GetMapping
    public String listReports(Model model) {
        model.addAttribute("reports", reportService.getAllReports());
        return "admin/report_list";
    }

    @GetMapping("/view/{id}")
    public String viewReport(@PathVariable Long id, Model model) {
        Report report = reportService.getReport(id);
        model.addAttribute("report", report);
        return "admin/report_view";
    }

    @PostMapping("/update-status/{id}")
    public String updateStatus(@PathVariable Long id, @RequestParam String status, @RequestParam(required = false) String adminNote) {
        Report report = reportService.getReport(id);
        report.setStatus(status);
        if (adminNote != null && !adminNote.trim().isEmpty()) {
            report.setAdminNote(adminNote);
        }
        reportService.saveReport(report);
        return "redirect:/admin/reports";
    }

    @GetMapping("/delete/{id}")
    public String deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return "redirect:/admin/reports";
    }
} 