package com.security.AuthShield.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class frontendController {

    @GetMapping(value = {"/","/login","/resetpassword","/verifyotp","/dashboard","/adminpanel"})
    public String forward(){
        return "forward:/index.html";
    }
}
