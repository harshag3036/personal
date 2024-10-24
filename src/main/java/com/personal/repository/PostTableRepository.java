package com.personal.repository;

import com.personal.model.db.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostTableRepository extends JpaRepository<PostEntity, UUID>, PagingAndSortingRepository<PostEntity, UUID> {

    Page<PostEntity> findByCustomerId(UUID customerId, Pageable pageable);
}
