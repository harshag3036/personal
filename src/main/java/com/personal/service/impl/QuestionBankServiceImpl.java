package com.personal.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.model.db.QuestionBankEntity;
import com.personal.model.request.CreateQuestionBankRequest;
import com.personal.model.response.QuestionBankDetailResponse;
import com.personal.model.response.QuestionIdWithSequence;
import com.personal.repository.QuestionBankRepository;
import com.personal.service.QuestionBankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class QuestionBankServiceImpl implements QuestionBankService {

    @Autowired
    private QuestionBankRepository questionBankRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public QuestionBankDetailResponse createQuestionBank(CreateQuestionBankRequest createQuestionBankRequest) {
        try {
            // Convert full_problem and full_solution objects to JSON strings
            String questionString = objectMapper.writeValueAsString(createQuestionBankRequest.getFullProblem());
            String solutionString = objectMapper.writeValueAsString(createQuestionBankRequest.getFullSolution());

            // Extract final_answer from full_solution
            List<String> finalAnswer = null;
            if (createQuestionBankRequest.getFullSolution() != null) {
                finalAnswer = createQuestionBankRequest.getFullSolution().getFinalAnswer();
            }

            // Extract processing info if available
            Double totalTime = null;
            Double totalCost = null;
            if (createQuestionBankRequest.getProcessingInfo() != null) {
                totalTime = createQuestionBankRequest.getProcessingInfo().getTotalTimeSeconds();
                totalCost = createQuestionBankRequest.getProcessingInfo().getEstimatedCostUsd();
            }

            // Build the entity
            QuestionBankEntity questionBankEntity = QuestionBankEntity.builder()
                    .questionIdS3(createQuestionBankRequest.getQuestionId())
                    .questionImage(createQuestionBankRequest.getQuestionImage())
                    .questionString(questionString)
                    .solutionString(solutionString)
                    .includesDiagram(createQuestionBankRequest.getHasDiagram())
                    .bloomsTagging(createQuestionBankRequest.getMetadata().getBloomsTagging())
                    .difficultyLevel(createQuestionBankRequest.getMetadata().getDifficultyLevel())
                    .subject(createQuestionBankRequest.getMetadata().getSubject())
                    .topics(createQuestionBankRequest.getMetadata().getTopics())
                    .subtopics(createQuestionBankRequest.getMetadata().getSubtopics())
                    .typeOfQuestion(createQuestionBankRequest.getMetadata().getTypeOfQuestion())
                    .totalTime(totalTime)
                    .totalCost(totalCost)
                    .finalAnswer(finalAnswer)
                    .build();

            // Save to database
            questionBankEntity = questionBankRepository.save(questionBankEntity);
            log.info("Question bank saved successfully with ID: {}", questionBankEntity.getQuestionId());

            // Return response
            return mapEntityToResponse(questionBankEntity);

        } catch (JsonProcessingException e) {
            log.error("Error converting question/solution to JSON string", e);
            throw new RuntimeException("Failed to process question bank data", e);
        }
    }

    @Override
    public QuestionBankDetailResponse getQuestionBankById(UUID questionId) {
        Optional<QuestionBankEntity> questionBankEntity = questionBankRepository.findById(questionId);
        if (questionBankEntity.isPresent()) {
            return mapEntityToResponse(questionBankEntity.get());
        } else {
            log.error("Question bank not found with ID: {}", questionId);
            throw new RuntimeException("Question bank not found");
        }
    }

    @Override
    public QuestionBankDetailResponse getQuestionBankByQuestionIdS3(String questionIdS3) {
        Optional<QuestionBankEntity> questionBankEntity = questionBankRepository.findByQuestionIdS3(questionIdS3);
        if (questionBankEntity.isPresent()) {
            return mapEntityToResponse(questionBankEntity.get());
        } else {
            log.error("Question bank not found with question_id_s3: {}", questionIdS3);
            throw new RuntimeException("Question bank not found");
        }
    }

    @Override
    public Page<QuestionBankDetailResponse> getAllQuestionBanks(Pageable pageable) {
        Page<QuestionBankEntity> questionBankEntities = questionBankRepository.findAll(pageable);
        return questionBankEntities.map(this::mapEntityToResponse);
    }

    @Override
    public Page<QuestionBankDetailResponse> getQuestionBanksBySubject(String subject, Pageable pageable) {
        Page<QuestionBankEntity> questionBankEntities = questionBankRepository.findBySubject(subject, pageable);
        if (questionBankEntities.isEmpty()) {
            log.warn("No question banks found for subject: {}", subject);
            return Page.empty();
        }
        return questionBankEntities.map(this::mapEntityToResponse);
    }

    @Override
    public List<String> getReferencesBySubject(String subject) {
        log.info("Fetching references for subject: {}", subject);
        List<String> references = questionBankRepository.findDistinctReferencesBySubject(subject);
        if (references.isEmpty()) {
            log.warn("No references found for subject: {}", subject);
        }
        return references;
    }

    @Override
    public java.util.Map<String, List<String>> getAllReferencesBySubjects() {
        log.info("Fetching all references grouped by subjects");
        List<String> allSubjects = questionBankRepository.findAllDistinctSubjects();
        
        java.util.Map<String, List<String>> referencesBySubject = new java.util.HashMap<>();
        for (String subject : allSubjects) {
            List<String> references = questionBankRepository.findDistinctReferencesBySubject(subject);
            if (!references.isEmpty()) {
                referencesBySubject.put(subject, references);
            }
        }
        
        log.info("Found references for {} subjects", referencesBySubject.size());
        return referencesBySubject;
    }

    @Override
    public List<QuestionIdWithSequence> getQuestionIdsBySubjectAndReference(String subject, String reference) {
        log.info("Fetching question IDs for subject: {} and reference: {}", subject, reference);
        List<QuestionBankEntity> entities = questionBankRepository.findBySubjectAndReference(subject, reference);
        
        List<QuestionIdWithSequence> result = new ArrayList<>();
        int sequenceNumber = 1;
        
        for (QuestionBankEntity entity : entities) {
            QuestionIdWithSequence idWithSeq = QuestionIdWithSequence.builder()
                    .id(sequenceNumber++)
                    .questionId(entity.getQuestionId())
                    .build();
            result.add(idWithSeq);
        }
        
        log.info("Found {} question IDs for subject: {} and reference: {}", result.size(), subject, reference);
        return result;
    }

    private QuestionBankDetailResponse mapEntityToResponse(QuestionBankEntity entity) {
        return QuestionBankDetailResponse.builder()
                .questionId(entity.getQuestionId())
                .questionIdS3(entity.getQuestionIdS3())
                .questionImage(entity.getQuestionImage())
                .questionString(entity.getQuestionString())
                .solutionString(entity.getSolutionString())
                .includesDiagram(entity.getIncludesDiagram())
                .bloomsTagging(entity.getBloomsTagging())
                .difficultyLevel(entity.getDifficultyLevel())
                .subject(entity.getSubject())
                .topics(entity.getTopics())
                .subtopics(entity.getSubtopics())
                .typeOfQuestion(entity.getTypeOfQuestion())
                .totalTime(entity.getTotalTime())
                .totalCost(entity.getTotalCost())
                .finalAnswer(entity.getFinalAnswer())
                .build();
    }
}
