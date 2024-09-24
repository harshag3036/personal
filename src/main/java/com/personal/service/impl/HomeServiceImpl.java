package com.personal.service.impl;

import com.personal.model.db.CustomerEntity;
import com.personal.model.request.CreateCustomerRequest;
import com.personal.repository.CustomerRepository;
import com.personal.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class HomeServiceImpl implements HomeService {

    @Autowired
    CustomerRepository customerRepository;
    @Override
    public void saveCustomerData(CreateCustomerRequest createCustomerRequest, String customerId) {
        final CustomerEntity customerEntity = CustomerEntity.builder()
                .dob(createCustomerRequest.getDob())
                .customerId(UUID.fromString(customerId))
                .name(createCustomerRequest.getName())
                .gender(createCustomerRequest.getGender()).build();
        customerRepository.save(customerEntity);
    }
}
