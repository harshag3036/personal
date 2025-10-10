package com.personal.repository;

import com.personal.model.db.QuestionBankEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuestionBankRepository extends JpaRepository<QuestionBankEntity, UUID>, PagingAndSortingRepository<QuestionBankEntity, UUID> {
    
    Optional<QuestionBankEntity> findByQuestionIdS3(String questionIdS3);
    
    Page<QuestionBankEntity> findBySubject(String subject, Pageable pageable);
    
    @Query("SELECT DISTINCT q.reference FROM QuestionBankEntity q WHERE q.subject = :subject AND q.reference IS NOT NULL")
    List<String> findDistinctReferencesBySubject(String subject);
    
    @Query("SELECT DISTINCT q.subject FROM QuestionBankEntity q WHERE q.subject IS NOT NULL ORDER BY q.subject")
    List<String> findAllDistinctSubjects();
    
    @Query("SELECT q FROM QuestionBankEntity q WHERE q.subject = :subject AND q.reference = :reference ORDER BY q.difficultyLevel DESC")
    List<QuestionBankEntity> findBySubjectAndReference(String subject, String reference);
}
