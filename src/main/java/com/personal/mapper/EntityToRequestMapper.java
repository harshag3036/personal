package com.personal.mapper;

import com.personal.model.db.CustomerEntity;
import com.personal.model.response.CustomerDetailResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EntityToRequestMapper {

    CustomerDetailResponse mapCustomerEntityToCustomerDetailResponse(CustomerEntity customerEntity);
}
