package com.personal.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.personal.model.constant.TopicSubtopics;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateQuestionBankRequest {
    
    @JsonProperty("full_problem")
    @NotNull
    private Object fullProblem;
    
    @JsonProperty("full_solution")
    @NotNull
    private FullSolution fullSolution;
    
    @JsonProperty("has_diagram")
    @NotNull
    private Boolean hasDiagram;
    
    @JsonProperty("metadata")
    @NotNull
    private Metadata metadata;
    
    @JsonProperty("question_id")
    @NotBlank
    private String questionId;
    
    @JsonProperty("question_image")
    private String questionImage;
    
    @JsonProperty("processing_info")
    private ProcessingInfo processingInfo;
    
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FullSolution {
        
        @JsonProperty("final_answer")
        private List<String> finalAnswer;
        
        @JsonProperty("solution")
        private Object solution;
    }
    
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProcessingInfo {
        
        @JsonProperty("total_time_seconds")
        private Double totalTimeSeconds;
        
        @JsonProperty("estimated_cost_usd")
        private Double estimatedCostUsd;
    }
    
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Metadata {
        
        @JsonProperty("blooms_tagging")
        private String bloomsTagging;
        
        @JsonProperty("difficulty_level")
        private Integer difficultyLevel;
        
        @JsonProperty("subject")
        private String subject;
        
        @JsonProperty("subtopics")
        private List<TopicSubtopics> subtopics;
        
        @JsonProperty("topics")
        private List<String> topics;
        
        @JsonProperty("type_of_question")
        private String typeOfQuestion;
    }
}
