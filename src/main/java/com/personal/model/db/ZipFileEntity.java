package com.personal.model.db;

import jakarta.persistence.*;
import lombok.*;

@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "zip_files")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZipFileEntity extends AuditModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String filename;
    
    @Column(nullable = false)
    private String originalFilename;
    
    @Column(nullable = false)
    private Long fileSize;
    
    @Column(nullable = false)
    private String contentType;
    
    // Store PostgreSQL Large Object ID for efficient handling of large files
    @Column(nullable = false)
    private Long largeObjectId;
    
    // Store file binary data directly in the table using BYTEA
    @Column(name = "file_data", columnDefinition = "BYTEA")
    private byte[] fileData;
    
    @Column(nullable = false)
    private String checksum; // MD5 checksum for integrity verification
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FileStatus status = FileStatus.ACTIVE;
    
    private String description;
    
    public enum FileStatus {
        ACTIVE, DELETED, CORRUPTED
    }
}
