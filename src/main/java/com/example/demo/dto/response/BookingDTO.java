package com.example.demo.dto.response;

import com.example.demo.entity.*;
import com.example.demo.entity.Process;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookingDTO {
    private Integer bookingId;
    private CustomerInfo customerInfo;
    private Service service;
    private StaffInfo staffInfo;
    private Date createAt;
    private String status;
    private Slot slot;
    private Date day;
    private String phoneStaff;
}
