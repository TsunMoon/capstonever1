package com.example.demo.service.impl;

import com.example.demo.dto.request.LoginReqDTO;
import com.example.demo.dto.response.LoginResDTO;
import com.example.demo.entity.Account;
import com.example.demo.jwt.JWTUtils;
import com.example.demo.repository.AccountRepository;
import com.example.demo.service.AuthenticateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateServiceImpl implements AuthenticateService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    JWTUtils jwtUtils;

    @Override
    public LoginResDTO login (LoginReqDTO requestLogin){
        Account newAccount = accountRepository.findAccountByPhone(requestLogin.getPhoneNumber());

        if(newAccount == null){
          return LoginResDTO.createErrorResponse(LoginResDTO.Error.USERNAME_NOT_FOUND);
        }

        if(!newAccount.getPassword().equals(requestLogin.getPassword())){
          return LoginResDTO.createErrorResponse(LoginResDTO.Error.WRONG_PASSWORD);
        }

        String role = newAccount.getRole().getName();
        String token = jwtUtils.generateToken(newAccount.getPhone());
        int idAccount = newAccount.getId();

        return LoginResDTO.createSuccessResponse(token,role,idAccount);
    }
}
