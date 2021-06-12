package com.example.demo.controller;

import com.example.demo.dto.request.CreateDefaultServiceDTOreq;
import com.example.demo.dto.request.ServiceComponentDTOresq;
import com.example.demo.dto.request.ServiceStepDTOres;
import com.example.demo.dto.request.UpdateDefaultServiceDTOres;
import com.example.demo.dto.response.*;
import com.example.demo.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/manager")
@CrossOrigin
public class ManagerController {

    @Autowired
    ManagerService managerService;

    // 1. Api Lấy tất cả Service Component của company
    @GetMapping(value = "/get_all_service_component_by_manager")
    public ResponseEntity<ServiceStepOutput> getAllServiceComponentByManager (
            @RequestParam("page") int page,
            @RequestParam("limit") int limit,
            @RequestParam("keyword") String keyword,
            @RequestParam("sortBy") String sortBy,
            @RequestParam("type") String type,
            @RequestParam("idCompany") int idCompany
    ){
        ServiceStepOutput output = managerService.getAllServiceComponentByManager(page,limit, keyword, sortBy, type, idCompany);
        if( output == null){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    // 2. Api lấy idCompany của manager
    @GetMapping(value = "/get_id_company_for_manager")
    public CompanyDTO getIdCompanyForManager(@RequestParam("idManager") int idManager){
        return managerService.getIdCompanyForManager(idManager);
    }

    // 3. Api lấy detail service component theo id
    @GetMapping(value = "/get_detail_service_component_by_id/{id}")
    public ResponseEntity<ServiceComponentDTO> getDetailServiceComponentById(@PathVariable("id") int id){
        ServiceComponentDTO result = managerService.getDetailServiceComponentById(id);
        if(result == null){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 4. Api update Service Component by id
    @PutMapping(value = "/update_service_component/{id}")
    public String updateServiceComponent(@PathVariable("id") int idServiceComponent, @RequestBody ServiceComponentDTOresq dto){
       return  managerService.updateServiceComponent(idServiceComponent, dto);
    }

    // 5. Api delete Service Component theo id
    @DeleteMapping(value = "/delete_service_component/{id}")
    public ResponseEntity<String> deleteServiceComponent(@PathVariable("id") int idServiceStep){
        String result = managerService.deleteServiceComponent(idServiceStep);
        if(result == null){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(result.equals("SUCCESS")){
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(result, HttpStatus.CONFLICT);
    }


    // 6. Api tạo mới Service Component
    @PostMapping(value = "/create_new_service_component")
    public String createNewServiceComponent (@RequestBody ServiceComponentDTOresq dto){
        return  managerService.createNewServiceComponent(dto);
    }




    // 7. Service Management: Lấy tất cả default service
    @GetMapping(value = "/get_all_default_service")
    public ServiceOutput getAllDefaultService(
            @RequestParam("page") Integer page,
            @RequestParam("limit") Integer limit,
            @RequestParam("keyword") String keyword,
            @RequestParam("sortBy") String sortBy,
            @RequestParam("type") String type,
            @RequestParam("companyId") Integer companyId){
        return managerService.getAllDefaultService(page, limit, keyword, sortBy, type, companyId);
    }

    // 8. Service Management: Lấy tất cả customized service
    @GetMapping(value = "/get_all_customized_service")
    public ServiceOutput getAllCustomizedService(
            @RequestParam("page") Integer page,
            @RequestParam("limit") Integer limit,
            @RequestParam("keyword") String keyword,
            @RequestParam("sortBy") String sortBy,
            @RequestParam("type") String type,
            @RequestParam("companyId") Integer companyId){
        return managerService.getAllCustomizedService(page, limit, keyword, sortBy, type, companyId);
    }


    // 9. Service Management: API tạo mới Default Service
    @PostMapping(value = "/create_new_default_service")
    public String createNewDefaultService(@RequestBody CreateDefaultServiceDTOreq dto){
        return managerService.createNewDefaultService(dto);
    }


    // 10. Service Management: API lấy Default Service by serviceId
    @GetMapping(value = "/get_one_default_service_by_service_id/{id}")
    public DefaultServiceDTO getOneDefaultServiceByServiceId(@PathVariable("id") Integer serviceId){
        return managerService.getOneDefaultServiceByServiceId(serviceId);
    }

    // 11. Service Management: API update lại Default Service by serviceId
    @PutMapping(value = "/update_default_service_by_service_id/{id}")
    public String updateDefaultServiceByServiceId(@PathVariable("id") Integer serviceId, @RequestBody UpdateDefaultServiceDTOres dto){
        return managerService.updateDefaultServiceByServiceId(serviceId, dto);
    }




    // . Staff Management : Lấy tất cả nhân viên trong 1 công ty
    @GetMapping(value = "/get_all_staff_in_company")
    public ResponseEntity<List<StaffInfoDTO>> getAllStaffInCompany(@RequestParam("companyId") int companyId){
        List<StaffInfoDTO> listResult = managerService.getAllStaffInCompany(companyId);
        if(listResult == null){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(listResult.isEmpty()){
            return new ResponseEntity<>(listResult, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(listResult, HttpStatus.OK);
    }

    // 7. Staff Management : Lấy lịch làm việc của một Company theo ngày truyền vào
//    @GetMapping(value = "/get_schedule_of_each_company_for_date")
//    public ResponseEntity<List<WorkTimeDTO>> getScheduleOfEachCompanyForDate(@RequestParam("startOfWeek") Date startOfWeek,
//                                                                @RequestParam("endOfWeek") Date endOfWeek,
//                                                                @RequestParam("companyId") Integer companyId,
//                                                                @RequestParam("staffId") Integer staffId){
//        List<WorkTimeDTO> result = managerService.getScheduleOfEachCompanyForDate(startOfWeek, endOfWeek, companyId, staffId);
//        if(result == null){
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }


    // 8. Booking : Lấy tất cả các yêu cầu đặt lịch theo companyId
//    @GetMapping(value = "/get_all_booking_request_by_companyId")
//    public ResponseEntity<List<BookingRequestQuery>> getAllBookingRequestByCompanyId(@RequestParam("companyId") Integer companyId){
//        List<BookingRequestQuery> listResult = managerService.getAllBookingRequestByCompanyId(companyId);
//        if(listResult.isEmpty()){
//            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//        return new ResponseEntity<>(listResult, HttpStatus.OK);
//    }


    // 9. Booking: Lấy tất cả nhân viên rảnh trong 1 slot detail
//    @GetMapping(value = "/get_staff_free_in_one_slot_detail")
//    public ResponseEntity<List<StaffInfoDTO>> getStaffFreeInOneSlotDetail(@RequestParam("slotDetailId") Integer slotDetailId,
//                                                        @RequestParam("typeName") String typeName
//                                                        ){
//        List<StaffInfoDTO> listResult = managerService.getStaffFreeInOneSlotDetail(slotDetailId, typeName);
//        if(listResult.isEmpty()){
//            return new ResponseEntity<>(listResult, HttpStatus.NO_CONTENT);
//        }
//        if(listResult == null){
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//        return new ResponseEntity<>(listResult, HttpStatus.OK);
//    }


    //==========================================================
    // ===================== API làm tốc độ dể demo cho thầy xem
    //=======================================================

    // Booking Request Management: API lấy tất cả booking request của 1 company
    @GetMapping(value = "/get_all_request_booking_of_one_company")
    public List<BookingRequestDTO> getAllRequestBookingOfOneCompany(@RequestParam("companyId") Integer companyId){
        return managerService.getAllRequestBookingOfOneCompany(companyId);
    }

    // Booking Request Management: API lấy ra các nhân viên có lịch rảnh trong 1 slot
    @GetMapping(value = "/get_staff_free_in_one_slot")
    public List<StaffInfoDTO> getStaffFreeInOneSlot(@RequestParam("bookingId") Integer bookingId){
        return managerService.getStaffFreeInOneSlot( bookingId);
    }

    // Booking Request Management: API assign nhân viên vào request booking
    @GetMapping(value = "/assign_staff_for_request_booking")
    public String assignStaffForRequestBooking(@RequestParam("bookingId") Integer bookingId, @RequestParam("staffId") Integer staffId){
        return managerService.assignStaffForRequestBooking(bookingId, staffId);
    }



}
