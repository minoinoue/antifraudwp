package com.tuandat.antifraudwp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContentController {
    
    @GetMapping("/req/login")
    public String login(){
        return "login";
    }
    
    @GetMapping("/req/signup")
    public String signup(){
        return "signup";
    }
    @GetMapping("/index")
    public String home(){
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
}
