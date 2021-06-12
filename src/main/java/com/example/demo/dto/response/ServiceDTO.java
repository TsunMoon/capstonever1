package com.example.demo.dto.response;

import com.example.demo.entity.Company;
import com.example.demo.entity.Type;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ServiceDTO {
    private int id;
    private String description;
    private String image;
    private boolean isCustomized;
    private String name;
    private Company company;
    private Type type;

}
