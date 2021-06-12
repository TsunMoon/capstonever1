package com.example.demo.service.impl;


import com.example.demo.dto.request.CustomerInfoDTOres;
import com.example.demo.dto.response.*;
import com.example.demo.entity.*;
import com.example.demo.entity.Process;
import com.example.demo.repository.*;
import com.example.demo.service.CustomerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerInfoRepository customerInfoRepository;

    @Autowired
    GenderRepository genderRepository;

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    SlotRepository slotRepository;

    @Autowired
    BookingRepository bookingRepository;


    @Autowired
    StaffInfoRepository staffInfoRepository;

    @Autowired
    StaffScheduleRepository staffScheduleRepository;

    @Autowired
    ProcessStepRepository processStepRepository;

    @Autowired
    BookingProcessStepRepository bookingProcessStepRepository;

    @Autowired
    ServiceComponentRepository serviceComponentRepository;

    @Autowired
    PriceByDateRepository priceByDateRepository;

    @Autowired
    AccountRepository accountRepository;



    // 1. Api cập nhật thông tin profile
    @Override
    public String updateCustomerInfo(int customerId, CustomerInfoDTOres dto) {
        try{
            CustomerInfo _customerInfo = customerInfoRepository.findById(customerId).get();
            if(_customerInfo == null){
                return "Người dùng không tồn tại";
            }
            _customerInfo.setDateOfBirth(dto.getDateOfBirth());
            _customerInfo.setDistrict(dto.getDistrict());
            _customerInfo.setFullname(dto.getFullname());
            _customerInfo.setProvince(dto.getProvince());
            _customerInfo.setStreet(dto.getStreet());
            _customerInfo.setImage(dto.getImage());

            if(_customerInfo.getGender() == null){
                _customerInfo.setGender(genderRepository.findByName(dto.getGender().trim()));
            }else if (!_customerInfo.getGender().getName().equals(dto.getGender().trim())){
                _customerInfo.setGender(genderRepository.findByName(dto.getGender().trim()));
            }
            //Kiểm tra xem giới tính có như cũ ko ?


            customerInfoRepository.save(_customerInfo);
            return "SUCCESS";

        }catch(Exception ex){
            ex.printStackTrace();
            return ex.getMessage();
        }
    }

    // 2. Api lấy detail của profile theo id
    @Override
    public CustomerInfoDTO getCustomerProfileById(int customerId) {
        try{
            CustomerInfo _customerInfo = customerInfoRepository.findById(customerId).get();
            CustomerInfoDTO dto = new CustomerInfoDTO();
            BeanUtils.copyProperties(_customerInfo, dto);
            dto.setCustomerId(_customerInfo.getAccount().getId());
            dto.setGender(_customerInfo.getGender());

            return dto;
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    // 3. Api tạo ra request booking
    @Override
    public String createBookingRequest(Integer serviceId, Integer customerId, Integer slotId, java.sql.Date day) {

        try {
            Booking _bookingRequest = new Booking();



            // 1. Kiểm tra xem slot đó có còn trống để đặt hay không
            // Lấy ra type của service đó
            Optional<com.example.demo.entity.Service> serviceOptional = serviceRepository.findById(serviceId);

            if(serviceOptional.isEmpty()){
                return "Không lấy ra được type của service";
            }
            String typeName = serviceOptional.get().getType().getName();

            // Lấy ra danh sách nhân viên có lịch đi làm vào slot đó
            List<Integer> listEnable = staffScheduleRepository.getListStaffIdEnableWorkInASlot(slotId, day, typeName);

            // Lấy ra danh sách nhân viên đang làm việc trong slot đó
            List<Integer> listBookingWorking = bookingRepository.getListBookingIdWorkingBySlotId(slotId, day);

            // So sách list enable với working để xem còn mấy nhân viên có thể thực hiện
            Integer numberFreeStaff = listEnable.size() - listBookingWorking.size();

            if(numberFreeStaff <= 0){
                return "Tạm hết nhân viên có thể phục vụ slot này";
            }


            // 2. Kiểm tra xem service có tồn tại ko ?

            Optional<com.example.demo.entity.Service> service = serviceRepository.findById(serviceId);
            if (service.isPresent()) {
                _bookingRequest.setService(service.get());
            } else {
                return "Không tìm thấy service";
            }
            Optional<CustomerInfo> customerInfo = customerInfoRepository.findById(customerId);
            if (customerInfo.isPresent()) {
                _bookingRequest.setCustomerInfo(customerInfo.get());
            } else {
                return "Không tìm thấy customer info";
            }


            _bookingRequest.setCreateAt(new Date(System.currentTimeMillis()));
            _bookingRequest.setStatus("pending");

            Optional<Slot> slot = slotRepository.findById(slotId);
            if (slot.isPresent()) {
                _bookingRequest.setSlot(slot.get());
            } else {
                return "Không tìm thấy slot id";
            }

            _bookingRequest.setDay(day);


            bookingRepository.save(_bookingRequest);
            return "SUCCESS";

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


    // 4. Api lấy tất cả booking của 1 customer
    @Override
    public List<BookingDTO> getAllBookingOfOneCustomer(Integer customerId) {
        List<BookingDTO> listResult = new ArrayList<>();
        List<Booking> listBookings = bookingRepository.getBookingByCustomerId(customerId);


        for(Booking each : listBookings){
            BookingDTO result = new BookingDTO();
            BeanUtils.copyProperties(each, result);
            result.setBookingId(each.getId());
            try {
                result.setPhoneStaff(accountRepository.findById(each.getStaffInfo().getStaffId()).get().getPhone());
            }catch(Exception ex){
                throw new RuntimeException("Not have Staff in this booking ");
            }

            listResult.add(result);
        }
        return listResult;
    }


    // 5. Api theo dõi liệu trình của từng khách hàng
    @Override
    public FollowProcess getFollowProcessOfEachCustomer(Integer bookingId) {
        FollowProcess result = new FollowProcess();

        // Lấy ra 1 booking
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        if(bookingOptional.isPresent()){

            Booking booking = bookingOptional.get();

            result.setBookingId(booking.getId());

            com.example.demo.entity.Service serviceBooking = booking.getService();
            // Kiểm xem có service ko, Thường thi lúc nào book lịch thì cũng phải có service rồi
            if(serviceBooking == null){
                throw new RuntimeException("In Booking not have service!!");
            }



            // Khi có 1 booking rồi từ đó lấy ra 1 service
            result.setService(serviceBooking);

            // Kiểm tra xem service đó có phải là customized service ko ?
            if (serviceBooking.isCustomized()) {
//
                Process process = booking.getProcess();
                if(process == null){
                    result.setProcess(null);
                    result.setListBookingProcessStepsDTO(null);
                    return result;
                }

                // Nếu là customized service thì Lấy ra thêm 1 process từ booking
                result.setProcess(booking.getProcess());

                // Lấy ra list process_step theo process_id
                List<ProcessStep> listProcessSteps = processStepRepository.getAllByProcessId(booking.getProcess().getId());

                // Sắp xếp lại thứ tự từ 1 đến 2
                Collections.sort(listProcessSteps, new Comparator<ProcessStep>() {
                    @Override
                    public int compare(ProcessStep p1, ProcessStep p2) {
                        return p1.getOrdinal().compareTo(p2.getOrdinal());
                    }
                });

                // Lấy list booking_process_step có process_step_id
                List<BookingProcessStepDTO> listBookingProcessStepDTO = new ArrayList<>();
                for (ProcessStep eachProcessStep : listProcessSteps) {
                    BookingProcessStep bookingProcessStep = bookingProcessStepRepository.getByProcessStepIdAndBookingId(eachProcessStep.getId(), bookingId);
                    if(bookingProcessStep == null){
                        throw new RuntimeException("Booking Process Step is null");
                    }
                    BookingProcessStepDTO bookingProcessStepDTO = new BookingProcessStepDTO();
                    BeanUtils.copyProperties(bookingProcessStep, bookingProcessStepDTO);


                    ProcessStepDTO copyProcessStepDTO = new ProcessStepDTO();
                    BeanUtils.copyProperties(bookingProcessStep.getProcessStep(),copyProcessStepDTO);
                    bookingProcessStepDTO.setProcessStepDTO(copyProcessStepDTO);

                    ServiceComponent copyEntityServiceComponent = bookingProcessStep.getProcessStep().getServiceComponent();
                    ServiceComponentDTO copyServiceComponentDTO = new ServiceComponentDTO();
                    BeanUtils.copyProperties(copyEntityServiceComponent, copyServiceComponentDTO);


                    copyProcessStepDTO.setServiceComponentDTO(copyServiceComponentDTO);
                    copyProcessStepDTO.getServiceComponentDTO().setPrice(priceByDateRepository.findByServiceComponentIdWhereIsUsedTrue(copyEntityServiceComponent.getId()).getPrice());


                    listBookingProcessStepDTO.add(bookingProcessStepDTO);
                }
                result.setListBookingProcessStepsDTO(listBookingProcessStepDTO);
            }else{
                result.setProcess(null);
                result.setListBookingProcessStepsDTO(null);
            }
        }else{
            throw new RuntimeException("Not find any booking by booking_id");
        }
        return result;
    }


    // 6. Api hiện lịch tới spa theo ngày của từng khách hàng
    @Override
    public List<ScheduleGoSpa> getScheduleGoSpaOfOneCustomer(Integer customerId) {
        java.sql.Date currentDay = new java.sql.Date(System.currentTimeMillis());

        // 1. Tạo 1 list result trả về
        List<ScheduleGoSpa> listResult = new ArrayList<>();

        // 2. Lấy list Booking với customerId và currentDay
        List<Booking> listBooking = bookingRepository.getAllBookingByCustomerIdAndLargerThanCurrentDay(customerId, currentDay);
        if(listBooking.isEmpty()){
            return listResult;
        }



        // 3. Kiểm tra each of listBooking  rồi thêm vào tronng listResult
        for(Booking eachBooking : listBooking){
            Boolean flag = false;
            for(ScheduleGoSpa eachSchedule : listResult){
                if(eachBooking.getDay().compareTo(eachSchedule.getDay()) == 0){
                    // Có trùng : thì add thêm vào trong ListScheduleGoSpaItems
                    flag = true;

                    ScheduleGoSpaItem item = new ScheduleGoSpaItem();
                    item.setTime(eachBooking.getSlot().getTime());
                    item.setBooking(eachBooking);
                    item.setCompany(eachBooking.getService().getCompany());

                    eachSchedule.getListScheduleGoSpaItems().add(item);
                }
            }
            // Nếu ko có day trùng thì ta tạo day mới
            if(!flag){
                // tạo mới 1 day trong list result
                ScheduleGoSpa scheduleGoSpa = new ScheduleGoSpa();
                scheduleGoSpa.setDay(eachBooking.getDay());

                ScheduleGoSpaItem item = new ScheduleGoSpaItem();
                item.setTime(eachBooking.getSlot().getTime());
                item.setBooking(eachBooking);
                item.setCompany(eachBooking.getService().getCompany());

                // Tạo mới 1 ListScheduleGoSpaItems
                List<ScheduleGoSpaItem> listItem = new ArrayList<>();
                listItem.add(item);

                scheduleGoSpa.setListScheduleGoSpaItems(listItem);
                listResult.add(scheduleGoSpa);
            }
        }


        // 4. Lấy ra list booking process step có customer và ngày lớn hơn ngày hiện tại
        List<BookingProcessStep> listBookingProcessSteps = bookingProcessStepRepository.getAllByCustomerIdAndLargerThanCurrentDay(customerId, currentDay);
        if(listBookingProcessSteps.isEmpty()){
            throw new RuntimeException("Booking Process Step of each customer have customized service is not available");
        }


        // Kiểm tra xem booking process step có
        for(BookingProcessStep eachBookingProcessStep : listBookingProcessSteps){
            Boolean flag = false;

            if(eachBookingProcessStep.getDay() != null){
                for(ScheduleGoSpa eachSchedule : listResult){
                    if(eachBookingProcessStep.getDay().compareTo(eachSchedule.getDay()) == 0){
                        // Có trùng : thì add thêm vào trong ListScheduleGoSpaItems
                        flag = true;

                        ScheduleGoSpaItem item = new ScheduleGoSpaItem();
                        item.setTime(eachBookingProcessStep.getSlot().getTime());
                        item.setBooking(eachBookingProcessStep.getBooking());
                        item.setCompany(eachBookingProcessStep.getBooking().getService().getCompany());

                        eachSchedule.getListScheduleGoSpaItems().add(item);
                    }
                }
                // Nếu ko có day trùng thì ta tạo day mới
                if(!flag){
                    // tạo mới 1 day trong list result
                    ScheduleGoSpa scheduleGoSpa = new ScheduleGoSpa();
                    scheduleGoSpa.setDay(eachBookingProcessStep.getDay());

                    ScheduleGoSpaItem item = new ScheduleGoSpaItem();
                    item.setTime(eachBookingProcessStep.getSlot().getTime());
                    item.setBooking(eachBookingProcessStep.getBooking());
                    item.setCompany(eachBookingProcessStep.getBooking().getService().getCompany());

                    // Tạo mới 1 ListScheduleGoSpaItems
                    List<ScheduleGoSpaItem> listItem = new ArrayList<>();
                    listItem.add(item);

                    scheduleGoSpa.setListScheduleGoSpaItems(listItem);
                    listResult.add(scheduleGoSpa);
                }
            }

        }


        return listResult;
    }


}
