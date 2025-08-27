package com.security.AuthShield.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class userDtoAdmin {
    private String email;
    private String username;
    private Boolean isAccountVerified;
}
