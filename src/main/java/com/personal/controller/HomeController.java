package com.personal.controller;

import com.personal.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "${api.v1.prefix}")
@CrossOrigin
public class HomeController {



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
}