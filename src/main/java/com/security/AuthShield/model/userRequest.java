package com.security.AuthShield.model;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class userRequest {
    @NotBlank(message = "Username must not be empty")
    private String username;
    @Email(message = "Enter a valid email")
    @NotBlank(message = "Email must not be empty")
    private String email;
    @Size(min =6 ,max =16, message = "Password  contains atLeast 6 chars and atMax of 16 chars")
    private String password;

}
