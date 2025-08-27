package com.security.AuthShield.controller;


import com.security.AuthShield.service.adminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("authShield/v1/admin/")
@CrossOrigin(origins = "http://localhost:5173" , allowCredentials = "true", allowedHeaders = "*" , methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PUT,RequestMethod.OPTIONS,RequestMethod.HEAD})
@RestController
public class adminController {

    @Autowired
    private adminService adminService;


    @GetMapping("/logs")
    public ResponseEntity<?> getLogs(){
        return adminService.getLogs();
    }

    @GetMapping("/usersData")
    public ResponseEntity<?> getUsersData(){
        return adminService.getUsersData();
    }
}
