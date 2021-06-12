package com.example.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerInfoDTOres {

    private Date dateOfBirth;
    private String district;
    private String fullname;
    private String image;
    private String last_location;
    private String province;
    private String street;
    private String gender;
}
