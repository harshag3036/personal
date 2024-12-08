package com.personal.model.db;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "comments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentsEntity extends AuditModel {
    @Id
    @Column(name = "comment_id")
    @GeneratedValue(generator = "UUID")
    private UUID commentId;

    @Column(columnDefinition = "TEXT")
    private String comment;

    private UUID author;

    @Column(name = "post_id")
    private UUID postId;

    @Builder.Default
    private Integer likes = 0;
}
