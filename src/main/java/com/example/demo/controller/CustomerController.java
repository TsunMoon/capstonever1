package com.example.demo.controller;

import com.example.demo.dto.request.BookingRequestDTOreq;
import com.example.demo.dto.request.CustomerInfoDTOres;
import com.example.demo.dto.response.CustomerInfoDTO;
import com.example.demo.dto.response.BookingDTO;
import com.example.demo.dto.response.FollowProcess;
import com.example.demo.dto.response.ScheduleGoSpa;
import com.example.demo.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/customer")
@CrossOrigin
public class CustomerController {

    @Autowired
    CustomerService customerService;

    // 1. Api cập nhật thông tin profile
    @PutMapping(value = "/update_customer_info/{id}")
    public ResponseEntity<String> updateCustomerInfo(@PathVariable("id") int customerId, @RequestBody CustomerInfoDTOres customerInfoDTOres){
            String message = customerService.updateCustomerInfo(customerId, customerInfoDTOres);
            if(message == "SUCCESS"){
                return new ResponseEntity<>("Thành công cập nhật thông tin khách hàng", HttpStatus.OK);
            }
            return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 2. Api lấy detail của profile theo id
    @GetMapping(value = "/get_customer_profile_by_id/{id}")
    public ResponseEntity<CustomerInfoDTO> getCustomerProfileById(@PathVariable("id") int customerId){
        CustomerInfoDTO result = customerService.getCustomerProfileById(customerId);
        if(result == null){
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    // 3. Api tạo ra request đặt lịch
    @PostMapping(value = "/create_booking_request")
    public ResponseEntity<String> createBookingRequest(@RequestBody BookingRequestDTOreq dto){
        String result = customerService.createBookingRequest(dto.getServiceId(), dto.getCustomerId(), dto.getSlotId(), dto.getDay());
        if(result == null){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(result.equals("SUCCESS")){
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(result, HttpStatus.CONFLICT);
    }

    // 4. Api lấy tất cả booking của 1 customer
    @GetMapping(value = "/get_all_booking_of_one_customer")
    public List<BookingDTO> getAllBookingOfOneCustomer(@RequestParam("customerId") Integer customerId){
        return customerService.getAllBookingOfOneCustomer(customerId);
    }

    // 5. Api theo dõi liệu trình của từng khách hàng
    @GetMapping(value = "/follow_process_of_each_customer")
    public FollowProcess followProcessOfEachCustomer(@RequestParam("bookingId") Integer bookingId){
       return  customerService.getFollowProcessOfEachCustomer(bookingId);
    }


    // 6. Api hiện lịch tới spa theo ngày của từng khách hàng
    @GetMapping(value = "/get_schedule_go_spa_of_one_customer")
    public List<ScheduleGoSpa> getScheduleGoSpaOfOneCustomer(@RequestParam("customerId") Integer customerId){
        return customerService.getScheduleGoSpaOfOneCustomer(customerId);
    }


}
