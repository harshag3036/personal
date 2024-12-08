package com.personal.service.impl;

import com.personal.exception.UsernameException;
import com.personal.model.db.LoginEntity;
import com.personal.repository.CustomerRepository;
import com.personal.repository.LoginTableRepository;
import com.personal.service.LoginService;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginTableRepository loginTableRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MeterRegistry meterRegistry;

    public boolean login(String username, String password) {
        try {
            LoginEntity loginEntity = loginTableRepository.findByUsername(username)
                .orElseThrow(() -> {
                    meterRegistry.counter("login.failure", "reason", "user_not_found").increment();
                    return UsernameException.userNotFound();
                });

            boolean isValid = passwordEncoder.matches(password, loginEntity.getPassword());
            if (isValid) {
                meterRegistry.counter("login.success").increment();
                return true;
            } else {
                meterRegistry.counter("login.failure", "reason", "invalid_password").increment();
                return false;
            }
        } catch (UsernameException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error during login validation", e);
            meterRegistry.counter("login.error").increment();
            return false;
        }
    }

    @Override
    public boolean signIn(String username, String password) {
        try {
            if (loginTableRepository.findByUsername(username).isPresent()) {
                meterRegistry.counter("signin.failure", "reason", "username_exists").increment();
                throw UsernameException.userAlreadyExists();
            }

            LoginEntity loginEntity = new LoginEntity();
            loginEntity.setUsername(username);
            loginEntity.setPassword(password); // Password will be encrypted via @PrePersist
            loginEntity.setCustomerId(UUID.randomUUID());
            loginTableRepository.save(loginEntity);

            meterRegistry.counter("signin.success").increment();
            return true;
        } catch (UsernameException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error during signin", e);
            meterRegistry.counter("signin.error").increment();
            return false;
        }
    }

    @Override
    public String getCustomerIdByUsername(String username) {
        try {
            return Objects.requireNonNull(loginTableRepository.findByUsername(username)
                    .orElseThrow(() -> UsernameException.userNotFound()))
                    .getCustomerId().toString();
        } catch (Exception e) {
            log.error("Error retrieving customer ID for username: {}", username, e);
            throw e;
        }
    }

    @Override
    public Boolean checkFirstTimeLogin(String customerId) {
        try {
            return ObjectUtils.isEmpty(customerRepository.findById(UUID.fromString(customerId)).orElse(null));
        } catch (Exception e) {
            log.error("Error checking first time login for customer ID: {}", customerId, e);
            return false;
        }
    }
}
