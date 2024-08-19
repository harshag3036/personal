package com.personal.repository;

import com.personal.model.db.LoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginTableRepository extends JpaRepository<LoginEntity, String> {}

