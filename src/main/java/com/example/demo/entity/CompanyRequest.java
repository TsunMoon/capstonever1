package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity(name = "CompanyRequest")
@Table(name = "company_request")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CompanyRequest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "district", nullable = false)
    private String district;

    @Column(name = "province", nullable = false)
    private String province;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "create_at", nullable = false)
    private Date createAt;

    @Column(name = "email_code", nullable = false)
    private String emailCode;


    @Column(name = "email_expired_at", nullable = false)
    private Date emailExpiredAt;

    @Column(name = "phone_code")
    private String phoneCode;

    @Column(name = "phone_expired_at")
    private Date phoneExpiredAt;

    @JsonIgnore
    @OneToMany(mappedBy = "companyRequest")
    private List<CompanyRequestType> listCompanyRequestTypes;


}
