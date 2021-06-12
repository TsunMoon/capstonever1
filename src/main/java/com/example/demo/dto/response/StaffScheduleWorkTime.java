package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StaffScheduleWorkTime {
    private Date day;
    private List<StaffScheduleForSlot> listStaffScheduleForSlots;
}
