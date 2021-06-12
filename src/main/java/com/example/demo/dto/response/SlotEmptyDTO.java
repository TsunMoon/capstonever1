package com.example.demo.dto.response;

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
public class SlotEmptyDTO {
    private int slotId;
    private Date time;
    private Date day;
    private Integer customerId;
    private Integer serviceId;
    private Integer staffId;
    private String fullname;
}
