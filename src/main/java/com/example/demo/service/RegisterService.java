package com.example.demo.service;

import com.example.demo.dto.request.RegisterAccountDTO;
import com.example.demo.dto.request.VerificationCodeDTO;

public interface RegisterService {
    String createPhoneAuthorizationCode(RegisterAccountDTO registerAccountDTO);

    String verificationCode(VerificationCodeDTO verificationCodeDTO);
}
