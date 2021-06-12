package com.example.demo.entity;

import lombok.*;


import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "account")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private CustomerInfo customerInfo;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private StaffInfo staffInfo;


    @Column(
            name = "phone",
            unique = true, columnDefinition = "varchar(20)", nullable = false
    )
    private String phone;


    @Column(name = "password", nullable = false
//            , columnDefinition = "varchar(MAX)"
    )
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

}
