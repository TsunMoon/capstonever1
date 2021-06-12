package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity(name = "ServiceComponent")
@Table(name = "service_component")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ServiceComponent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name", nullable = false
//            , columnDefinition = "nvarchar(250)"
    )
    private String name;

    @Column(name = "description", nullable = false
//            , columnDefinition = "nvarchar(MAX)"
    )
    private String description;

    @Column(name = "image", nullable = false
//            , columnDefinition = "nvarchar(MAX)"
    )
    private String image;


    @Column(name = "createAt", nullable = false)
    private Date createAt;

    @Column(name = "isRemoved", nullable = false)
    private boolean isRemoved;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private Type type;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    @JsonIgnore
    private Company company;


    @JsonIgnore
    @OneToMany(mappedBy = "serviceComponent")
    private List<ServiceServiceComponent> listServiceServiceComponents;

    @JsonIgnore
    @OneToMany(mappedBy = "serviceComponent")
    private List<PriceByDate> listPriceByDates;

    @JsonIgnore
    @OneToMany(mappedBy = "serviceComponent")
    private List<ProcessStep> listProcessSteps;

}
