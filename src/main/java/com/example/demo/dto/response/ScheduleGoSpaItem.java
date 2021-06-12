package com.example.demo.dto.response;

import com.example.demo.entity.Booking;
import com.example.demo.entity.Company;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class ScheduleGoSpaItem {
    private Time time;
    private Company company;
    private Booking booking;

}
