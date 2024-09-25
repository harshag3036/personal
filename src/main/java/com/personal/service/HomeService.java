package com.personal.service;

import com.personal.model.request.CreateCustomerRequest;
import com.personal.model.response.CustomerDetailResponse;

public interface HomeService {
    void saveCustomerData(CreateCustomerRequest createCustomerRequest, String customerId);

    CustomerDetailResponse getCustomerData(String customerId);
}
