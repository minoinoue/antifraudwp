package com.tuandat.antifraudwp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import com.tuandat.antifraudwp.repository.NewsRepository;
import java.util.Optional;

@Controller
public class ContentController {
    
    @Autowired
    private NewsRepository newsRepository;

    @GetMapping("/req/login")
    public String login(){
        return "login";
    }
    
    @GetMapping("/req/signup")
    public String signup(){
        return "signup";
    }
    @GetMapping("/index")
    public String home(Model model){
        model.addAttribute("newsList", newsRepository.findAll());
        return "index";
    }
    
    @GetMapping("/req/forgot-password-page")
    public String forgotPasswordPage() {
        return "forgot_password";
    }
    @GetMapping("/req/reset-password-page")
    public String resetPasswordPage() {
        return "reset_password";
    }

    @GetMapping("/login")
    public String loginAlias() {
        return "login";
    }

    @GetMapping("/news/{id}")
    public String newsDetail(@PathVariable Long id, Model model) {
        Optional<com.tuandat.antifraudwp.model.News> newsOpt = newsRepository.findById(id);
        if (newsOpt.isPresent()) {
            model.addAttribute("news", newsOpt.get());
            return "news_detail";
        }
        return "redirect:/index";
    }
}
