package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ServiceOutput {
    private Integer page;
    private Integer totalPage;
    private Integer totalItem;
    private List<ServiceDTO> listServiceDTOS;
}
