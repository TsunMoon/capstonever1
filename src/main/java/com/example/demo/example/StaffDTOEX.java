package com.example.demo.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StaffDTOEX {
    private String password;
    private String phone;
    private int roleId;
    private String district;
    private String fullname;
    private String province;
    private String street;


}
