package com.personal.model.db;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "uploaded_files")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilesEntity extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;

    @Lob
    private byte[] fileData;

    private LocalDateTime uploadedAt = LocalDateTime.now();
}
