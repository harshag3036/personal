package com.personal.model.db;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity

public class LoginTable {

    @Id
    private String username;

    private String password;
}
