package com.personal.repository;

import com.personal.model.db.QuestionBankEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuestionBankRepository extends JpaRepository<QuestionBankEntity, UUID>, PagingAndSortingRepository<QuestionBankEntity, UUID> {
    
    Optional<QuestionBankEntity> findByQuestionIdS3(String questionIdS3);
    
    Page<QuestionBankEntity> findBySubject(String subject, Pageable pageable);
}
