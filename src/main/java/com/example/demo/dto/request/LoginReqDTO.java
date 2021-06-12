package com.example.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LoginReqDTO {
    private String phoneNumber;
    private String password;

}
