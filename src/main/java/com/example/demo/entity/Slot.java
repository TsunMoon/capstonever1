package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Time;
import java.util.List;

@Entity(name = "Slot")
@Table(name = "slot")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Slot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "time", nullable = false)
    private Time time;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    @JsonIgnore
    private Company company;


    @JsonIgnore
    @OneToMany(mappedBy = "slot")
    private List<BookingProcessStep> listBookingProcessSteps;

    @JsonIgnore
    @OneToMany(mappedBy = "slot")
    private List<StaffSchedule> listStaffSchedules;

    @JsonIgnore
    @OneToMany(mappedBy = "slot")
    private List<Booking> listBookings;


}
