package com.example.demo.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "ServiceServiceComponent")
@Table(name = "service_service_component")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ServiceServiceComponent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "ordinal")
    private Integer ordinal;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @ManyToOne
    @JoinColumn(name = "service_component_id")
    private ServiceComponent serviceComponent;


}
