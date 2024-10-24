package com.personal.mapper;

import com.personal.model.db.CommentsEntity;
import com.personal.model.db.CustomerEntity;
import com.personal.model.db.PostEntity;
import com.personal.model.request.CreateCommentRequest;
import com.personal.model.request.CreatePostRequest;
import com.personal.model.response.CustomerDetailResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EntityToRequestMapper {

    CustomerDetailResponse mapCustomerEntityToCustomerDetailResponse(CustomerEntity customerEntity);

    PostEntity mapCreatePostRequestToPostEntity(CreatePostRequest createPostRequest);

    CommentsEntity mapCreateCommentRequestToCommentEntity(CreateCommentRequest createCommentRequest);
}
