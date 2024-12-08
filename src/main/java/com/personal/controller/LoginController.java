package com.personal.controller;

import com.personal.exception.AuthenticationException;
import com.personal.model.request.AuthRequest;
import com.personal.model.response.AuthResponse;
import com.personal.model.response.ErrorResponse;
import com.personal.service.LoginService;
import com.personal.util.JwtUtil;
import io.github.bucket4j.Bucket;
import io.micrometer.core.annotation.Timed;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "${api.v1.prefix}")
@Slf4j
@CrossOrigin
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private Bucket authenticationBucket;

    @PostMapping("/login")
    @Timed(value = "login.time", description = "Time taken to process login request")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) {
        // Check rate limit
        if (!authenticationBucket.tryConsume(1)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(new ErrorResponse(
                        "Too many login attempts. Please try again later.",
                        "RATE_LIMIT_EXCEEDED"
                    ));
        }

        log.info("Login attempt for username: {}", request.getUsername());

        try {
            // Authenticate the user
            if (loginService.login(request.getUsername(), request.getPassword())) {
                String customerId = loginService.getCustomerIdByUsername(request.getUsername());
                String token = JwtUtil.generateToken(request.getUsername(), customerId);
                Boolean firstLogin = loginService.checkFirstTimeLogin(customerId);
                
                log.info("Successful login for username: {}", request.getUsername());
                return ResponseEntity.ok(new AuthResponse(token, true, firstLogin, UUID.fromString(customerId)));
            } else {
                log.warn("Failed login attempt for username: {}", request.getUsername());
                throw AuthenticationException.invalidCredentials();
            }
        } catch (AuthenticationException | com.personal.exception.UsernameException e) {
            // Let these exceptions be handled by GlobalExceptionHandler
            throw e;
        } catch (Exception e) {
            log.error("Error during login for username: {}", request.getUsername(), e);
            throw new RuntimeException("An error occurred during login");
        }
    }

    @PostMapping("/signin")
    @Timed(value = "signin.time", description = "Time taken to process signin request")
    public ResponseEntity<?> signIn(@Valid @RequestBody AuthRequest request) {
        // Check rate limit
        if (!authenticationBucket.tryConsume(1)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(new ErrorResponse(
                        "Too many signin attempts. Please try again later.",
                        "RATE_LIMIT_EXCEEDED"
                    ));
        }

        log.info("Signin attempt for username: {}", request.getUsername());

        try {
            if (loginService.signIn(request.getUsername(), request.getPassword())) {
                String customerId = loginService.getCustomerIdByUsername(request.getUsername());
                String token = JwtUtil.generateToken(request.getUsername(), customerId);
                Boolean firstLogin = loginService.checkFirstTimeLogin(customerId);

                log.info("Successful signin for username: {}", request.getUsername());
                return ResponseEntity.ok(new AuthResponse(token, true, firstLogin, UUID.fromString(customerId)));
            } else {
                log.warn("Failed signin attempt for username: {}", request.getUsername());
                throw new RuntimeException("Signin failed");
            }
        } catch (com.personal.exception.UsernameException e) {
            // Let UsernameException be handled by GlobalExceptionHandler
            throw e;
        } catch (Exception e) {
            log.error("Error during signin for username: {}", request.getUsername(), e);
            throw new RuntimeException("An error occurred during signin");
        }
    }
}
