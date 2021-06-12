package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "CompanyRequestType")
@Table(name = "company_request_type")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CompanyRequestType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "company_request_id", nullable = false)
    private CompanyRequest companyRequest;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private Type type;

}
