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
import com.tuandat.antifraudwp.model.MyAppUserRepository;
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
}