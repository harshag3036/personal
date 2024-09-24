package com.personal.service;

import com.personal.model.request.CreateCustomerRequest;

public interface HomeService {
    void saveCustomerData(CreateCustomerRequest createCustomerRequest, String customerId);
}
