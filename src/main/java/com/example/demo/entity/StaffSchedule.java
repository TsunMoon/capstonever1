package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Entity(name = "StaffSchedule")
@Table(name = "staff_schedule")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StaffSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;


    @Column(name = "isOff", nullable = false)
    private boolean isOff;

    @Column(name = "reason_off")
    private String reasonOff;

    @Column(name = "day")
    private Date day;


    @ManyToOne
    @JoinColumn(name = "staff_id")
    private StaffInfo staffInfo;

    @ManyToOne
    @JoinColumn(name = "slot_id")
    private Slot slot;



}
