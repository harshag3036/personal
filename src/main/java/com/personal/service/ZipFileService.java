package com.personal.service;

import com.personal.model.response.ZipFileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface ZipFileService {
    
    /**
     * Upload a zip file to PostgreSQL database using Large Object API
     * @param file The multipart file to upload
     * @param description Optional description for the file
     * @param customFilename Optional custom filename
     * @return ZipFileResponse containing file metadata
     * @throws IOException if file processing fails
     */
    ZipFileResponse uploadZipFile(MultipartFile file, String description, String customFilename) throws IOException;
    
    /**
     * Download a zip file by ID as InputStream for efficient streaming
     * @param fileId The ID of the file to download
     * @return InputStream for the file content
     * @throws IOException if file retrieval fails
     */
    InputStream downloadZipFile(Long fileId) throws IOException;
    
    /**
     * Get file metadata by ID
     * @param fileId The ID of the file
     * @return ZipFileResponse containing file metadata
     */
    ZipFileResponse getFileMetadata(Long fileId);
    
    /**
     * Get all active zip files
     * @return List of ZipFileResponse
     */
    List<ZipFileResponse> getAllActiveFiles();
    
    /**
     * Soft delete a zip file (mark as deleted)
     * @param fileId The ID of the file to delete
     * @return true if deletion was successful
     */
    boolean deleteZipFile(Long fileId);
    
    /**
     * Permanently delete a zip file from database
     * @param fileId The ID of the file to permanently delete
     * @return true if deletion was successful
     */
    boolean permanentlyDeleteZipFile(Long fileId);
    
    /**
     * Verify file integrity using checksum
     * @param fileId The ID of the file to verify
     * @return true if file integrity is valid
     */
    boolean verifyFileIntegrity(Long fileId);
    
    /**
     * Get total storage used by active files
     * @return Total storage in bytes
     */
    Long getTotalStorageUsed();

    ZipFileResponse getStorageStats();
}
