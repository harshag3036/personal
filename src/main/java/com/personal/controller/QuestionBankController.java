package com.personal.controller;

import com.personal.model.request.CreateQuestionBankRequest;
import com.personal.model.response.ApiResponse;
import com.personal.model.response.IdsByReferenceData;
import com.personal.model.response.QuestionBankDetailResponse;
import com.personal.model.response.QuestionDetailsData;
import com.personal.model.response.QuestionIdWithSequence;
import com.personal.model.response.ReferenceData;
import com.personal.service.QuestionBankService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "${api.v1.prefix}")
@Slf4j
@CrossOrigin
public class QuestionBankController {

    @Autowired
    private QuestionBankService questionBankService;

    @PostMapping("/createQuestionBank")
    public ResponseEntity<QuestionBankDetailResponse> createQuestionBank(
            @Valid @RequestBody CreateQuestionBankRequest createQuestionBankRequest) {
        log.info("Create Question Bank called for question_id: {}", createQuestionBankRequest.getQuestionId());
        QuestionBankDetailResponse response = questionBankService.createQuestionBank(createQuestionBankRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/getQuestionBank/{questionId}")
    public ResponseEntity<QuestionBankDetailResponse> getQuestionBankById(
            @PathVariable("questionId") UUID questionId) {
        log.info("Get Question Bank by ID called for questionId: {}", questionId);
        QuestionBankDetailResponse response = questionBankService.getQuestionBankById(questionId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getQuestionBankByS3Id/{questionIdS3}")
    public ResponseEntity<QuestionBankDetailResponse> getQuestionBankByQuestionIdS3(
            @PathVariable("questionIdS3") String questionIdS3) {
        log.info("Get Question Bank by S3 ID called for questionIdS3: {}", questionIdS3);
        QuestionBankDetailResponse response = questionBankService.getQuestionBankByQuestionIdS3(questionIdS3);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAllQuestionBanks")
    public ResponseEntity<Page<QuestionBankDetailResponse>> getAllQuestionBanks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Get All Question Banks called with page: {} and size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<QuestionBankDetailResponse> response = questionBankService.getAllQuestionBanks(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getQuestionBanksBySubject/{subject}")
    public ResponseEntity<Page<QuestionBankDetailResponse>> getQuestionBanksBySubject(
            @PathVariable("subject") String subject,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Get Question Banks by Subject called for subject: {} with page: {} and size: {}", subject, page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<QuestionBankDetailResponse> response = questionBankService.getQuestionBanksBySubject(subject, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getReferencesBySubject/{subject}")
    public ResponseEntity<ApiResponse<ReferenceData>> getReferencesBySubject(
            @PathVariable("subject") String subject) {
        log.info("Get References by Subject called for subject: {}", subject);
        List<String> references = questionBankService.getReferencesBySubject(subject);
        
        ReferenceData referenceData = ReferenceData.builder()
                .references(references)
                .build();
        
        ApiResponse<ReferenceData> response = ApiResponse.<ReferenceData>builder()
                .data(referenceData)
                .build();
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAllReferencesBySubjects")
    public ResponseEntity<ApiResponse<Map<String, List<String>>>> getAllReferencesBySubjects() {
        log.info("Get All References grouped by Subjects called");
        Map<String, List<String>> referencesBySubject = questionBankService.getAllReferencesBySubjects();
        
        ApiResponse<Map<String, List<String>>> response = ApiResponse.<Map<String, List<String>>>builder()
                .data(referencesBySubject)
                .build();
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getQuestionIdsBySubjectAndReference")
    public ResponseEntity<ApiResponse<IdsByReferenceData>> getQuestionIdsBySubjectAndReference(
            @RequestParam("subject") String subject,
            @RequestParam("reference") String reference) {
        log.info("Get Question IDs by Subject and Reference called for subject: {} and reference: {}", subject, reference);
        List<QuestionIdWithSequence> questionIds = questionBankService.getQuestionIdsBySubjectAndReference(subject, reference);
        
        IdsByReferenceData idsByReferenceData = IdsByReferenceData.builder()
                .idsByReference(questionIds)
                .build();
        
        ApiResponse<IdsByReferenceData> response = ApiResponse.<IdsByReferenceData>builder()
                .data(idsByReferenceData)
                .build();
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getQuestionDetails/{questionId}")
    public ResponseEntity<ApiResponse<QuestionDetailsData>> getQuestionDetails(
            @PathVariable("questionId") UUID questionId) {
        log.info("Get Question Details called for questionId: {}", questionId);
        QuestionBankDetailResponse questionDetails = questionBankService.getQuestionBankById(questionId);
        
        QuestionDetailsData questionDetailsData = QuestionDetailsData.builder()
                .questionDetails(questionDetails)
                .build();
        
        ApiResponse<QuestionDetailsData> response = ApiResponse.<QuestionDetailsData>builder()
                .data(questionDetailsData)
                .build();
        
        return ResponseEntity.ok(response);
    }
}
