package com.personal.repository;

import com.personal.model.db.ZipFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ZipFileRepository extends JpaRepository<ZipFileEntity, Long> {
    
    Optional<ZipFileEntity> findByIdAndStatus(Long id, ZipFileEntity.FileStatus status);
    
    List<ZipFileEntity> findByStatusOrderByCreatedAtDesc(ZipFileEntity.FileStatus status);
    
    Optional<ZipFileEntity> findByFilenameAndStatus(String filename, ZipFileEntity.FileStatus status);
    
    @Query("SELECT z FROM ZipFileEntity z WHERE z.status = :status AND z.fileSize > :minSize")
    List<ZipFileEntity> findLargeFilesByStatus(@Param("status") ZipFileEntity.FileStatus status, 
                                               @Param("minSize") Long minSize);
    
    @Query("SELECT SUM(z.fileSize) FROM ZipFileEntity z WHERE z.status = :status")
    Long getTotalStorageUsed(@Param("status") ZipFileEntity.FileStatus status);
}
