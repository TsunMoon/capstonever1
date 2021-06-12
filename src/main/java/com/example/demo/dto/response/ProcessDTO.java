package com.example.demo.dto.response;

import com.example.demo.entity.Service;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProcessDTO {
    private Integer id;
    private String description;
    private Service service;
}
