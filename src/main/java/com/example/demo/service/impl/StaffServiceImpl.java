package com.example.demo.service.impl;

import com.example.demo.dto.response.StaffInfoDTO;
import com.example.demo.dto.response.StaffScheduleForSlot;
import com.example.demo.dto.response.StaffScheduleWorkTime;
import com.example.demo.entity.*;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.SlotRepository;
import com.example.demo.repository.StaffInfoRepository;
import com.example.demo.repository.StaffScheduleRepository;
import com.example.demo.service.StaffService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StaffServiceImpl implements StaffService {

    @Autowired
    StaffInfoRepository staffInfoRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    StaffScheduleRepository staffScheduleRepository;

    @Autowired
    SlotRepository slotRepository;


    // 1. lấy staff info theo staffId
    @Override
    public StaffInfoDTO getStaffInfoByStaffId(Integer staffId) {

        // Kiểm tra xem account đó phải là role staff ko
        Optional<Account> accountOptional = accountRepository.findById(staffId);
        if (accountOptional.isEmpty()) {
            throw new RuntimeException("Account by staffId is not available");
        }
        Account account = accountOptional.get();

        if (!account.getRole().getName().equalsIgnoreCase("Staff")) {
            throw new RuntimeException("This id is not a staff");
        }


        StaffInfo staffInfo = staffInfoRepository.findByStaffId(staffId);
        if (staffInfo == null) {
            throw new RuntimeException("StaffInfo by staffId is not available");
        }

        StaffInfoDTO dto = new StaffInfoDTO();
        BeanUtils.copyProperties(staffInfo, dto);
        dto.setPhoneNumber(account.getPhone());

        return dto;
    }


    // 2. Cập nhật thông tin staff info, chỉ cho cập nhật lại image và fullname
    @Override
    public String updateStaffInfoProfileImageAndFullname(Integer staffId, StaffInfoDTO dto) {
        return null;
    }



    // 3. Lấy lịch làm việc của 1 nhân viên
    @Override
    public List<StaffScheduleWorkTime> getScheduleOfOneStaff(Integer staffId) {

        // 1. Lấy ra staffInfo theo staffId
        StaffInfo staffInfo = staffInfoRepository.findByStaffId(staffId);
        if(staffInfo == null){
            throw new RuntimeException("StaffInfo by StaffId is not available!!!");
        }

        // 2. Lấy ra list staff_schedule theo staffId
        List<StaffSchedule> listStaffSchedules = staffInfo.getListStaffSchedules();
        if(listStaffSchedules.isEmpty()){
            throw new RuntimeException("This Staff not have any staff schedule!!!");
        }

        // 3. Sort list staff_schedule theo day
        Comparator<StaffSchedule> compareByDay = (StaffSchedule ss1, StaffSchedule ss2) -> ss1.getDay().compareTo(ss2.getDay());
        Collections.sort(listStaffSchedules, compareByDay);

        // 4. Tạo ra list result
        List<StaffScheduleWorkTime> listResult = new ArrayList<>();

        // 5. Dùng staffiD => companyId => list<Slot> theo companyId
        List<Slot> listSlotByCompany = staffInfo.getCompany().getListSlots();
        if(listSlotByCompany.isEmpty()){
            throw new RuntimeException("This Company doesn't have any slot!!!");
        }

        // 6. Sort lại list slot cho đúng theo thứ tự từ nhỏ tới lớn
        Comparator<Slot> compareSlotByTime = (Slot s1, Slot s2) -> s1.getTime().compareTo(s2.getTime());
        Collections.sort(listSlotByCompany, compareSlotByTime);

        // 7. Lấy ra list booking theo staffId
        List<Booking> listBookings = staffInfo.getListBookings();

        // 8. Lấy ra list booking_process_step
        List<BookingProcessStep> listProcessStep = staffInfo.getListBookingProcessSteps();

        // 9. Tạo ra khuôn theo ngày trước
        for(StaffSchedule eachSchedule : listStaffSchedules){
            Boolean flagDay = false;

            // Kiểm tra xem ngày đã có trong list result chưa
            for(StaffScheduleWorkTime eachWorkTime : listResult){
                if(eachSchedule.getDay().compareTo(eachWorkTime.getDay()) == 0){
                    flagDay = true;
                    StaffScheduleForSlot oneSlot = new StaffScheduleForSlot();
                    oneSlot.setSlotTime(eachSchedule.getSlot().getTime());
                    oneSlot.setSlot(eachSchedule.getSlot());

                    Boolean flagBooking = false;
                    // Nếu list booking có
                    if(listBookings != null){

                        // Lấy booking có slotTime và day trùng với lịch làm việc
                        for(Booking eachBooking : listBookings){
                            if(eachBooking.getDay().compareTo(eachSchedule.getDay()) == 0){
                                if(eachBooking.getSlot().getTime().compareTo(eachSchedule.getSlot().getTime()) == 0){

                                    flagBooking = true;

                                    oneSlot.setBookingId(eachBooking.getId());
                                    oneSlot.setService(eachBooking.getService());
                                    oneSlot.setCustomerInfo(eachBooking.getCustomerInfo());
                                    oneSlot.setCustomerPhone(eachBooking.getCustomerInfo().getAccount().getPhone());
                                    // Kiểm tra xem booking đó có service là Default hay Customized
                                    if(eachBooking.getService().isCustomized()){
                                        oneSlot.setProcess(eachBooking.getProcess());

                                        // set là null vì ngày này chỉ lên để khám thôi
                                        oneSlot.setProcessStep(null);
                                    }else{
                                        // Là default service
                                        oneSlot.setProcess(null);
                                        oneSlot.setProcessStep(null);
                                    }


                                }
                            }
                        }

                    }
                    // Nếu như không có booking thì qua kiềm tra bên booking_process_step
                    if(!flagBooking){
                        // Nếu List booking_process_step tồn tại
                        if(listProcessStep != null){
                            for(BookingProcessStep eachStep : listProcessStep){
                                if(eachStep.getDay() != null && eachStep.getSlot() != null){
                                    if(eachStep.getDay().compareTo(eachSchedule.getDay()) == 0){
                                        if(eachStep.getSlot().getTime().compareTo(eachSchedule.getSlot().getTime()) == 0){

                                            oneSlot.setBookingId(eachStep.getBooking().getId());
                                            oneSlot.setService(eachStep.getBooking().getService());
                                            oneSlot.setCustomerInfo(eachStep.getBooking().getCustomerInfo());
                                            oneSlot.setCustomerPhone(eachStep.getBooking().getCustomerInfo().getAccount().getPhone());
                                            oneSlot.setProcess(eachStep.getProcessStep().getProcess());
                                            oneSlot.setProcessStep(eachStep.getProcessStep());
                                        }
                                    }
                                }
                            }
                        }

                    }


                    // Thêm vào listStaffScheduleForSlots
                    eachWorkTime.getListStaffScheduleForSlots().add(oneSlot);
                }
            }

            // ===============================
            // Kiểm tra ngày trong list result
            if(!flagDay){
                StaffScheduleWorkTime result = new StaffScheduleWorkTime();
                result.setDay(eachSchedule.getDay());

                List<StaffScheduleForSlot> listItemOfResult = new ArrayList<>();
                StaffScheduleForSlot oneSlot = new StaffScheduleForSlot();
                oneSlot.setSlotTime(eachSchedule.getSlot().getTime());
                oneSlot.setSlot(eachSchedule.getSlot());

                Boolean flagBooking = false;
                // Nếu list booking có
                if(listBookings != null){
                    // Lấy booking có slotTime và day trùng với lịch làm việc
                    for(Booking eachBooking : listBookings){
                        if(eachBooking.getDay().compareTo(eachSchedule.getDay()) == 0){
                            if(eachBooking.getSlot().getTime().compareTo(eachSchedule.getSlot().getTime()) == 0){

                                flagBooking = true;
                                oneSlot.setBookingId(eachBooking.getId());
                                oneSlot.setService(eachBooking.getService());
                                oneSlot.setCustomerInfo(eachBooking.getCustomerInfo());
                                oneSlot.setCustomerPhone(eachBooking.getCustomerInfo().getAccount().getPhone());
                                // Kiểm tra xem booking đó có service là Default hay Customized
                                if(eachBooking.getService().isCustomized()){
                                    oneSlot.setProcess(eachBooking.getProcess());

                                    // set là null vì ngày này chỉ lên để khám thôi
                                    oneSlot.setProcessStep(null);
                                }else{
                                    // Là default service
                                    oneSlot.setProcess(null);
                                    oneSlot.setProcessStep(null);
                                }


                            }
                        }
                    }
                }

                // Nếu như không có booking thì qua kiềm tra bên booking_process_step
                if(!flagBooking){
                    // Nếu List booking_process_step tồn tại
                    if(listProcessStep != null){
                        for(BookingProcessStep eachStep : listProcessStep){
                            if(eachStep.getDay() != null && eachStep.getSlot() != null){
                                if(eachStep.getDay().compareTo(eachSchedule.getDay()) == 0){
                                    if(eachStep.getSlot().getTime().compareTo(eachSchedule.getSlot().getTime()) == 0){

                                        oneSlot.setBookingId(eachStep.getBooking().getId());
                                        oneSlot.setService(eachStep.getBooking().getService());
                                        oneSlot.setCustomerInfo(eachStep.getBooking().getCustomerInfo());
                                        oneSlot.setCustomerPhone(eachStep.getBooking().getCustomerInfo().getAccount().getPhone());
                                        oneSlot.setProcess(eachStep.getProcessStep().getProcess());
                                        oneSlot.setProcessStep(eachStep.getProcessStep());
                                    }
                                }
                            }
                        }
                    }

                }

                // set vào list result
                listItemOfResult.add(oneSlot);
                result.setListStaffScheduleForSlots(listItemOfResult);
                listResult.add(result);

            }

        }

        return listResult;
    }


}
