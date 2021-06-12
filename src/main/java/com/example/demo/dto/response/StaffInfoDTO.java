package com.example.demo.dto.response;

import com.example.demo.entity.Company;
import com.example.demo.entity.Gender;
import com.example.demo.entity.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StaffInfoDTO {
    private Integer staffId;
    private String district;
    private String fullname;
    private String image;
    private String province;
    private String street;
    private Company company;
    private Gender gender;
    private Type type;
    private String phoneNumber;
}
