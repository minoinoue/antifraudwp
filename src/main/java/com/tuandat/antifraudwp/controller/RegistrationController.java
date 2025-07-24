package com.tuandat.antifraudwp.controller;

import java.util.Map;
import java.util.Optional;
import java.security.SecureRandom;


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
		
		Optional<MyAppUser> existingAppUserByEmail = myAppUserRepository.findByEmail(user.getEmail());
		Optional<MyAppUser> existingAppUserByUsername = myAppUserRepository.findByUsername(user.getUsername());
		if(existingAppUserByUsername.isPresent()) {
			return new ResponseEntity<>("Username đã tồn tại.", HttpStatus.BAD_REQUEST);
		}
		if(existingAppUserByEmail.isPresent()) {
			MyAppUser existingAppUser = existingAppUserByEmail.get();
			if(existingAppUser.isVerified()) {
				return new ResponseEntity<>("Email đã tồn tại.", HttpStatus.BAD_REQUEST);
			} else {
				// Nếu email đã tồn tại nhưng chưa xác thực, chỉ gửi lại email xác thực, không tạo user mới
				String verificationToken = JwtTokenUtil.generateToken(user.getEmail());
				existingAppUser.setVerificationToken(verificationToken);
				myAppUserRepository.save(existingAppUser);
				emailService.sendVerificationEmail(user.getEmail(), verificationToken);
				return new ResponseEntity<>("Đã gửi lại email xác thực. Kiểm tra hộp thư.", HttpStatus.OK);
			}
		}
		// Nếu username và email đều chưa tồn tại, mới tạo user mới
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		String verificationToken = JwtTokenUtil.generateToken(user.getEmail());
		user.setVerificationToken(verificationToken);
		myAppUserRepository.save(user);
		emailService.sendVerificationEmail(user.getEmail(), verificationToken);
		return new ResponseEntity<>("Đăng ký thành công! Vui lòng xác thực email.", HttpStatus.OK);
	}

	@PostMapping("/req/forgot-password")
	public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
		String email = request.get("email");
		Optional<MyAppUser> userOpt = myAppUserRepository.findByEmail(email);
		if (userOpt.isEmpty()) {
			return new ResponseEntity<>("Email không tồn tại!", HttpStatus.BAD_REQUEST);
		}
		MyAppUser user = userOpt.get();
		// Sinh mã OTP 6 số
		String otp = String.format("%06d", new SecureRandom().nextInt(1000000));
		user.setOtp(otp);
		myAppUserRepository.save(user);
		emailService.sendForgotPasswordEmail(email, otp);
		return new ResponseEntity<>("Đã gửi mã OTP đặt lại mật khẩu!", HttpStatus.OK);
	}

	@PostMapping("/req/reset-password")
	public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
		String otp = request.get("token");
		String newPassword = request.get("newPassword");
		if (otp == null || newPassword == null) {
			return new ResponseEntity<>("Thiếu thông tin!", HttpStatus.BAD_REQUEST);
		}
		Optional<MyAppUser> userOpt = myAppUserRepository.findAll().stream().filter(u -> otp.equals(u.getOtp())).findFirst();
		if (userOpt.isEmpty()) {
			return new ResponseEntity<>("Mã OTP không hợp lệ hoặc đã hết hạn!", HttpStatus.BAD_REQUEST);
		}
		MyAppUser user = userOpt.get();
		user.setPassword(passwordEncoder.encode(newPassword));
		user.setOtp(null);
		myAppUserRepository.save(user);
		return new ResponseEntity<>("Đổi mật khẩu thành công!", HttpStatus.OK);
	}

	@PostMapping("/req/verify-reset-token")
	public ResponseEntity<String> verifyResetToken(@RequestBody Map<String, String> request) {
		String otp = request.get("token");
		if (otp == null) {
			return new ResponseEntity<>("Thiếu mã OTP!", HttpStatus.BAD_REQUEST);
		}
		Optional<MyAppUser> userOpt = myAppUserRepository.findAll().stream().filter(u -> otp.equals(u.getOtp())).findFirst();
		if (userOpt.isEmpty()) {
			return new ResponseEntity<>("Mã OTP không hợp lệ hoặc đã hết hạn!", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("OK", HttpStatus.OK);
	}
}