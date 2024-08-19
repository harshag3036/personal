package com.personal.model.db;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "login")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginEntity {

    @Id
    private String username;

    private String password;
}
