package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "gender")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Gender implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;


    @Column(name = "name", nullable = false, unique = true
//            , columnDefinition = "nvarchar(50)"
    )
    private String name;


    @OneToMany(mappedBy = "gender")
    @JsonIgnore
    private List<CustomerInfo> listCustomerInfos;

    @OneToMany(mappedBy = "gender")
    @JsonIgnore
    private List<StaffInfo> staffInfos;



}
