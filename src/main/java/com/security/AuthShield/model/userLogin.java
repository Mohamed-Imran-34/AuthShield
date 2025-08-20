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
public class userLogin {

    @NotBlank(message = "Email must nto be null")
    @Email(message = "Enter a valid email")
    private String email;
    @Size(min =6 ,max =16, message = "Password  contains atLeast 6 chars and atMax of 16 chars")
    private String password;
}
