package com.security.AuthShield.configuration;


import com.security.AuthShield.jwtservice.jwtFilterChain;
import com.security.AuthShield.securityService.securityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class securityConfiguration {

    @Autowired
    private jwtFilterChain jwtFilterChain;
    @Autowired
    private securityService securityService;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(requests->requests.requestMatchers("/adminpanel","/authShield/v1/admin/**").hasRole("admin").requestMatchers("/authShield/v1/register","/authShield/v1/login","/authShield/v1/resetOtp","/authShield/v1/verifyResetOtp","/authShield/v1/reset/accountPassword","/","/login","/resetpassword","/dashboard","/static/**","/index.html","/manifest.json","/assets/**").permitAll().anyRequest().authenticated())
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .logout(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtFilterChain, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(securityService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        return provider;
    }


}
