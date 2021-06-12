package com.example.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateDefaultServiceDTOreq {
    private String description;
    private String image;
    private String name;
    private Integer companyId;
    private String typeName;
    private List<OrdinalServiceComponent> listOrdinalServiceComponents;
}
