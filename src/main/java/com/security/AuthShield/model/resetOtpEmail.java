package com.security.AuthShield.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class resetOtpEmail {
    @NotBlank(message = "email must not be empty")
    @Email(message = "Enter a valid email")
    private String email;
}
