package com.example.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ServiceComponentDTOresq {
    private String description;
    private String image;
    private String name;
    private Integer companyId;
    private String typeName;
    private float price;

}
