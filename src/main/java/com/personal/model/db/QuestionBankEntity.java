package com.personal.model.db;

import com.personal.model.constant.TopicSubtopics;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "question_bank", indexes = {
    @Index(name = "idx_subject", columnList = "subject")
})
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionBankEntity extends AuditModel {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "question_id")
    private UUID questionId;

    @Column(name = "question_id_s3")
    private String questionIdS3;

    @Column(name = "question_image")
    private String questionImage;

    @Type(value = JsonBinaryType.class)
    @Column(name = "question_string", columnDefinition = "json")
    private String questionString;

    @Type(value = JsonBinaryType.class)
    @Column(name = "solution_string", columnDefinition = "json")
    private String solutionString;

    @Column(name = "includes_diagram")
    private Boolean includesDiagram;

    @Column(name = "blooms_tagging")
    private String bloomsTagging;

    @Column(name = "difficulty_level")
    private Integer difficultyLevel;

    @Column(name = "subject")
    private String subject;

    @Type(value = JsonBinaryType.class)
    @Column(columnDefinition = "json")
    private List<String> topics;

    @Type(value = JsonBinaryType.class)
    @Column(name = "sub_topics", columnDefinition = "json")
    private List<TopicSubtopics> subtopics;

    @Column(name = "type_of_question")
    private String typeOfQuestion;

    @Column(name = "total_time")
    private Double totalTime;

    @Column(name = "total_cost")
    private Double totalCost;

    @Type(value = JsonBinaryType.class)
    @Column(name = "final_answer", columnDefinition = "json")
    private List<String> finalAnswer;
}
