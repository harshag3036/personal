package com.personal.controller;

import com.personal.model.request.CreateQuestionBankRequest;
import com.personal.model.response.QuestionBankDetailResponse;
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
}
