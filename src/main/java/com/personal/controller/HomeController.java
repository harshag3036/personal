package com.personal.controller;

import com.personal.model.db.CustomerEntity;
import com.personal.model.request.CreateCustomerRequest;
import com.personal.service.HomeService;
import com.personal.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "${api.v1.prefix}")
@CrossOrigin
public class HomeController {

    @Autowired
    private HomeService homeService;



    @GetMapping("/home")
    public ResponseEntity<?> openHomePage(@RequestHeader("Authorization") String token) {
        // Extract the token from the "Bearer" prefix
        String jwtToken = token.substring(7);

        // Validate the token
        if (JwtUtil.isTokenExpired(jwtToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        // Extract customerId from the token
        String customerId = JwtUtil.extractCustomerId(jwtToken);

        // Return customerId (or other user-specific data)
        return ResponseEntity.ok("Welcome Customer ID: " + customerId);
    }

    @PostMapping("/customerData")
    public ResponseEntity<?> saveCustomerData(@RequestHeader("Authorization") String token , @RequestBody CreateCustomerRequest createCustomerRequest) {
        // Extract the token from the "Bearer" prefix
        String jwtToken = token.substring(7);

        // Validate the token
        if (JwtUtil.isTokenExpired(jwtToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        // Extract customerId from the token
        String customerId = JwtUtil.extractCustomerId(jwtToken);
        homeService.saveCustomerData(createCustomerRequest, customerId);
        // Return customerId (or other user-specific data)
        return ResponseEntity.ok("Customer Data for ID: " + customerId);
    }

    @GetMapping("/customerData")
    public ResponseEntity<?> saveCustomerData(@RequestHeader("Authorization") String token){
        // Extract the token from the "Bearer" prefix
        String jwtToken = token.substring(7);

        // Validate the token
        if (JwtUtil.isTokenExpired(jwtToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        // Extract customerId from the token
        String customerId = JwtUtil.extractCustomerId(jwtToken);
        // Return customerId (or other user-specific data)
        return ResponseEntity.ok(homeService.getCustomerData(customerId));
    }
}