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
public class ServiceDTOEX {
    private String description;
    private String image;
    private String name;
    private int companyId;
    private int typeId;
    private List<Integer> listServiceStepId;
}
