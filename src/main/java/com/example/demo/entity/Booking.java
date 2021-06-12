package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity(name = "Booking")
@Table(name = "booking")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "createAt", nullable = false)
    private Date createAt;

    @Column(name = "status")
    private String status;

    @Column(name = "day")
    private java.sql.Date day;


    @ManyToOne
    @JoinColumn(name = "slot_id")
    private Slot slot;


    @ManyToOne
    @JoinColumn(name = "staff_id")
    private StaffInfo staffInfo;


    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerInfo customerInfo;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @ManyToOne
    @JoinColumn(name = "process_id")
    private Process process;

    @JsonIgnore
    @OneToMany(mappedBy = "booking")
    private List<BookingProcessStep> listBookingProcessSteps;

}
