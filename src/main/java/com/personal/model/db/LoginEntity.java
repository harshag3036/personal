package com.personal.model.db;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

@Entity
@Table(name = "login")
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginEntity extends AuditModel {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "customer_id")
    private UUID customerId;

    @Column(unique = true)
    private String username;

    @Column(nullable = false, length = 60) // BCrypt generates 60 character hashes
    private String password;

    public void setPassword(String password) {
        if (password != null) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            this.password = encoder.encode(password);
        }
    }
}
