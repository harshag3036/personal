package com.personal.repository;

import com.personal.model.db.ChatDataEntity;
import com.personal.model.db.CommentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ChatDataRepository extends JpaRepository<ChatDataEntity, UUID> {

    @Query(
            nativeQuery = true,
            value = "WITH input_words AS (SELECT unnest(regexp_split_to_array(:message, '\\s+')) AS word) SELECT DISTINCT response FROM chat_data WHERE EXISTS (SELECT 1 FROM input_words WHERE LOWER(chat_data.keywords) = LOWER(word));"
    )
    List<String> getResponseFromWords(@Param("message") String message);

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM chat_data WHERE LOWER(keywords) LIKE LOWER(CONCAT('%', :keyword, '%'));"
    )
    List<ChatDataEntity> getChatEntityFromKeywords(@Param("keyword") String keyword);

//    @Query(
//            nativeQuery = true,
//            value = "SELECT * \n" +
//                    "FROM chat_data \n" +
//                    "WHERE to_tsvector('english', keywords) @@ plainto_tsquery('english', :keyword);"
//    )
//    List<ChatDataEntity> getChatEntityFromKeywords(@Param("keyword") String keyword);
}
