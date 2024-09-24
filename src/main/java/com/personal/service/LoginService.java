package com.personal.service;

public interface LoginService {
    public boolean login(String username, String password);

    public boolean signIn(String username, String password);

    String getCustomerIdByUsername(String username);

    Boolean checkFirstTimeLogin(String customerId);
}
