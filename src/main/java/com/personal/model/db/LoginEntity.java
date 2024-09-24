package com.personal.model.db;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "login")
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginEntity extends AuditModel{

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "customer_id")
    private UUID customerId;
    private String username;

    private String password;
}
