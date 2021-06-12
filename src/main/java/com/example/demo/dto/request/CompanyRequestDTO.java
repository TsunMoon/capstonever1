package com.example.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CompanyRequestDTO {

    private String name;
    private String street;
    private String district;
    private String province;
    private List<String> listType;
    private String location;
    private String email;
    private String phone;

}
