package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import java.util.List;

@Entity(name = "Company")
@Table(name = "company")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Company implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name", nullable = false
//            , columnDefinition = "nvarchar(250)"
    )
    private String name;

    @Column(name = "image", nullable = false)
    private String image;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "location", nullable = false
//            , columnDefinition = "nvarchar(MAX)"
    )
    private String location;

    @Column(name = "street", nullable = false
//            , columnDefinition = "nvarchar(500)"
    )
    private String street;

    @Column(name = "district", nullable = false
//            , columnDefinition = "nvarchar(500)"
    )
    private String district;

    @Column(name = "province", nullable = false
//            , columnDefinition = "nvarchar(500)"
    )
    private String province;

    @Column(name = "create_at", nullable = false)
    private Date createAt;

    @Column(name = "work_start", nullable = false)
    private Time workStart;

    @Column(name = "work_end", nullable = false)
    private Time workEnd;

    @Column(name = "break_start", nullable = false)
    private Time breakStart;

    @Column(name = "break_end", nullable = false)
    private Time breakEnd;


    @OneToMany(mappedBy = "company")
    @JsonIgnore
    List<CompanyType> listCompanyTypes;

    @JsonIgnore
    @OneToMany(mappedBy = "company")
    List<Order> listOrders;

    @JsonIgnore
    @OneToMany(mappedBy = "company")
    List<Service> listServices;

    @JsonIgnore
    @OneToMany(mappedBy = "company")
    List<StaffInfo> listStaffInfos;

    @JsonIgnore
    @OneToMany(mappedBy = "company")
    List<ServiceComponent> listServiceComponents;

    @JsonIgnore
    @OneToMany(mappedBy = "company")
    List<Slot> listSlots;

}
