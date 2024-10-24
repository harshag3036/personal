package com.personal.repository;

import com.personal.model.db.CommentsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CommentsTableRepository extends JpaRepository<CommentsEntity, UUID> {

    Page<CommentsEntity> findByPostId(UUID postId, Pageable pageable);

}
