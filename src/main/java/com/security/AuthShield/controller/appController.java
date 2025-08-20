package com.security.AuthShield.controller;


import com.security.AuthShield.model.*;
import com.security.AuthShield.service.appService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authShield/v1/")
@CrossOrigin(origins = "http://localhost:5173" , allowCredentials = "true", allowedHeaders = "*" , methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PUT,RequestMethod.OPTIONS,RequestMethod.HEAD})
public class appController {

    @Autowired
    private appService service;


    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody userRequest request){
        return  service.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody userLogin userLogin){
        return service.login(userLogin);

    }

    @PostMapping("/logout")
    public ResponseEntity<?> test(){
        return service.logout();
    }

    @PostMapping("/verifyEmailOtp")
    public ResponseEntity<?> sendVerifyOtp(){
        return service.sendVerifyOtp(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @PostMapping("/verifyEmail")
    public ResponseEntity<?> verifyEmail(@Valid @RequestBody verifyOtp verifyotp){
        return service.verifyEmail(verifyotp.getOtp());
    }

    @PostMapping("/resetOtp")
    public ResponseEntity<?> sendResetOtp(@Valid @RequestBody resetOtpEmail resetOtpEmail){
        return service.sendResetOtp(resetOtpEmail);
    }

    @PostMapping("/verifyResetOtp")
    public ResponseEntity<?> verifyResetOtp(@Valid @RequestBody verifyResetEmailOtp verifyResetEmailOtp){
        return service.verifyResetOtp(verifyResetEmailOtp);
    }
    @PostMapping("/reset/accountPassword")
    public ResponseEntity<?> resetAccountPassword(@Valid @RequestBody resetPassword resetPassword){
        return service.resetAccountPassword(resetPassword);
    }

    @PostMapping("/profileDetails")
    public ResponseEntity<?> profileDetails(){
        return service.profileDetails();
    }

}
