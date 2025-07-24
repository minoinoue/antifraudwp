package com.tuandat.antifraudwp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tuandat.antifraudwp.model.MyAppUser;
import com.tuandat.antifraudwp.repository.MyAppUserRepository;
import com.tuandat.antifraudwp.utils.JwtTokenUtil;

import java.util.Optional;

@RestController
public class VerificationController {
    
    @Autowired
    private MyAppUserRepository myAppUserRepository;
    
    @Autowired
    private JwtTokenUtil jwtUtil;
     
    @GetMapping("/req/signup/verify")
    public ResponseEntity verifyEmail(@RequestParam("token") String token) {
        String emailString = jwtUtil.extractEmail(token);
        Optional<MyAppUser> userOpt = myAppUserRepository.findByEmail(emailString);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token đã hết hạn!");
        }
        MyAppUser user = userOpt.get();
        if (user.getVerificationToken() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token đã hết hạn!");
        }
        if (!jwtUtil.validateToken(token) || !user.getVerificationToken().equals(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token đã hết hạn!");
        }
        user.setVerificationToken(null);
        user.setVerified(true);  
        myAppUserRepository.save(user);
        String html = """
        <html><head><meta charset='UTF-8'><title>Xác thực email</title>
        <script>setTimeout(function(){ window.location.href='/req/login'; }, 2000);</script></head>
        <body style='text-align:center;padding:40px;font-family:sans-serif;'>
        <h2 style='color:green;'>Xác thực email thành công!</h2>
        <p>Bạn sẽ được chuyển về trang đăng nhập sau vài giây...</p>
        </body></html>
        """;
        return ResponseEntity.ok().header("Content-Type", "text/html; charset=UTF-8").body(html);
    }
    
    
}