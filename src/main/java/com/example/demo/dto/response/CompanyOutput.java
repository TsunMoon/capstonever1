package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CompanyOutput {
    private int page;
    private int totalPage;
    private int totalItem;
    private List<CompanyDTO> listCompanyDTOS;
}
