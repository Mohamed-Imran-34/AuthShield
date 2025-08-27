package com.security.AuthShield.jwtservice;

import com.security.AuthShield.securityService.securityService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class jwtFilterChain extends OncePerRequestFilter {


    @Autowired
    private securityService securityService;

    @Autowired
    private jwtUtils jwtUtils;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token=null;
        User user=null;
        String username=null;
        final List<String> paths= new ArrayList<>(Arrays.asList("/authShield/v1/register","/authShield/v1/login","/authShield/v1/resetOtp","/authShield/v1/verifyResetOtp","/authShield/v1/reset/accountPassword","/","/login","/resetpassword","/dashboard"));
        String path =request.getServletPath();
        if(paths.contains(path)){
            filterChain.doFilter(request,response);
            return;
        }

        Cookie[] cookies=request.getCookies();
        if (cookies != null){
        for(Cookie cookie : cookies){
            if("JWTKEY".equals(cookie.getName())){
                token=cookie.getValue();
                break;
            }
        }}

        if(token != null){
            username=jwtUtils.getUsername(token);
        }
        if(token!= null && username!=null){
            user= (User) securityService.loadUserByUsername(username);
        }

        if(token!=null && user!=null && SecurityContextHolder.getContext().getAuthentication() ==null){
            if(jwtUtils.validateToken(token,user.getUsername())){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request,response);
    }
}
