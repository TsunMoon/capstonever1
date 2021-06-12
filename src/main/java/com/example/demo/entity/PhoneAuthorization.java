package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity(name = "PhoneAuthorization")
@Table(name = "phone_authorization")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PhoneAuthorization {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "create_at", nullable = false)
    private Timestamp createAt;

    @Column(name = "expired_at", nullable = false)
    private Timestamp expiredAt;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "fullname", nullable = false)
    private String fullname;


}
