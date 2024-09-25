package com.personal.model.response;

import com.personal.model.constant.Gender;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDetailResponse {
    private String name;
    private LocalDate dob;
    private Gender gender;
}
