package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Entity(name = "BookingProcessStep")
@Table(name = "booking_process_step")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookingProcessStep implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "day")
    private Date day;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private StaffInfo staffInfo;

    @ManyToOne
    @JoinColumn(name = "process_step_id")
    private ProcessStep processStep;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "slot_id")
    private Slot slot;

}
