package com.personal.repository;

import com.personal.model.db.FilesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilesRepository extends JpaRepository<FilesEntity, Long> {

}
