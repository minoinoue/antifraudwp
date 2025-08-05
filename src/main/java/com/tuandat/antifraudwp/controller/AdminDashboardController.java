package com.tuandat.antifraudwp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.tuandat.antifraudwp.repository.NewsRepository;
import com.tuandat.antifraudwp.repository.VideoRepository;
import com.tuandat.antifraudwp.repository.ProductRepository;
import com.tuandat.antifraudwp.repository.ReportRepository;
import com.tuandat.antifraudwp.repository.MyAppUserRepository;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {
    
    @Autowired private NewsRepository newsRepository;
    @Autowired private VideoRepository videoRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private ReportRepository reportRepository;
    @Autowired private MyAppUserRepository userRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("newsCount", newsRepository.count());
        model.addAttribute("videoCount", videoRepository.count());
        model.addAttribute("productCount", productRepository.count());
        model.addAttribute("reportCount", reportRepository.count());
        model.addAttribute("userCount", userRepository.count());
        return "admin/dashboard";
    }
} 