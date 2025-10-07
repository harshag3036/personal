package com.personal.model.response;

import com.personal.model.constant.TopicSubtopics;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionBankDetailResponse {

    private UUID questionId;
    private String questionIdS3;
    private String questionImage;
    private String questionString;
    private String solutionString;
    private Boolean includesDiagram;
    private String bloomsTagging;
    private Integer difficultyLevel;
    private String subject;
    private List<String> topics;
    private List<TopicSubtopics> subtopics;
    private String typeOfQuestion;
    private Double totalTime;
    private Double totalCost;
    private List<String> finalAnswer;
}
