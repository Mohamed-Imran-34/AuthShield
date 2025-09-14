package com.security.AuthShield.mailService;

import brevo.ApiClient;
import brevo.ApiException;
import brevo.Configuration;
import brevoApi.TransactionalEmailsApi;
import brevoModel.SendSmtpEmail;
import brevoModel.SendSmtpEmailSender;
import brevoModel.SendSmtpEmailTo;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Collections;

@Component
public class mailServices {


    @Autowired
    private TemplateEngine templateEngine;

    @Value("${apiKey}")
    private String apiKey;




    public void verifyOtpMail(String toEmail,String username,String otp) throws MessagingException {
        ApiClient apiClient = Configuration.getDefaultApiClient();
        apiClient.setApiKey(apiKey);


        Context context = new Context();
        context.setVariable("email",toEmail);
        context.setVariable("username",username);
        context.setVariable("otp",otp);

        String msg = templateEngine.process("verify-otp",context);

        TransactionalEmailsApi transactionalEmailsApi =  new TransactionalEmailsApi(apiClient);
        SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();
        sendSmtpEmail.setSender(new SendSmtpEmailSender().email("a1b2c3d4w7x8y9z0@gmail.com").name("AuthShield"));
        sendSmtpEmail.setTo(Collections.singletonList(new SendSmtpEmailTo().email(toEmail).name(username)));
        sendSmtpEmail.setSubject("Email verification OTP");
        sendSmtpEmail.textContent(msg);

        try{
            transactionalEmailsApi.sendTransacEmail(sendSmtpEmail);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }


    }

    public void resetOtpEmail(String toEmail,String username,String otp) throws MessagingException {


        ApiClient apiClient = Configuration.getDefaultApiClient();
        apiClient.setApiKey(apiKey);


        Context context = new Context();
        context.setVariable("email",toEmail);
        context.setVariable("username",username);
        context.setVariable("otp",otp);

        String msg = templateEngine.process("reset-otp",context);

        TransactionalEmailsApi transactionalEmailsApi =  new TransactionalEmailsApi(apiClient);
        SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();
        sendSmtpEmail.setSender(new SendSmtpEmailSender().email("a1b2c3d4w7x8y9z0@gmail.com").name("AuthShield"));
        sendSmtpEmail.setTo(Collections.singletonList(new SendSmtpEmailTo().email(toEmail).name(username)));
        sendSmtpEmail.setSubject("OTP for password reset");
        sendSmtpEmail.setHtmlContent(msg);

        try{
            transactionalEmailsApi.sendTransacEmail(sendSmtpEmail);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }

}
