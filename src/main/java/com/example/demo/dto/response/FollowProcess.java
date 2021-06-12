package com.example.demo.dto.response;

import com.example.demo.entity.BookingProcessStep;
import com.example.demo.entity.Process;
import com.example.demo.entity.Service;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FollowProcess {
    private Integer bookingId;
    private Service service;
    private Process process;
    private List<BookingProcessStepDTO> listBookingProcessStepsDTO;
}
