package com.example.demo.dto.response;

import com.example.demo.entity.Company;
import com.example.demo.entity.ServiceServiceComponent;
import com.example.demo.entity.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DefaultServiceDTO {
    private Integer id;
    private String description;
    private String image;
    private boolean isCustomized;
    private String name;
    private Company company;
    private Type type;
    private List<ServiceServiceComponent> listServiceServiceComponents;
}
