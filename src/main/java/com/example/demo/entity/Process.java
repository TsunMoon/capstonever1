package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity(name = "Process")
@Table(name = "process")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Process implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "service_id")
    @JsonIgnore
    private Service service;

    @JsonIgnore
    @OneToMany(mappedBy = "process")
    private List<ProcessStep> listProcessSteps;


    @JsonIgnore
    @OneToMany(mappedBy = "process")
    private List<Booking> listBookings;

}
