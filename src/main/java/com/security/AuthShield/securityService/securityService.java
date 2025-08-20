package com.security.AuthShield.securityService;

import com.security.AuthShield.model.users;
import com.security.AuthShield.repository.userTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
public class securityService  implements UserDetailsService {

    @Autowired
    private userTableRepository userTableRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        users user = userTableRepository.findById(username).orElse(null);

        return User.builder().username(user.getEmail()).password(user.getPassword()).roles("user").build();
    }
}
