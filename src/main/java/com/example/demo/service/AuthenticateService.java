package com.example.demo.service;

import com.example.demo.dto.request.LoginReqDTO;
import com.example.demo.dto.response.LoginResDTO;

public interface AuthenticateService {
    LoginResDTO login (LoginReqDTO requestLogin);
}