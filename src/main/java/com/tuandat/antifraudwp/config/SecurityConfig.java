package com.tuandat.antifraudwp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.tuandat.antifraudwp.service.MyAppUserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final MyAppUserService appUserService;

    @Autowired
    public SecurityConfig(MyAppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return appUserService;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(appUserService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, org.springframework.security.core.AuthenticationException exception) throws IOException, jakarta.servlet.ServletException {
                System.out.println("Lỗi xác thực: " + exception.getMessage());
                request.getSession().setAttribute("SPRING_SECURITY_LAST_EXCEPTION", exception.getMessage());
                response.sendRedirect("/req/login?error");
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(httpForm -> {
                    httpForm.loginPage("/req/login").permitAll();
                    httpForm.defaultSuccessUrl("/index");
                    httpForm.failureHandler(customAuthenticationFailureHandler());
                })
                .authorizeHttpRequests(registry -> {
                    registry.requestMatchers("/req/**", "/css/**", "/js/**").permitAll();
                    registry.anyRequest().authenticated();
                })
                .build();
    }
}