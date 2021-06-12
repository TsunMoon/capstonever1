package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StaffScheduleDTO {
    private Integer id;
    private Integer staffId;
    private boolean isOff;
    private String reasonOff;
    private Integer slotDetailId;
}
