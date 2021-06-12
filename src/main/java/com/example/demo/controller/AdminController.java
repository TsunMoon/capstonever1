package com.example.demo.controller;

import com.example.demo.dto.response.CompanyDTO;
import com.example.demo.dto.response.CompanyOutput;
import com.example.demo.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value = "/admin")
@RestController
@CrossOrigin
public class AdminController {

    @Autowired
    AdminService adminService;


    // 1. Lấy tất cả các company
    @GetMapping(value = "/get_all_company")
    public ResponseEntity<CompanyOutput> getAllCompany(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit,
            @RequestParam("keyword") String keyword,
            @RequestParam("sortBy") String sortBy
    ){
        CompanyOutput result = adminService.getAllCompany(page,limit, keyword, sortBy);
        if(result == null){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
