package com.example.demo.dto.response;

import com.example.demo.dto.response.GenderDTO;
import com.example.demo.entity.Gender;
import com.example.demo.example.GenderDTOEX;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerInfoDTO {
    private int customerId;
    private Date dateOfBirth;
    private String district;
    private String fullname;
    private String image;
    private String lastLocation;
    private String province;
    private String street;
    private Gender gender;

}
