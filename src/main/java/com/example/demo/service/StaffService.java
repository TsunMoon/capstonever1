package com.example.demo.service;

import com.example.demo.dto.response.StaffInfoDTO;
import com.example.demo.dto.response.StaffScheduleWorkTime;

import java.util.List;

public interface StaffService {

    // 1. lấy staff info theo staffId
    StaffInfoDTO getStaffInfoByStaffId(Integer staffId);

    // 2. Cập nhật thông tin staff info, chỉ cho cập nhật lại image và fullname
    String updateStaffInfoProfileImageAndFullname(Integer staffId, StaffInfoDTO dto);

    // 3. Lấy lịch làm việc của 1 nhân viên
    List<StaffScheduleWorkTime> getScheduleOfOneStaff(Integer staffId);

}
