package com.tuandat.antifraudwp.controller;

import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tuandat.antifraudwp.model.MyAppUser;
import com.tuandat.antifraudwp.repository.MyAppUserRepository;
import com.tuandat.antifraudwp.service.EmailService;
import com.tuandat.antifraudwp.utils.JwtTokenUtil;
import com.tuandat.antifraudwp.controller.RegistrationController;

@RestController
public class RegistrationController {
    
	@Autowired
	private MyAppUserRepository myAppUserRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private EmailService emailService;
	
	@PostMapping (value = "/req/signup", consumes = "application/json")
	public ResponseEntity<String> createUser(@RequestBody MyAppUser user) {
		
		MyAppUser existingAppUser = myAppUserRepository.findByEmail(user.getEmail());
		
		if(existingAppUser != null) {
			if(existingAppUser.isVerified()) {
				return new ResponseEntity<>("User Already exsit and verified.", HttpStatus.BAD_REQUEST);
			} else {
				String verificationToken = JwtTokenUtil.generateToken(user.getEmail());
				existingAppUser.setVerificationToken(verificationToken);
				myAppUserRepository.save(existingAppUser);
				//Send email code
				emailService.sendVerificationEmail(user.getEmail(), verificationToken);
				return new ResponseEntity<>("Verification Email resent. Check your inbox", HttpStatus.OK);
			}
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		String verificationToken = JwtTokenUtil.generateToken(user.getEmail());
		user.setVerificationToken(verificationToken);
		myAppUserRepository.save(user);
		//Send email code
		emailService.sendVerificationEmail(user.getEmail(), verificationToken);
		
		return new ResponseEntity<>("Registration successfull! Please verify your email", HttpStatus.OK);
	}

	@PostMapping("/req/forgot-password")
	public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
		String email = request.get("email");
		MyAppUser user = myAppUserRepository.findByEmail(email);
		if (user == null) {
			return new ResponseEntity<>("Email không tồn tại!", HttpStatus.BAD_REQUEST);
		}
		String resetToken = JwtTokenUtil.generateToken(email);
		user.setResetToken(resetToken);
		myAppUserRepository.save(user);
		emailService.sendForgotPasswordEmail(email, resetToken);
		return new ResponseEntity<>("Đã gửi email đặt lại mật khẩu!", HttpStatus.OK);
	}

	@PostMapping("/req/reset-password")
	public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
		String token = request.get("token");
		String newPassword = request.get("newPassword");
		if (token == null || newPassword == null) {
			return new ResponseEntity<>("Thiếu thông tin!", HttpStatus.BAD_REQUEST);
		}
		String email;
		try {
			email = JwtTokenUtil.getInstance().extractEmail(token);
		} catch (Exception e) {
			return new ResponseEntity<>("Token không hợp lệ!", HttpStatus.BAD_REQUEST);
		}
		MyAppUser user = myAppUserRepository.findByEmail(email);
		if (user == null || user.getResetToken() == null || !user.getResetToken().equals(token)) {
			return new ResponseEntity<>("Token không hợp lệ hoặc đã hết hạn!", HttpStatus.BAD_REQUEST);
		}
		if (!JwtTokenUtil.getInstance().validateToken(token)) {
			return new ResponseEntity<>("Token đã hết hạn!", HttpStatus.BAD_REQUEST);
		}
		user.setPassword(passwordEncoder.encode(newPassword));
		user.setResetToken(null);
		myAppUserRepository.save(user);
		return new ResponseEntity<>("Đổi mật khẩu thành công!", HttpStatus.OK);
	}

	@PostMapping("/req/verify-reset-token")
	public ResponseEntity<String> verifyResetToken(@RequestBody Map<String, String> request) {
		String token = request.get("token");
		if (token == null) {
			return new ResponseEntity<>("Thiếu token!", HttpStatus.BAD_REQUEST);
		}
		String email;
		try {
			email = JwtTokenUtil.getInstance().extractEmail(token);
		} catch (Exception e) {
			return new ResponseEntity<>("Token không hợp lệ!", HttpStatus.BAD_REQUEST);
		}
		MyAppUser user = myAppUserRepository.findByEmail(email);
		if (user == null || user.getResetToken() == null || !user.getResetToken().equals(token)) {
			return new ResponseEntity<>("Token không hợp lệ hoặc đã hết hạn!", HttpStatus.BAD_REQUEST);
		}
		if (!JwtTokenUtil.getInstance().validateToken(token)) {
			return new ResponseEntity<>("Token đã hết hạn!", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("OK", HttpStatus.OK);
	}
}