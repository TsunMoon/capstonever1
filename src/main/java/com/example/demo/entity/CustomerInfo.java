package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity(name = "CustomerInfo")
@Table(name = "customer_info")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerInfo  implements Serializable {

    @Id
    @Column(name = "id")
    private int id;

    @JsonIgnore
    @OneToOne
    @MapsId
    @JoinColumn(name = "customer_id")
    private Account account;

    @Column(name = "fullname"
//            , columnDefinition = "nvarchar(MAX)"
    )
    private String fullname;


    @ManyToOne
    @JoinColumn(name = "gender_id")
    private Gender gender;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "street"
//            , columnDefinition = "nvarchar(500)"

    )
    private String street;

    @Column(name = "district"
//            , columnDefinition = "nvarchar(500)"
    )
    private String district;

    @Column(name = "province"
//            , columnDefinition = "nvarchar(500)"
    )
    private String province;

    @Column(name = "last_location"
//            , columnDefinition = "nvarchar(MAX)"
    )
    private String lastLocation;

    @Column(name = "image")
    private String image;


    @JsonIgnore
    @OneToMany(mappedBy = "customerInfo")
    private List<Order> listOrders;

    @JsonIgnore
    @OneToMany(mappedBy = "customerInfo")
    private List<Booking> listBookings;


}
