package com.example.demo.dto.response;

import com.example.demo.entity.*;
import com.example.demo.entity.Process;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StaffScheduleForSlot {
    private Time slotTime;
    private Slot slot;
    private Integer bookingId;
    private Service service;
    private Process process;
    private ProcessStep processStep;
    private CustomerInfo customerInfo;
    private String customerPhone;


}
