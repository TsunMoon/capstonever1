package com.example.demo.dto.queryDTO;

import com.example.demo.entity.Company;
import com.example.demo.entity.CustomerInfo;
import com.example.demo.entity.Service;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.util.Date;
import java.util.Timer;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookingRequestQuery {
    private Integer bookingRequestId;
    private Service service;
    private CustomerInfo customerInfo;
    private Date createAt;
    private Date day;
    private Company company;
}
