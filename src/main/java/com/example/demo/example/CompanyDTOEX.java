package com.example.demo.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CompanyDTOEX {
    private String district;
    private String location;
    private String name;
    private String email;
    private String province;
    private String street;
    private List<Integer> listTypeId;

}
