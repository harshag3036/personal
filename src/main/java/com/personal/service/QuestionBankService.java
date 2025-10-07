package com.personal.service;

import com.personal.model.request.CreateQuestionBankRequest;
import com.personal.model.response.QuestionBankDetailResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface QuestionBankService {

    QuestionBankDetailResponse createQuestionBank(CreateQuestionBankRequest createQuestionBankRequest);
    
    QuestionBankDetailResponse getQuestionBankById(UUID questionId);
    
    QuestionBankDetailResponse getQuestionBankByQuestionIdS3(String questionIdS3);
    
    Page<QuestionBankDetailResponse> getAllQuestionBanks(Pageable pageable);
    
    Page<QuestionBankDetailResponse> getQuestionBanksBySubject(String subject, Pageable pageable);
}
