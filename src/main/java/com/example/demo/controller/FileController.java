package com.example.demo.controller;

import com.example.demo.dto.request.LoginReqDTO;
import com.example.demo.dto.response.LoginResDTO;
import com.example.demo.global.ConstantVariable;
import com.example.demo.service.AuthenticateService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@RequestMapping(value = "/file")
public class FileController {

    @PostMapping()
    public String uploadFile(@RequestBody MultipartFile file) {
        System.out.println(file.getSize());
        System.out.println(file.getResource().getFilename());
        return "good";
    }

}
