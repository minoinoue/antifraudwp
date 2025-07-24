package com.tuandat.antifraudwp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendVerificationEmail(String email, String verificationToken) {
        String subject = "Xác thực Email";
        String path = "/req/signup/verify";
        String message = "Nhấn nút dưới đây để xác thực email của bạn:";
        try {
            String actionUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(path)
                    .queryParam("token", verificationToken)
                    .toUriString();

            String content = """
                <div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 32px 24px; border-radius: 12px; background: linear-gradient(135deg, #f9f9f9 60%%, #e3f0ff 100%%); box-shadow: 0 4px 24px rgba(0,0,0,0.07); text-align: center;\">
                    <h2 style=\"color: #1a237e; margin-bottom: 12px;\">%s</h2>
                    <p style=\"font-size: 18px; color: #333; margin-bottom: 18px;\">%s</p>
                    <a href=\"%s\" style=\"display: inline-block; margin: 18px 0 12px 0; padding: 12px 32px; font-size: 18px; color: #fff; background: linear-gradient(90deg, #1976d2, #64b5f6); text-decoration: none; border-radius: 8px; font-weight: bold; box-shadow: 0 2px 8px rgba(25,118,210,0.12); transition: background 0.2s;\">Xác thực email</a>
                    <p style=\"font-size: 15px; color: #555; margin: 18px 0 0 0;\">Hoặc copy và dán liên kết này vào trình duyệt:</p>
                    <p style=\"font-size: 15px; color: #1976d2; word-break: break-all; margin: 8px 0 0 0;\">%s</p>
                    <p style=\"font-size: 12px; color: #aaa; margin-top: 32px;\">Đây là email tự động. Vui lòng không trả lời.</p>
                </div>
            """.formatted(subject, message, actionUrl, actionUrl);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setFrom(from);
            helper.setText(content, true);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            System.err.println("Failed to send verification email: " + e.getMessage());
        }
    }


    public void sendForgotPasswordEmail(String email, String otp) {
        String subject = "Yêu cầu đặt lại mật khẩu";
        String message = "Mã OTP đặt lại mật khẩu của bạn là: <b>" + otp + "</b>";
        String content = """
            <div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 32px 24px; border-radius: 12px; background: linear-gradient(135deg, #f9f9f9 60%%, #e3f0ff 100%%); box-shadow: 0 4px 24px rgba(0,0,0,0.07); text-align: center;\">
                <h2 style=\"color: #1a237e; margin-bottom: 12px;\">%s</h2>
                <p style=\"font-size: 18px; color: #333; margin-bottom: 18px;\">%s</p>
                <p style=\"font-size: 15px; color: #555; margin: 18px 0 0 0;\">Nếu bạn không yêu cầu đặt lại mật khẩu, hãy bỏ qua email này.</p>
                <p style=\"font-size: 12px; color: #aaa; margin-top: 32px;\">Đây là email tự động. Vui lòng không trả lời.</p>
            </div>
        """.formatted(subject, message);
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setFrom(from);
            helper.setText(content, true);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            System.err.println("Failed to send forgot password email: " + e.getMessage());
        }
    }


    private void sendEmail(String email, String subject, String message, String token, String path) {
        try {
            String actionUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(path)
                    .queryParam("token", token)
                    .toUriString();

            String content = """
                    <div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 32px 24px; border-radius: 12px; background: linear-gradient(135deg, #f9f9f9 60%%, #e3f0ff 100%%); box-shadow: 0 4px 24px rgba(0,0,0,0.07); text-align: center;\">
                        <h2 style=\"color: #1a237e; margin-bottom: 12px;\">%s</h2>
                        <p style=\"font-size: 18px; color: #333; margin-bottom: 18px;\">%s</p>
                        <div style=\"margin: 18px 0 24px 0;\">
                            <span style=\"display: inline-block; font-size: 20px; font-weight: bold; color: #fff; background: linear-gradient(90deg, #1976d2, #64b5f6); padding: 10px 32px; border-radius: 8px; letter-spacing: 1px; box-shadow: 0 2px 8px rgba(25,118,210,0.08); user-select: all;\">Mã OTP: %s</span>
                        </div>
                        <a href=\"%s\" style=\"display: inline-block; margin: 18px 0 12px 0; padding: 12px 32px; font-size: 18px; color: #fff; background: linear-gradient(90deg, #1976d2, #64b5f6); text-decoration: none; border-radius: 8px; font-weight: bold; box-shadow: 0 2px 8px rgba(25,118,210,0.12); transition: background 0.2s;\">Đặt lại mật khẩu</a>
                        <p style=\"font-size: 15px; color: #555; margin: 18px 0 0 0;\">Hoặc copy và dán liên kết này vào trình duyệt:</p>
                        <p style=\"font-size: 15px; color: #1976d2; word-break: break-all; margin: 8px 0 0 0;\">%s</p>
                        <p style=\"font-size: 12px; color: #aaa; margin-top: 32px;\">Đây là email tự động. Vui lòng không trả lời.</p>
                    </div>
                """.formatted(subject, message, token, actionUrl, actionUrl);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setFrom(from);
            helper.setText(content, true);
            mailSender.send(mimeMessage);

        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}
