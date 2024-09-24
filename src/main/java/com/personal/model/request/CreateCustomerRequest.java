package com.personal.model.request;

import com.personal.model.constant.Gender;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomerRequest {
    private String name;
    private LocalDate dob;
    private Gender gender;
}
