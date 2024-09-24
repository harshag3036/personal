package com.personal.model.db;

import com.personal.model.constant.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "customer")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEntity extends AuditModel{
    @Id
    @Column(name = "customer_id")
    private UUID customerId;

    @Column(name = "name")
    private String name;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name="gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;
}
