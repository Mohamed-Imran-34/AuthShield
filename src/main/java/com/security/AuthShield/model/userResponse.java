package com.security.AuthShield.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class userResponse {
    private String username;
    private String email;
    private Boolean isAccountVerified;
}
