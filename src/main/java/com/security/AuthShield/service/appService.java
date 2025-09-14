package com.security.AuthShield.service;


import com.security.AuthShield.jwtservice.jwtUtils;
import com.security.AuthShield.mailService.mailServices;
import com.security.AuthShield.model.*;
import com.security.AuthShield.repository.userLogRepository;
import com.security.AuthShield.repository.userTableRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Date;

@Service
public class appService {
    @Autowired
    private userTableRepository userTableRepository;

    @Autowired
    private mailServices mailServices;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private jwtUtils jwtUtils ;

    @Autowired
    private userLogRepository userLogRepository;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);


    public ResponseEntity<?> register(userRequest request) {

        users user=  users.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .isAccountVerified(false)
                .verifyOtp(null)
                .resetOtp(null)
                .verifyExpiration(null)
                .resetExpiration(null)
                .role("user")
                .build();

        usersLog userslog = usersLog.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .info("Created")
                .build();
        if(! userTableRepository.existsById(request.getEmail())){
            userTableRepository.save(user);
            userLogRepository.save(userslog);
            return new ResponseEntity<>("Successfully created", HttpStatus.CREATED);
        }
        else{
            return new ResponseEntity<>("Email already registered", HttpStatus.CONFLICT);
        }

    }

    public ResponseEntity<?> login(@Valid userLogin userLogin) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userLogin.getEmail(),userLogin.getPassword());
        try{
            authenticationProvider.authenticate(authToken);
            ResponseCookie cookie = ResponseCookie.from("JWTKEY",jwtUtils.generateToken(userLogin.getEmail())).path("/").sameSite("Strict").httpOnly(true).secure(false).maxAge(10*24*60*60).build();
            HttpHeaders headers= new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE,cookie.toString());
            users user=userTableRepository.findById(userLogin.getEmail()).orElse(null);
            return  new ResponseEntity<>( new userResponse(user.getUsername(),user.getEmail(),user.getIsAccountVerified(),"admin".equals(user.getRole())),headers,HttpStatus.OK);
        }
        catch (BadCredentialsException e){
            return  new ResponseEntity<>("Invalid Credentials",HttpStatus.UNAUTHORIZED);
        }
        catch (Exception e){
            return  new ResponseEntity<>("User does not exist",HttpStatus.UNAUTHORIZED);
        }

    }

    public ResponseEntity<?> logout() {

        ResponseCookie cookie = ResponseCookie.from("JWTKEY","")
                .secure(false).httpOnly(true).sameSite("Strict").maxAge(0).path("/").build();
        HttpHeaders headers=new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE,cookie.toString());
        return new ResponseEntity<>("Successfully logged out",headers,HttpStatus.OK);
    }

    public ResponseEntity<?> sendVerifyOtp(String email) {
        users user =userTableRepository.findById(email).orElse(null);
        if(user == null){
            return new ResponseEntity<>("User doesn't Exist",HttpStatus.NOT_FOUND);
        }
        if(user.getIsAccountVerified()){
            return new ResponseEntity<>("User already Verified",HttpStatus.NOT_ACCEPTABLE);
        }
        SecureRandom secureRandom = new SecureRandom();
        int generated=100000+secureRandom.nextInt(900000);
        String otp=String.valueOf(generated);
        try{
            mailServices.verifyOtpMail(email,user.getUsername(),otp);
            user.setVerifyOtp(otp);
            user.setVerifyExpiration(new Date(System.currentTimeMillis()+10*60*1000));
            userTableRepository.save(user);
            return new ResponseEntity<>("OTP sent successfully",HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Mail can't be sent Try Again !!!"+e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<?> verifyEmail(@NotBlank(message = "OTP must not be empty") @Size(min = 6,max = 6,message = "OTP in must be the length of 6") String otp) {

        String email= SecurityContextHolder.getContext().getAuthentication().getName();
        users user=userTableRepository.findById(email).orElse(null);
        if(user == null){
            return new ResponseEntity<>("User doesn't exist",HttpStatus.NOT_FOUND);
        }
        if(user.getIsAccountVerified()){
            return new ResponseEntity<>("User already Verified",HttpStatus.NOT_ACCEPTABLE);
        }

        if(! otp.equals(user.getVerifyOtp()) ){
            return new ResponseEntity<>("Invalid OTP",HttpStatus.NOT_FOUND);
        }
        if(user.getVerifyExpiration().before(new Date(System.currentTimeMillis()))){
            return new ResponseEntity<>("OTP Expired",HttpStatus.NOT_ACCEPTABLE);
        }
        else{
            user.setVerifyExpiration(null);
            user.setVerifyOtp(null);
            user.setIsAccountVerified(true);
            userTableRepository.save(user);
            return new ResponseEntity<>("Successfully verified",HttpStatus.OK);
        }
    }

    public ResponseEntity<?> sendResetOtp(@Valid resetOtpEmail resetOtpEmail) {
        users user =userTableRepository.findById(resetOtpEmail.getEmail()).orElse(null);
        if(user == null){
            return new ResponseEntity<>("User doesn't exist",HttpStatus.NOT_FOUND);
        }

        SecureRandom secureRandom= new SecureRandom();
        int otp=100000+secureRandom.nextInt(900000);
        String generated=String.valueOf(otp);
        try{
            mailServices.resetOtpEmail(resetOtpEmail.getEmail(),user.getUsername(),generated);
            user.setResetOtp(generated);
            user.setResetExpiration(new Date(System.currentTimeMillis()+10*60*1000));
            userTableRepository.save(user);
            return new ResponseEntity<>("Otp sent successfully",HttpStatus.OK);
        } catch (Exception e) {
            return  new ResponseEntity<>("Email doesn't sent ! tryAgain"+e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<?> verifyResetOtp(@Valid verifyResetEmailOtp verifyResetEmailOtp) {
        users user =userTableRepository.findById(verifyResetEmailOtp.getEmail()).orElse(null);
        if(user== null){
            return new ResponseEntity<>("User doesn't exist",HttpStatus.NOT_FOUND);
        }
        if(user.getResetOtp() == null){
            return new ResponseEntity<>("Invalid OTP request",HttpStatus.NOT_ACCEPTABLE);
        }
        if(! user.getResetOtp().equals(verifyResetEmailOtp.getOtp())){
            return new ResponseEntity<>("Invalid OTP ",HttpStatus.NOT_ACCEPTABLE);
        }
        if(user.getResetExpiration().before(new Date(System.currentTimeMillis()))){
            return new ResponseEntity<>("OTP Expired",HttpStatus.NOT_ACCEPTABLE);
        }
        else {
            return new ResponseEntity<>("Valid OTP",HttpStatus.OK);
        }
    }

    public ResponseEntity<?> resetAccountPassword(@Valid resetPassword resetPassword) {
        users user =userTableRepository.findById(resetPassword.getEmail()).orElse(null);
        if(user== null){
            return new ResponseEntity<>("User doesn't exist",HttpStatus.NOT_FOUND);
        }
        if(user.getResetOtp() == null){
            return new ResponseEntity<>("Invalid OTP request",HttpStatus.NOT_ACCEPTABLE);
        }
        if(! user.getResetOtp().equals(resetPassword.getOtp())){
            return new ResponseEntity<>("Invalid OTP ",HttpStatus.NOT_ACCEPTABLE);
        }
        if(user.getResetExpiration().before(new Date(System.currentTimeMillis()))){
            return new ResponseEntity<>("OTP Expired",HttpStatus.NOT_ACCEPTABLE);
        }
        else {
            user.setPassword(passwordEncoder.encode(resetPassword.getPassword()));
            user.setResetExpiration(null);
            user.setResetOtp(null);
            userTableRepository.save(user);
            return new ResponseEntity<>("Password changed successfully",HttpStatus.OK);
        }
    }

    public ResponseEntity<?> profileDetails() {
        String name=SecurityContextHolder.getContext().getAuthentication().getName();
        users user = userTableRepository.findById(name).orElse(null);
        if(user != null) {
            userResponse userResponse = new userResponse(user.getUsername(),user.getEmail(),user.getIsAccountVerified(),"admin".equals(user.getRole()));
            return  new ResponseEntity<>(userResponse,HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid request",HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<?> deleteAccount(String email) {

        users  user = userTableRepository.findById(email).orElse(null);
        usersLog userslog =usersLog.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .info("Deleted ")
                .build();

        try{
            userTableRepository.deleteById(email);
            userLogRepository.save(userslog);
            ResponseCookie cookie = ResponseCookie.from("JWTKEY","").path("/").sameSite("Strict").maxAge(0).httpOnly(true).secure(false).build();
            HttpHeaders headers=new HttpHeaders();
            headers.add(HttpHeaders.SET_COOKIE,cookie.toString());
            return new ResponseEntity<>("Deleted successfully",headers,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete",HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
