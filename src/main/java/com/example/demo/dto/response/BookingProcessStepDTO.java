package com.example.demo.dto.response;

import com.example.demo.entity.Booking;
import com.example.demo.entity.ProcessStep;
import com.example.demo.entity.Slot;
import com.example.demo.entity.StaffInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookingProcessStepDTO {
    private Integer id;
    private ProcessStepDTO processStepDTO;
    @JsonIgnore
    private Booking booking;
    private Slot slot;
    private Date day;
    private StaffInfo staffInfo;
    private String status;
}
