package com.security.AuthShield.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class verifyResetEmailOtp {
    @NotBlank(message = "email must not be empty")
    @Email(message = "Enter a valid email")
    private String email;
    @NotBlank(message = "OTP must not be empty")
    @Size(min = 6,max = 6,message = "OTP in must be the length of 6")
    private String otp;
}
