package com.personal.service.impl;

import com.personal.model.db.ZipFileEntity;
import com.personal.model.response.ZipFileResponse;
import com.personal.repository.ZipFileRepository;
import com.personal.service.ZipFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ZipFileServiceImpl implements ZipFileService {

    private final ZipFileRepository zipFileRepository;
    
    private static final int CHUNK_SIZE = 64 * 1024; // 64KB chunks for efficient processing
    private static final long MAX_FILE_SIZE = 1024L * 1024L * 1024L; // 1GB limit

    @Override
    @Transactional
    public ZipFileResponse uploadZipFile(MultipartFile file, String description, String customFilename) throws IOException {
        log.info("Starting upload for file: {} (size: {} bytes)", file.getOriginalFilename(), file.getSize());
        
        try {
            // Validate file
            validateFile(file);
            
            // Read file content into byte array with checksum calculation
            byte[] fileContent;
            String checksum;
            
            try (InputStream inputStream = file.getInputStream()) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                
                byte[] buffer = new byte[CHUNK_SIZE];
                int bytesRead;
                long totalBytes = 0;
                
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    baos.write(buffer, 0, bytesRead);
                    md5.update(buffer, 0, bytesRead);
                    totalBytes += bytesRead;
                    
                    if (totalBytes % (1024 * 1024) == 0) { // Log every MB
                        log.debug("Read {} MB", totalBytes / (1024 * 1024));
                    }
                }
                
                fileContent = baos.toByteArray();
                checksum = bytesToHex(md5.digest());
                log.info("File read successfully: {} bytes, checksum: {}", fileContent.length, checksum);
            }
            
            // Determine filename to use
            String finalFilename = (customFilename != null && !customFilename.trim().isEmpty()) 
                ? customFilename.trim() 
                : file.getOriginalFilename();
            
            // Create entity with file content
            ZipFileEntity entity = new ZipFileEntity();
            entity.setFilename(finalFilename);
            entity.setOriginalFilename(file.getOriginalFilename());
            entity.setFileSize(file.getSize());
            entity.setContentType(file.getContentType());
            entity.setLargeObjectId(0L); // Not using large objects anymore
            entity.setFileData(fileContent); // Store binary data directly
            entity.setChecksum(checksum);
            entity.setDescription(description);
            entity.setStatus(ZipFileEntity.FileStatus.ACTIVE);
            
            entity = zipFileRepository.save(entity);
            log.info("Saved file entity with ID: {} and {} bytes of data", entity.getId(), fileContent.length);
            
            return mapToResponse(entity);
            
        } catch (NoSuchAlgorithmException e) {
            log.error("MD5 algorithm not available", e);
            throw new IOException("MD5 algorithm not available", e);
        } catch (Exception e) {
            log.error("Error uploading file: {}", e.getMessage(), e);
            throw new IOException("Failed to upload file: " + e.getMessage(), e);
        }
    }

    @Override
    public InputStream downloadZipFile(Long fileId) throws IOException {
        log.info("Starting download for file ID: {}", fileId);
        
        Optional<ZipFileEntity> entityOpt = zipFileRepository.findByIdAndStatus(fileId, ZipFileEntity.FileStatus.ACTIVE);
        if (entityOpt.isEmpty()) {
            log.warn("File not found or inactive: {}", fileId);
            throw new IOException("File not found or inactive: " + fileId);
        }
        
        ZipFileEntity entity = entityOpt.get();
        log.info("Found file: {} (size: {} bytes)", entity.getFilename(), entity.getFileSize());
        
        // Get file content directly from entity
        byte[] fileContent = entity.getFileData();
        if (fileContent == null || fileContent.length == 0) {
            log.error("File content is null or empty for file ID: {}", fileId);
            throw new IOException("File content not available for file ID: " + fileId);
        }
        
        log.info("Retrieved file content: {} bytes", fileContent.length);
        
        // Create ByteArrayInputStream for reliable streaming
        return new ByteArrayInputStream(fileContent);
    }

    @Override
    public ZipFileResponse getFileMetadata(Long fileId) {
        Optional<ZipFileEntity> entityOpt = zipFileRepository.findByIdAndStatus(fileId, ZipFileEntity.FileStatus.ACTIVE);
        return entityOpt.map(this::mapToResponse).orElse(null);
    }

    @Override
    public List<ZipFileResponse> getAllActiveFiles() {
        List<ZipFileEntity> entities = zipFileRepository.findByStatusOrderByCreatedAtDesc(ZipFileEntity.FileStatus.ACTIVE);
        return entities.stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public boolean deleteZipFile(Long fileId) {
        Optional<ZipFileEntity> entityOpt = zipFileRepository.findByIdAndStatus(fileId, ZipFileEntity.FileStatus.ACTIVE);
        if (entityOpt.isEmpty()) {
            return false;
        }
        
        ZipFileEntity entity = entityOpt.get();
        entity.setStatus(ZipFileEntity.FileStatus.DELETED);
        zipFileRepository.save(entity);
        log.info("Soft deleted file: {}", fileId);
        return true;
    }

    @Override
    @Transactional
    public boolean permanentlyDeleteZipFile(Long fileId) {
        Optional<ZipFileEntity> entityOpt = zipFileRepository.findById(fileId);
        if (entityOpt.isEmpty()) {
            return false;
        }
        
        ZipFileEntity entity = entityOpt.get();
        
        // Delete the entity (file content will be deleted automatically)
        zipFileRepository.delete(entity);
        log.info("Permanently deleted file: {}", fileId);
        return true;
    }

    @Override
    public boolean verifyFileIntegrity(Long fileId) {
        Optional<ZipFileEntity> entityOpt = zipFileRepository.findByIdAndStatus(fileId, ZipFileEntity.FileStatus.ACTIVE);
        if (entityOpt.isEmpty()) {
            return false;
        }
        
        ZipFileEntity entity = entityOpt.get();
        
        try {
            // Get file content directly from entity
            byte[] fileContent = entity.getFileData();
            if (fileContent == null || fileContent.length == 0) {
                log.error("File content is null or empty for file ID: {}", fileId);
                entity.setStatus(ZipFileEntity.FileStatus.CORRUPTED);
                zipFileRepository.save(entity);
                return false;
            }
            
            String calculatedChecksum = calculateChecksum(fileContent);
            
            boolean isValid = entity.getChecksum().equals(calculatedChecksum);
            if (!isValid) {
                log.warn("File integrity check failed for file {}: expected {}, got {}", 
                        fileId, entity.getChecksum(), calculatedChecksum);
                entity.setStatus(ZipFileEntity.FileStatus.CORRUPTED);
                zipFileRepository.save(entity);
            }
            
            return isValid;
        } catch (Exception e) {
            log.error("Error verifying file integrity for {}: {}", fileId, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Long getTotalStorageUsed() {
        Long total = zipFileRepository.getTotalStorageUsed(ZipFileEntity.FileStatus.ACTIVE);
        return total != null ? total : 0L;
    }

    @Override
    public ZipFileResponse getStorageStats() {
        // Get total storage used
        Long totalSize = getTotalStorageUsed();
        
        // Get total file count
        List<ZipFileEntity> activeFiles = zipFileRepository.findByStatusOrderByCreatedAtDesc(ZipFileEntity.FileStatus.ACTIVE);
        long totalFiles = activeFiles.size();
        
        return ZipFileResponse.builder()
                .filename("Storage Statistics")
                .description(String.format("Total files: %d", totalFiles))
                .fileSize(totalSize)
                .build();
    }

    private void validateFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("File is empty");
        }
        
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IOException("File size exceeds maximum limit of " + (MAX_FILE_SIZE / (1024 * 1024)) + " MB");
        }
        
        String contentType = file.getContentType();
        String filename = file.getOriginalFilename();
        
        if ((contentType == null || (!contentType.equals("application/zip") && !contentType.equals("application/x-zip-compressed"))) &&
            (filename == null || !filename.toLowerCase().endsWith(".zip"))) {
            throw new IOException("File must be a ZIP archive");
        }
    }

    private String calculateChecksum(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] digest = md5.digest(data);
        return bytesToHex(digest);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private ZipFileResponse mapToResponse(ZipFileEntity entity) {
        return ZipFileResponse.builder()
                .id(entity.getId())
                .filename(entity.getFilename())
                .originalFilename(entity.getOriginalFilename())
                .fileSize(entity.getFileSize())
                .contentType(entity.getContentType())
                .checksum(entity.getChecksum())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .modifiedAt(entity.getModifiedAt())
                .createdBy(entity.getCreatedBy())
                .modifiedBy(entity.getModifiedBy())
                .build();
    }
}
