package com.example.demo.dto.response;

import com.example.demo.dto.response.TypeDTO;
import com.example.demo.entity.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CompanyDTO {
    private int id;
    private Date createAt;
    private String district;
    private String email;
    private String location;
    private String name;
    private String image;
    private String province;
    private String street;
    private Time workStart;
    private Time workEnd;
    private Time breakStart;
    private Time breakEnd;
    private List<Type> listTypes;


}
