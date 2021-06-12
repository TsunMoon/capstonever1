package com.example.demo.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "CompanyType")
@Table(name = "company_type")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CompanyType  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private Type type;

}
