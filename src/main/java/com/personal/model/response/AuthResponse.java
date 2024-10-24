package com.personal.model.response;

import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    String token;
    Boolean valid;
    Boolean firstLogin;
    UUID customerId;
}
