package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity(name = "Type")
@Table(name = "type")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Type implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name", nullable = false, unique = true
//            , columnDefinition = "nvarchar(MAX)"
    )
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "type")
    private List<CompanyType> listCompanyTypes;

    @JsonIgnore
    @OneToMany(mappedBy = "type")
    private List<Service> listServices;

    @JsonIgnore
    @OneToMany(mappedBy = "type")
    private List<CompanyRequestType> listCompanyRequestTypes;

    @JsonIgnore
    @OneToMany(mappedBy = "type")
    private List<ServiceComponent> listServiceComponents;

    @JsonIgnore
    @OneToMany(mappedBy = "type")
    private List<StaffInfo> listStaffInfos;

}
