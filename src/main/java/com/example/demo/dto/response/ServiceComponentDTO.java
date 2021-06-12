package com.example.demo.dto.response;

import com.example.demo.entity.Company;
import com.example.demo.entity.Type;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ServiceComponentDTO {
    private Integer id;
    private Date createAt;
    private String description;
    private String image;
    private boolean isRemoved;
    private String name;
    @JsonIgnore
    private Company company;
    private Type type;
    private float price;

}
