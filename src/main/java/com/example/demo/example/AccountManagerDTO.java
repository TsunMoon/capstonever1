package com.example.demo.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountManagerDTO {
    private String password;
    private String phone;
    private String fullname;
    private String image;
    private String street;
    private String district;
    private String province;
    private int companyId;
    private int genderId;

}
