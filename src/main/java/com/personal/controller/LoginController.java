package com.personal.controller;

import com.personal.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "${api.v1.prefix}")
@Slf4j
@CrossOrigin
public class LoginController {

    @Autowired
    LoginService loginService;

    @GetMapping("/login")
    public boolean login(@RequestParam String username, @RequestParam String password) {
        log.info("Login called with username: {} and password: {}", username, password);
        return true;
    }
}
