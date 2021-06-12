package com.example.demo.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "staff_info")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StaffInfo implements Serializable {

    @Id
    @Column(name = "staff_id")
    private int staffId;

    @JsonIgnore
    @OneToOne
    @MapsId
    @JoinColumn(name = "staff_id")
    private Account account;


    @Column(name = "fullname", nullable = false
//            , columnDefinition = "nvarchar(MAX)"
    )
    private String fullname;



    @Column(name = "image", nullable = false
//            , columnDefinition = "nvarchar(MAX)"
    )
    private String image;

    @Column(name = "street", nullable = false
//            , columnDefinition = "nvarchar(500)"
    )
    private String street;

    @Column(name = "district", nullable = false
//            , columnDefinition = "nvarchar(500)"
    )
    private String district;

    @Column(name = "province", nullable = false
//            , columnDefinition = "nvarchar(500)"
    )
    private String province;


    @ManyToOne
    @JoinColumn(name = "gender_id", nullable = false)
    private Gender gender;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    @JsonIgnore
    private Company company;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private Type type;


    @OneToMany(mappedBy = "staffInfo")
    @JsonIgnore
    private List<Order> listOrders;

    @JsonIgnore
    @OneToMany(mappedBy = "staffInfo")
    private List<Booking> listBookings;

    @JsonIgnore
    @OneToMany(mappedBy = "staffInfo")
    private List<StaffSchedule> listStaffSchedules;

    @JsonIgnore
    @OneToMany(mappedBy = "staffInfo")
    private List<BookingProcessStep> listBookingProcessSteps;

}
