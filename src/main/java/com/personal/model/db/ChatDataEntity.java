package com.personal.model.db;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "chat_data")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatDataEntity extends AuditModel{
    @Id
    @Column(name = "data_id")
    @GeneratedValue(generator = "UUID")
    private UUID dataId;

    private String keywords;

    private String response;
}
