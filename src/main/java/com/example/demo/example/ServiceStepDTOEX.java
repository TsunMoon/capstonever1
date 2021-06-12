package com.example.demo.example;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ServiceStepDTOEX {
    private String name;
    private String description;
    private String image;
    private Date createAt;
    private float price;
    private int typeId;
    private int companyId;
}
