package com.example.demo.controller;

import com.example.demo.dto.response.StaffInfoDTO;
import com.example.demo.dto.response.StaffScheduleWorkTime;
import com.example.demo.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value = "/staff")
@RestController
@CrossOrigin
public class StaffController {

    @Autowired
    StaffService staffService;

    // 1. lấy staff info theo staffId
    @GetMapping(value = "/get_staff_info_by_staff_id/{id}")
    public StaffInfoDTO getStaffInfoByStaffId(@PathVariable("id") Integer staffId){
        return staffService.getStaffInfoByStaffId(staffId);
    }

    // 2. Cập nhật thông tin staff info, chỉ cho cập nhật lại image và fullname
//    @PutMapping(value = "/update_staff_info_profile/[id]")
//    public String updateStaffInfoProfileImageAndFullname(@RequestParam("id")Integer staffId,
//                                                         @RequestBody StaffInfoDTO dto,
//
//                                                         ){
//
//    }

    // 3. Lấy lịch làm việc của 1 nhân viên
    @GetMapping(value = "/get_schedule_of_one_staff/{id}")
    public List<StaffScheduleWorkTime> getScheduleOfOneStaff(@PathVariable("id") Integer staffId){
        return staffService.getScheduleOfOneStaff(staffId);
    }


}
