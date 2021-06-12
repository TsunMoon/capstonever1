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
public class ServiceStepOutput {
    private int page;
    private int totalPage;
    private int totalItem;
    private List<ServiceComponentDTO> listServiceComponentDTOS;

}
