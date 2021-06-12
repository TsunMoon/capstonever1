package com.example.demo.service;


import com.example.demo.dto.response.CompanyDTO;
import com.example.demo.dto.response.CompanyOutput;

import java.util.List;

public interface AdminService {

    // 1. Lấy tất cả các company
    CompanyOutput getAllCompany (int page, int limit, String keyword, String sortBy);

}
