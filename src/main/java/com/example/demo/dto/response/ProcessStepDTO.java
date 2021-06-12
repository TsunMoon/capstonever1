package com.example.demo.dto.response;


import com.example.demo.entity.Process;
import com.example.demo.entity.ServiceComponent;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProcessStepDTO {
    private Integer id;
    @JsonIgnore
    private Process process;
    private ServiceComponentDTO serviceComponentDTO;
    private Integer ordinal;

}
