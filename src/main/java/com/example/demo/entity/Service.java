package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity(name = "Service")
@Table(name = "service")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Service implements Serializable {

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

    @Column(name = "isCustomized", nullable = false)
    private boolean isCustomized;

    @Column(name = "isRemoved", nullable = false)
    private boolean isRemoved;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private Type type;

    @JsonIgnore
    @OneToMany(mappedBy = "service")
    private List<ServiceServiceComponent> listServiceServiceComponents;

    @JsonIgnore
    @OneToMany(mappedBy = "service")
    private List<Booking> listBookings;

    @JsonIgnore
    @OneToMany(mappedBy = "service")
    private List<Process> listProcesses;


}
