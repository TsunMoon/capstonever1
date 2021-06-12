package com.example.demo.service;

import com.example.demo.dto.request.CustomerInfoDTOres;
import com.example.demo.dto.response.CustomerInfoDTO;
import com.example.demo.dto.response.BookingDTO;
import com.example.demo.dto.response.FollowProcess;
import com.example.demo.dto.response.ScheduleGoSpa;

import java.sql.Date;
import java.util.List;

public interface CustomerService {

    // 1. Api cập nhật thông tin profile
    String updateCustomerInfo(int customerId, CustomerInfoDTOres customerInfoDTOres);

    // 2. Api lấy detail của profile theo id
    CustomerInfoDTO getCustomerProfileById(int customerId);

    // 3. Api tạo ra request đặt lịch
    String createBookingRequest(Integer serviceId, Integer customerId, Integer slotId, Date day);

    // 4. Api lấy tất cả booking của 1 customer
    List<BookingDTO> getAllBookingOfOneCustomer(Integer customerId);

    // 5. Api theo dõi liệu trình của từng khách hàng
    FollowProcess getFollowProcessOfEachCustomer(Integer bookingId);

    // 6. Api hiện lịch tới spa theo ngày của từng khách hàng
    List<ScheduleGoSpa> getScheduleGoSpaOfOneCustomer(Integer customerId);

}
