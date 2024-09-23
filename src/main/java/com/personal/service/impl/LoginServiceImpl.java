package com.personal.service.impl;

import com.personal.model.db.LoginEntity;
import com.personal.repository.LoginTableRepository;
import com.personal.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    LoginTableRepository loginTableRepository;
    public boolean login(String username, String password) {
        LoginEntity loginEntity = loginTableRepository.findByUsername(username).orElse(null);
        if (loginEntity == null) {
            return false;
        }
        return loginEntity.getPassword().equals(password);
    }

    @Override
    public boolean signIn(String username, String password) {
        LoginEntity loginEntity = loginTableRepository.findByUsername(username).orElse(null);
        if (loginEntity != null) {
            return false;
        }
        loginEntity = new LoginEntity();
        loginEntity.setUsername(username);
        loginEntity.setPassword(password);
        loginEntity.setCustomerId(UUID.randomUUID());
        loginTableRepository.save(loginEntity);
        return true;
    }

    @Override
    public String getCustomerIdByUsername(String username) {
        return loginTableRepository.findByUsername(username).orElse(null).getCustomerId().toString();
    }


}
