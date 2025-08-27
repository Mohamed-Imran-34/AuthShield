package com.security.AuthShield.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table( name = "users_table")
public class users {
    private String username;
    private String password;
    @Id
    private String email;
    private Boolean isAccountVerified;
    private String resetOtp;
    private String verifyOtp;
    private Date resetExpiration;
    private Date verifyExpiration;
    private String role;

    @CurrentTimestamp
    @Column(updatable = false)
    private Date created;
    @UpdateTimestamp
    private Date updated;
}
