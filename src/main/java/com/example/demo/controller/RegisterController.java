package com.example.demo.controller;

import com.example.demo.dto.request.RegisterAccountDTO;
import com.example.demo.dto.request.VerificationCodeDTO;
import com.example.demo.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/register")
@CrossOrigin
public class RegisterController {

    @Autowired
    RegisterService registerService;

    @PostMapping(value = "/register_account")
    public ResponseEntity<String> createRegisterAccountCode(@RequestBody RegisterAccountDTO registerAccountDTO){
        try {
            String code = registerService.createPhoneAuthorizationCode(registerAccountDTO);
            if(code.equals("") || code == null){
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if(code.trim().equals("Duplicate Account")){
                return  new ResponseEntity<>(code, HttpStatus.CONFLICT);
            }
            return  new ResponseEntity<>(code, HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/verification_code")
    public ResponseEntity<String> verificationCode(@RequestBody VerificationCodeDTO verificationCodeDTO) {
        try {
            String result = registerService.verificationCode(verificationCodeDTO);
            if (result.equals("") || result == null || !result.equals("SUCCESS")) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>("Thành công rồi đó", HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
