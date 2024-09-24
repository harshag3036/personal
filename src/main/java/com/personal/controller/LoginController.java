package com.personal.controller;

import com.personal.model.response.AuthResponse;
import com.personal.service.LoginService;
import com.personal.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "${api.v1.prefix}")
@Slf4j
@CrossOrigin
public class LoginController {

    @Autowired
    LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
        log.info("Login called with username: {} and password: {}", username, password);

        // Authenticate the user
        if (loginService.login(username, password)) {
            // Retrieve the customerId from the database
            String customerId = loginService.getCustomerIdByUsername(username);

            // Generate JWT token with username and customerId
            String token = JwtUtil.generateToken(username, customerId);
            Boolean firstLogin = loginService.checkFirstTimeLogin(customerId);
            return ResponseEntity.ok(new AuthResponse(token,true,firstLogin));
        } else {
            // Return unauthorized response if login fails
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials");
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestParam String username, @RequestParam String password) {
        log.info("SignIn called with username: {} and password: {}", username, password);

        // Authenticate the user (Sign-up logic)
        if (loginService.signIn(username, password)) {
            // Retrieve the customerId after sign-in
            String customerId = loginService.getCustomerIdByUsername(username);

            // Generate JWT token with username and customerId
            String token = JwtUtil.generateToken(username, customerId);

            Boolean firstLogin = loginService.checkFirstTimeLogin(customerId);
            return ResponseEntity.ok(new AuthResponse(token,true,firstLogin));
        } else {
            // Return unauthorized response if sign-in fails
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sign-in Failed");
        }
    }


}
