package com.personal.model.db;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "login")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "customer_id")
    private UUID customerId;
    private String username;

    private String password;
}
