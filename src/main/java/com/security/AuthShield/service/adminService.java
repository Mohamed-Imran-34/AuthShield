package com.security.AuthShield.service;


import com.security.AuthShield.repository.userLogRepository;
import com.security.AuthShield.repository.userTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class adminService {

    @Autowired
    private userTableRepository userTableRepository;
    @Autowired
    private userLogRepository userLogRepository;

    public ResponseEntity<?> getLogs() {


        return new ResponseEntity<>(userLogRepository.findByAllOrder(),HttpStatus.OK);

    }

    public ResponseEntity<?> getUsersData() {

        return new ResponseEntity<>(userTableRepository.findByAdmin(),HttpStatus.OK);
    }
}
