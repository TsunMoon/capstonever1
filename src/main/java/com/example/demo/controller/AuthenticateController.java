package com.example.demo.controller;

import com.example.demo.dto.request.LoginReqDTO;
import com.example.demo.dto.response.LoginResDTO;
import com.example.demo.global.ConstantVariable;
import com.example.demo.service.AuthenticateService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = ConstantVariable.AUTHENTICATE)
@CrossOrigin
public class AuthenticateController {

    @Autowired
    AuthenticateService authenticateService;

    @PostMapping()
    @ApiOperation(value = "Xác thực danh tính bằng phoneNumber và password")
    public LoginResDTO authenticateAccount(@RequestBody LoginReqDTO loginReqDTO){
        return authenticateService.login(loginReqDTO);
    }

}
