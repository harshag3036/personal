package com.personal.controller;

import com.personal.model.response.ZipFileResponse;
import com.personal.service.ZipFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/zip-files")
@RequiredArgsConstructor
public class ZipFileController {

    private final ZipFileService zipFileService;

    /**
     * Upload a zip file to PostgreSQL database
     * Supports large files up to 1GB with efficient streaming
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadZipFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "filename", required = false) String customFilename) {
        
        try {
            log.info("Received upload request for file: {}, size: {} bytes", 
                    file.getOriginalFilename(), file.getSize());
            
            ZipFileResponse response = zipFileService.uploadZipFile(file, description, customFilename);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "File uploaded successfully");
            result.put("data", response);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
            
        } catch (IOException e) {
            log.error("Error uploading file: {}", e.getMessage(), e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "File upload failed: " + e.getMessage());
            errorResponse.put("error", e.getClass().getSimpleName());
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            log.error("Unexpected error during file upload", e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Internal server error during file upload");
            errorResponse.put("error", e.getClass().getSimpleName());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Download a zip file by ID with efficient streaming
     * Supports large files with proper content headers
     */
    @GetMapping("/download/{fileId}")
    public ResponseEntity<?> downloadZipFile(@PathVariable Long fileId) {
        try {
            log.info("Received download request for file ID: {}", fileId);
            
            // Get file metadata first
            ZipFileResponse metadata = zipFileService.getFileMetadata(fileId);
            if (metadata == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "File not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            
            // Get file stream
            InputStream fileStream = zipFileService.downloadZipFile(fileId);
            InputStreamResource resource = new InputStreamResource(fileStream);
            
            // Set appropriate headers for file download
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, 
                    "attachment; filename=\"" + metadata.getFilename() + "\"");
            headers.add(HttpHeaders.CONTENT_TYPE, metadata.getContentType());
            headers.add(HttpHeaders.CONTENT_LENGTH, metadata.getFileSize().toString());
            headers.add("X-File-Checksum", metadata.getChecksum());
            headers.add("X-File-Original-Name", metadata.getOriginalFilename());
            
            log.info("Starting download stream for file: {}, size: {} bytes", 
                    metadata.getFilename(), metadata.getFileSize());
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(metadata.getFileSize())
                    .contentType(MediaType.parseMediaType(metadata.getContentType()))
                    .body(resource);
                    
        } catch (IOException e) {
            log.error("Error downloading file ID {}: {}", fileId, e.getMessage(), e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "File download failed: " + e.getMessage());
            errorResponse.put("error", e.getClass().getSimpleName());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        } catch (Exception e) {
            log.error("Unexpected error during file download for ID: {}", fileId, e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Internal server error during file download");
            errorResponse.put("error", e.getClass().getSimpleName());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Get file metadata by ID
     */
    @GetMapping("/{fileId}")
    public ResponseEntity<?> getFileMetadata(@PathVariable Long fileId) {
        try {
            ZipFileResponse response = zipFileService.getFileMetadata(fileId);
            
            if (response == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "File not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", response);
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            log.error("Error retrieving file metadata for ID: {}", fileId, e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error retrieving file metadata");
            errorResponse.put("error", e.getClass().getSimpleName());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Get all active zip files
     */
    @GetMapping
    public ResponseEntity<?> getAllActiveFiles() {
        try {
            List<ZipFileResponse> files = zipFileService.getAllActiveFiles();
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("count", files.size());
            result.put("data", files);
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            log.error("Error retrieving all files", e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error retrieving files");
            errorResponse.put("error", e.getClass().getSimpleName());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Soft delete a zip file
     */
    @DeleteMapping("/{fileId}")
    public ResponseEntity<?> deleteZipFile(@PathVariable Long fileId) {
        try {
            boolean deleted = zipFileService.deleteZipFile(fileId);
            
            Map<String, Object> result = new HashMap<>();
            if (deleted) {
                result.put("success", true);
                result.put("message", "File deleted successfully");
                return ResponseEntity.ok(result);
            } else {
                result.put("success", false);
                result.put("message", "File not found or already deleted");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
            }
            
        } catch (Exception e) {
            log.error("Error deleting file ID: {}", fileId, e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error deleting file");
            errorResponse.put("error", e.getClass().getSimpleName());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Permanently delete a zip file
     */
    @DeleteMapping("/{fileId}/permanent")
    public ResponseEntity<?> permanentlyDeleteZipFile(@PathVariable Long fileId) {
        try {
            boolean deleted = zipFileService.permanentlyDeleteZipFile(fileId);
            
            Map<String, Object> result = new HashMap<>();
            if (deleted) {
                result.put("success", true);
                result.put("message", "File permanently deleted successfully");
                return ResponseEntity.ok(result);
            } else {
                result.put("success", false);
                result.put("message", "File not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
            }
            
        } catch (Exception e) {
            log.error("Error permanently deleting file ID: {}", fileId, e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error permanently deleting file");
            errorResponse.put("error", e.getClass().getSimpleName());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Verify file integrity
     */
    @PostMapping("/{fileId}/verify")
    public ResponseEntity<?> verifyFileIntegrity(@PathVariable Long fileId) {
        try {
            boolean isValid = zipFileService.verifyFileIntegrity(fileId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("fileId", fileId);
            result.put("isValid", isValid);
            result.put("message", isValid ? "File integrity verified" : "File integrity check failed");
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            log.error("Error verifying file integrity for ID: {}", fileId, e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error verifying file integrity");
            errorResponse.put("error", e.getClass().getSimpleName());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Get storage statistics
     */
    @GetMapping("/stats")
    public ResponseEntity<?> getStorageStats() {
        try {
            Long totalStorage = zipFileService.getTotalStorageUsed();
            List<ZipFileResponse> files = zipFileService.getAllActiveFiles();
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalFiles", files.size());
            stats.put("totalStorageBytes", totalStorage);
            stats.put("totalStorageFormatted", formatFileSize(totalStorage));
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", stats);
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            log.error("Error retrieving storage stats", e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error retrieving storage statistics");
            errorResponse.put("error", e.getClass().getSimpleName());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    private String formatFileSize(Long bytes) {
        if (bytes == null || bytes == 0) return "0 B";
        
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.2f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
        return String.format("%.2f GB", bytes / (1024.0 * 1024.0 * 1024.0));
    }
}
