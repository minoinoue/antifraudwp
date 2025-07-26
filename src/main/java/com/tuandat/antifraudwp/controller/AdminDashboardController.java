package com.tuandat.antifraudwp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "admin/dashboard";
    }
} 