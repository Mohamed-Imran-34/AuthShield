package com.security.AuthShield.mailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class mailServices {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void verifyOtpMail(String toEmail,String username,String otp) throws MessagingException {


//        SimpleMailMessage mailMessage= new SimpleMailMessage();
//        mailMessage.setFrom("AuthShield<a1b2c3d4w7x8y9z0@gmail.com>");
//        mailMessage.setTo(toEmail);
//        mailMessage.setSubject("Otp for email verification");
//        mailMessage.setText("Welcome to AuthShield \n \n  Hi "+username +",  \n \n Otp for your email verification is "+ otp+ ".Kindly use this Otp to verify your email and this Otp is only valid for next 10 minutes. \n \n  If you didn't request for email verification please ignore this email. \n \n With Regards, \n AuthShield.");
//        mailSender.send(mailMessage);

        Context context = new Context();
        context.setVariable("email",toEmail);
        context.setVariable("username",username);
        context.setVariable("otp",otp);

        String msg = templateEngine.process("verify-otp",context);
        MimeMessage message= mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("AuthShield<a1b2c3d4w7x8y9z0@gmail.com>");
        helper.setTo(toEmail);
        helper.setSubject("Otp for email verification");
        helper.setText(msg,true);
        mailSender.send(message);






    }

    public void resetOtpEmail(String toEmail,String username,String otp) throws MessagingException {
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        mailMessage.setFrom("AuthShield<a1b2c3d4w7x8y9z0@gmail.com>");
//        mailMessage.setTo(toEmail);
//        mailMessage.setSubject("Otp for Resetting Password");
//        mailMessage.setText("AuthShield \n \n"+"Hi "+username+",\n \n Otp for resetting the password for your account "+toEmail+" is "+otp+".Kindly use this Otp to reset your account password and this Otp is only valid for next 10 minutes. \n \n If you didn't request for password resetting Otp please ignore this email.\n \n With Regards, \n AuthShield.");
//        mailSender.send(mailMessage);

        Context context = new Context();
        context.setVariable("email",toEmail);
        context.setVariable("username",username);
        context.setVariable("otp",otp);

        String msg = templateEngine.process("reset-otp",context);
        MimeMessage message= mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("AuthShield<a1b2c3d4w7x8y9z0@gmail.com>");
        helper.setTo(toEmail);
        helper.setSubject("Otp for Password Reset");
        helper.setText(msg,true);
        mailSender.send(message);
    }

}
