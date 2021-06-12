package com.example.demo.service;


import com.example.demo.dto.request.CreateDefaultServiceDTOreq;
import com.example.demo.dto.request.ServiceComponentDTOresq;
import com.example.demo.dto.request.ServiceStepDTOres;
import com.example.demo.dto.request.UpdateDefaultServiceDTOres;
import com.example.demo.dto.response.*;
import org.springframework.web.bind.annotation.PathVariable;

import java.sql.Date;
import java.util.List;

public interface ManagerService {
    // 1. Api Lấy tất cả Service Component của company
    ServiceStepOutput getAllServiceComponentByManager(int page, int limit, String keyword, String sortBy, String type, int idCompany);

    // 2. Api lấy idCompany của manager
    CompanyDTO getIdCompanyForManager(int idManager);

    // 3. Api lấy detail service component theo id
    ServiceComponentDTO getDetailServiceComponentById(int serviceComponentId);

    // 4. Api update Service Component by id
    String updateServiceComponent(int idServiceComponent, ServiceComponentDTOresq dto);

    // 5. Api delete Service Component theo id
    String deleteServiceComponent(int idServiceStep);

    // 6. Api tạo mới Service Component
    String createNewServiceComponent(ServiceComponentDTOresq dto);

    // 7. Service Management: Lấy tất cả default service
    ServiceOutput getAllDefaultService(Integer page, Integer limit, String keyword, String sortBy, String type, Integer companyId);

    // 8. Service Management: Lấy tất cả customized service
    ServiceOutput getAllCustomizedService(Integer page, Integer limit, String keyword, String sortBy, String type, Integer companyId);

    // 9. Service Management: API tạo mới Default Service
    String createNewDefaultService(CreateDefaultServiceDTOreq dto);

    // 10. Service Management: API lấy Default Service by serviceId
    DefaultServiceDTO getOneDefaultServiceByServiceId(Integer serviceId);


    // 11. Service Management: API update lại Default Service by serviceId
    String updateDefaultServiceByServiceId( Integer serviceId, UpdateDefaultServiceDTOres dto);


    // . Lấy tất cả nhân viên trong 1 công ty
    List<StaffInfoDTO> getAllStaffInCompany(int companyId);

    // 7. Staff Management : Lấy lịch làm việc của một Company theo ngày truyền vào
//    List<WorkTimeDTO> getScheduleOfEachCompanyForDate(Date startOfWeek, Date endOfWeek, Integer companyId, Integer staffId);

    // 8. Booking : Lấy tất cả các yêu cầu đặt lịch theo companyId
//    List<BookingRequestQuery> getAllBookingRequestByCompanyId(Integer companyId);

    // 9. Booking: Lấy tất cả nhân viên rảnh trong 1 slot detail
//    List<StaffInfoDTO> getStaffFreeInOneSlotDetail(Integer slotDetailId, String typeName);

    //==========================================================
    // ===================== API làm tốc độ dể demo cho thầy xem
    //=======================================================

    // Booking Request management: API lấy tất cả booking request của 1 company
    List<BookingRequestDTO> getAllRequestBookingOfOneCompany(Integer companyId);

    // Booking Request Management: API lấy ra các nhân viên có lịch rảnh trong 1 slot
    List<StaffInfoDTO> getStaffFreeInOneSlot( Integer bookingId);

    // Booking Request Management: API assign nhân viên vào request booking
    String assignStaffForRequestBooking(Integer bookingId, Integer staffId);




}
