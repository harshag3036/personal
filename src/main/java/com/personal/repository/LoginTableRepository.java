package com.personal.repository;

import com.personal.model.db.LoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LoginTableRepository extends JpaRepository<LoginEntity, UUID> {
    Optional<LoginEntity> findByUsername(String username);
}

