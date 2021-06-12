package com.example.demo.dto.response;

import com.example.demo.entity.CustomerInfo;
import com.example.demo.entity.Process;
import com.example.demo.entity.Service;
import com.example.demo.entity.Slot;
import com.example.demo.entity.StaffInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookingRequestDTO {
    private Integer id;
    private CustomerInfo customerInfo;
    private Service service;
    private StaffInfo staffInfo;
    private Date createAt;
    private String status;
    private Process process;
    private Slot slot;
    private Date day;
    private String staffPhone;
    private String customerPhone;

}
