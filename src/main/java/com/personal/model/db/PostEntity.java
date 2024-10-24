package com.personal.model.db;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "posts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostEntity extends AuditModel {

    @Id
    @Column(name = "post_id")
    @GeneratedValue(generator = "UUID")
    private UUID postId;
    private String title;
    private String content;
    private UUID customerId;
}
