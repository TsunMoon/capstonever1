package com.example.demo.service.impl;

import com.example.demo.dto.request.*;
import com.example.demo.dto.response.*;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.service.ManagerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ManagerServiceImpl implements ManagerService {

    @Autowired
    ServiceComponentRepository serviceComponentRepository;

    @Autowired
    StaffInfoRepository staffInfoRepository;

    @Autowired
    PriceByDateRepository priceByDateRepository;

    @Autowired
    TypeRepository typeRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    StaffScheduleRepository staffScheduleRepository;

    @Autowired
    SlotRepository slotRepository;


    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    CompanyTypeRepository companyTypeRepository;

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    ServiceServiceComponentRepository serviceServiceComponentRepository;


    // 1. Api Lấy tất cả Service Component của company
    @Override
    public ServiceStepOutput getAllServiceComponentByManager(int page, int limit, String keyword, String sortBy, String type, int idCompany) {
        try {
            ServiceStepOutput output = new ServiceStepOutput();
            output.setPage(page);
            Pageable pageable = null;

            switch (sortBy) {
                case "none":
                    pageable = PageRequest.of(page - 1, limit);
                    break;
                case "name_ascending":
                    pageable = PageRequest.of(page - 1, limit, Sort.by("name").ascending());
                    break;
                case "name_descending":
                    pageable = PageRequest.of(page - 1, limit, Sort.by("name").descending());
                    break;
                case "date_ascending":
                    pageable = PageRequest.of(page - 1, limit, Sort.by("createAt").ascending());
                    break;
                case "date_descending":
                    pageable = PageRequest.of(page - 1, limit, Sort.by("createAt").descending());
                    break;
                case "price_ascending":
                    pageable = PageRequest.of(page - 1, limit, Sort.by("p.price").ascending());
                    break;
                case "price_descending":
                    pageable = PageRequest.of(page - 1, limit, Sort.by("p.price").descending());
                    break;
            }
            if (pageable == null) {
                return output;
            }

            List<ServiceComponentDTO> listServiceComponentDTOS = serviceComponentRepository.getAllServiceComponentByManager(keyword.trim(), idCompany, type, pageable);
            output.setListServiceComponentDTOS(listServiceComponentDTOS);


            Integer numberSearch = algorithmToGetTotalPage(idCompany, keyword.trim(), type);
            if (numberSearch == null) {
                return null;
            }
            output.setTotalPage((int) Math.ceil((double) numberSearch / limit));
            output.setTotalItem(numberSearch);

            return output;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


    // 2. Api lấy idCompany của manager
    @Override
    public CompanyDTO getIdCompanyForManager(int idManager) {

        CompanyDTO result = new CompanyDTO();
        StaffInfo staffInfo = staffInfoRepository.findByStaffId(idManager);
        if (staffInfo == null) {
            throw new RuntimeException("Staffinfo by staffId is not available");
        }
        result.setId(staffInfo.getStaffId());
        List<CompanyType> listCompanyTypes = staffInfo.getCompany().getListCompanyTypes();
        // Lấy list type từ list company type
        List<Type> listTypes = new ArrayList<>();
        for (CompanyType eachCompanyType : listCompanyTypes) {
            listTypes.add(eachCompanyType.getType());
        }

        result.setListTypes(listTypes);


        return result;

    }

    // 3. Api lấy detail service Component theo id
    @Override
    public ServiceComponentDTO getDetailServiceComponentById(int serviceComponentId) {
        try {
            ServiceComponentDTO dto = new ServiceComponentDTO();
            Optional<ServiceComponent> serviceStepOptional = serviceComponentRepository.findById(serviceComponentId);
            if (serviceStepOptional.isPresent()) {
                ServiceComponent serviceComponent = serviceStepOptional.get();

                BeanUtils.copyProperties(serviceComponent, dto);
                dto.setPrice(priceByDateRepository.findByServiceComponentIdWhereIsUsedTrue(dto.getId()).getPrice());
                dto.setType(serviceComponent.getType());
                return dto;
            }
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // 4. Api update Service Component by id
    @Override
    public String updateServiceComponent(int idServiceComponent, ServiceComponentDTOresq dto) {
        // Kiểm tra xem service_component_id có tồn tại ko ?
        Optional<ServiceComponent> serviceComponentOptional = serviceComponentRepository.findById(idServiceComponent);

        // Kiểm tra các field rỗng
        if (dto.getDescription() == "" || dto.getName() == "" || dto.getCompanyId() == 0 || dto.getTypeName() == "") {
            throw new RuntimeException("Field in Update service component not empty!!!");
        }

        if (serviceComponentOptional.isPresent()) {
            ServiceComponent _serviceComponent = serviceComponentOptional.get();
            BeanUtils.copyProperties(dto, _serviceComponent);
            _serviceComponent.setRemoved(false);
            try {
                _serviceComponent.setType(typeRepository.findByName(dto.getTypeName()));
            } catch (Exception ex) {
                throw new RuntimeException("TypeName is not available");
            }

            // Kiểm tra xem price có bằng 0 hay bé hơn 1000


            //Kiểm tra price
            float priceUpdate = dto.getPrice();

            // Kiểm tra priceUpdate có bé hơn 1000 đồng ko
            if (priceUpdate < 1000) {
                throw new RuntimeException("Price is not less than 1.000 vnd");
            }

            PriceByDate priceIsUsing = new PriceByDate();
            try {
                priceIsUsing = priceByDateRepository.findByServiceComponentIdWhereIsUsedTrue(idServiceComponent);
            } catch (Exception ex) {
                throw new RuntimeException("Có 2 Price đc set isUsed = true");
            }


            if (priceIsUsing.getPrice() != priceUpdate) {
                // Kiểm tra xem Price đó đã có trc chưa
                List<PriceByDate> listPriceByDates = priceByDateRepository.findByServiceComponentId(idServiceComponent);
                for (PriceByDate eachPrice : listPriceByDates) {
                    if (eachPrice.getPrice() == priceUpdate) {
                        eachPrice.setUsed(true);
                        priceIsUsing.setUsed(false);

                        priceByDateRepository.save(eachPrice);
                        priceByDateRepository.save(priceIsUsing);
                        return "Success";
                    }
                    eachPrice.setUsed(false);
                    priceByDateRepository.save(eachPrice);
                }


                // Tạo Price mới
                PriceByDate _priceByDate = new PriceByDate();
                _priceByDate.setCreateAt(new Date(System.currentTimeMillis()));
                _priceByDate.setUsed(true);
                _priceByDate.setPrice(priceUpdate);
                // Lưu Service Step
                ServiceComponent saveServiceComponent = serviceComponentRepository.save(_serviceComponent);
                _priceByDate.setServiceComponent(saveServiceComponent);

                priceByDateRepository.save(_priceByDate);

                // Set isUsed Price cũ về false
                priceIsUsing.setUsed(false);
                priceByDateRepository.save(priceIsUsing);
            }
            // Nếu current price = update price thì lưu lại những cái update
            ServiceComponent saveServiceComponent = serviceComponentRepository.save(_serviceComponent);
            return "Success";
        }
        throw new RuntimeException("Không tìm thấy Service Step đê cập nhật");
    }

    // 5. Api delete Service Component theo id
    @Override
    public String deleteServiceComponent(int idServiceStep) {
        try {
            // Kiểm tra có Service Step ko ?
            Optional<ServiceComponent> serviceStepOptional = serviceComponentRepository.findById(idServiceStep);
            if (serviceStepOptional.isPresent()) {
                ServiceComponent _serviceComponent = serviceStepOptional.get();

                // Rồi dởi isRemoved thành true trong Service Step
                _serviceComponent.setRemoved(true);
                serviceComponentRepository.save(_serviceComponent);
                return "SUCCESS";
            }

            return "Không tìm thấy Service Step để xóa";
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // 6. Api tạo mới Service Component
    @Override
    public String createNewServiceComponent(ServiceComponentDTOresq dto) {

        ServiceComponent _serviceComponent = new ServiceComponent();


        // 1. Kiểm tra null tất cả các field
        if (dto.getDescription() == null || dto.getImage() == null || dto.getName() == null || dto.getCompanyId() == null || dto.getTypeName() == null) {
            throw new RuntimeException("Field in Create service component not null !!!");
        }

        // Kiểm tra rỗng tất cả các field
        if (dto.getDescription() == "" || dto.getName() == "" || dto.getCompanyId() == 0 || dto.getTypeName() == "") {
            throw new RuntimeException("Field in Create service component not empty!!!");
        }

        // Kiểm tra pirce trong  lớn hơn 1000 vnd
        if (dto.getPrice() < 1000) {
            throw new RuntimeException("Price must larger than 1.000 vnd !!!");
        }


        // 2. Kiểm tra companyId có tồn tại ko ?
        Optional<Company> companyOptional = companyRepository.findById(dto.getCompanyId());
        if (companyOptional.isEmpty()) {
            throw new RuntimeException("companyId in create service component not available");
        }
        Company company = companyOptional.get();


        // 3. Kiểm tra type_id có tồn tại trong type của company ko ?
        String typeName = dto.getTypeName();
        CompanyType companyType = companyTypeRepository.findByCompanyIdAndTypeName(company.getId(), typeName);
        if (companyType == null) {
            throw new RuntimeException("companyType in create service component not available ");
        }

        // Gán vào entity để lưu vào db
        BeanUtils.copyProperties(dto, _serviceComponent);
        _serviceComponent.setCreateAt(new Date(System.currentTimeMillis()));
        _serviceComponent.setRemoved(false);
        _serviceComponent.setCompany(company);
        try {
            _serviceComponent.setType(typeRepository.findByName(typeName));
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error in find type service component");
        }

        ServiceComponent saveServiceComponent = serviceComponentRepository.save(_serviceComponent);
        if (saveServiceComponent == null) {
            throw new RuntimeException("Error in save service component");
        }

        // 4. Kiểm tra price
        float price = dto.getPrice();

        PriceByDate priceByDate = new PriceByDate();
        priceByDate.setCreateAt(new Date(System.currentTimeMillis()));
        priceByDate.setUsed(true);
        priceByDate.setPrice(price);
        priceByDate.setUpdateAt(null);
        priceByDate.setServiceComponent(saveServiceComponent);


        PriceByDate savePriceByDate = priceByDateRepository.save(priceByDate);
        if (savePriceByDate == null) {
            throw new RuntimeException("Error in save price by date");
        }

        return "SUCCESS";
    }


    // 7. Service Management: Lấy tất cả default service
    @Override
    public ServiceOutput getAllDefaultService(Integer page, Integer limit, String keyword, String sortBy, String type, Integer companyId) {

        ServiceOutput output = new ServiceOutput();
        output.setPage(page);
        Pageable pageable = null;

        switch (sortBy) {
            case "none":
                pageable = PageRequest.of(page - 1, limit);
                break;
            case "name_ascending":
                pageable = PageRequest.of(page - 1, limit, Sort.by("name").ascending());
                break;
            case "name_descending":
                pageable = PageRequest.of(page - 1, limit, Sort.by("name").descending());
                break;
        }
        if (pageable == null) {
            return output;
        }

        List<ServiceDTO> listResult = new ArrayList<>();

        // Lấy list default service theo companyId và isRemoved = false
        List<com.example.demo.entity.Service> listServices = serviceRepository.getAllDefaultServiceByCompanyId(companyId, keyword, type, pageable);
        if (listServices != null) {
            for (com.example.demo.entity.Service eachService : listServices) {
                ServiceDTO result = new ServiceDTO();
                BeanUtils.copyProperties(eachService, result);
                listResult.add(result);
            }
            output.setListServiceDTOS(listResult);
            output.setTotalItem(listServices.size());
            output.setTotalPage((int) Math.ceil((double) listServices.size() / limit));

        }

        return output;
    }

    @Override
    public ServiceOutput getAllCustomizedService(Integer page, Integer limit, String keyword, String sortBy, String type, Integer companyId) {
        ServiceOutput output = new ServiceOutput();
        output.setPage(page);
        Pageable pageable = null;

        switch (sortBy) {
            case "none":
                pageable = PageRequest.of(page - 1, limit);
                break;
            case "name_ascending":
                pageable = PageRequest.of(page - 1, limit, Sort.by("name").ascending());
                break;
            case "name_descending":
                pageable = PageRequest.of(page - 1, limit, Sort.by("name").descending());
                break;
        }
        if (pageable == null) {
            return output;
        }

        List<ServiceDTO> listResult = new ArrayList<>();

        List<com.example.demo.entity.Service> listServices = serviceRepository.getAllCustomizedServiceByCompanyId(companyId, keyword, type, pageable);

        if (listServices != null) {
            for (com.example.demo.entity.Service eachService : listServices) {
                ServiceDTO result = new ServiceDTO();
                BeanUtils.copyProperties(eachService, result);
                listResult.add(result);
            }
            output.setListServiceDTOS(listResult);
            output.setTotalItem(listServices.size());
            output.setTotalPage((int) Math.ceil((double) listServices.size() / limit));

        }
        return output;
    }


    // 9. Service Management: API tạo mới Default Service
    @Override
    public String createNewDefaultService(CreateDefaultServiceDTOreq dto) {

        // Kiểm tra các field có bị null ko ?
        if (dto.getDescription() == null || dto.getImage() == null || dto.getName() == null || dto.getCompanyId() == null
                || dto.getTypeName() == null || dto.getListOrdinalServiceComponents().isEmpty()) {
            throw new RuntimeException("Not input null, please check input again !!!");
        }

        // Kiểm tra các field có rỗng ko ?
        if (dto.getDescription().equals("") || dto.getName().equals("") || dto.getCompanyId() == 0 || dto.getTypeName().equals("")) {
            throw new RuntimeException("Not input empty, please check again !!!");
        }

        com.example.demo.entity.Service _service = new com.example.demo.entity.Service();
        BeanUtils.copyProperties(dto, _service);
        _service.setCustomized(false);
        _service.setRemoved(false);

        Optional<Company> companyOptional = companyRepository.findById(dto.getCompanyId());
        if (companyOptional.isEmpty()) {
            throw new RuntimeException("Company with this this companyId is not available");
        }
        _service.setCompany(companyOptional.get());


        Type type = typeRepository.findByName(dto.getTypeName());
        if (type == null) {
            throw new RuntimeException("This type is not available");
        }
        _service.setType(type);

        com.example.demo.entity.Service saveService = new com.example.demo.entity.Service();

        List<ServiceServiceComponent> listSave = new ArrayList<>();

        // Kiểm tra xem service_component có tồn tại ko rồi mới lưu service và service_service_component
        for (OrdinalServiceComponent eachOrdinal : dto.getListOrdinalServiceComponents()) {
            ServiceServiceComponent _serviceServiceComponent = new ServiceServiceComponent();

            _serviceServiceComponent.setOrdinal(eachOrdinal.getOrdinal());

            // Kiểm tra service_component có tồn tại ko ?
            Optional<ServiceComponent> serviceComponentOptional = serviceComponentRepository.findById(eachOrdinal.getServiceComponentId());
            if (serviceComponentOptional.isEmpty()) {
                throw new RuntimeException("Service Component by serviceComponentId is not existed !!!");
            }
            _serviceServiceComponent.setServiceComponent(serviceComponentOptional.get());

            listSave.add(_serviceServiceComponent);
        }


        // Nếu chạy hết vòng for mà ko có lỗi thì mới lưu service
        try {
            saveService = serviceRepository.save(_service);
        } catch (Exception ex) {
            throw new RuntimeException("Couldn't save Service !!!");
        }
        if (saveService == null) {
            throw new RuntimeException("Can't save service !!!");
        }

        for (ServiceServiceComponent eachSave : listSave) {
            eachSave.setService(saveService);
            try {
                serviceServiceComponentRepository.save(eachSave);
            } catch (Exception ex) {
                throw new RuntimeException("Save Service Service Component Failed!!!!");
            }
        }

        return "SUCCESS";
    }


    // 10. Service Management: API lấy Default Service by serviceId
    @Override
    public DefaultServiceDTO getOneDefaultServiceByServiceId(Integer serviceId) {

        DefaultServiceDTO result = new DefaultServiceDTO();

        com.example.demo.entity.Service service = serviceRepository.getDefaultServiceByServiceId(serviceId);
        if(service == null){
            throw new RuntimeException("This Service is not existed or not a default service");
        }


        BeanUtils.copyProperties(service, result);
        result.setListServiceServiceComponents(service.getListServiceServiceComponents());

        return result;
    }


    // 11. Service Management: API update lại Default Service by serviceId
    @Override
    public String updateDefaultServiceByServiceId(Integer serviceId, UpdateDefaultServiceDTOres dto) {

        // 1. Lấy Service bằng serviceId

        com.example.demo.entity.Service _service = serviceRepository.getDefaultServiceByServiceId(serviceId);
        if(_service == null){
            throw new RuntimeException("This service for update is not existed!!!");
        }

        // 2. Set các field update cho service
        _service.setDescription(dto.getDescription());
        _service.setImage(dto.getImage());
        _service.setName(dto.getName());


        // 3. Lấy ra list update service component
        List<ServiceServiceComponentDTOres> listServiceComponentUpdate = dto.getListServiceComponentOrdinal();

        // 4. Lấy ra 1 list ssc có sẵn của service
        List<ServiceServiceComponent> listSSC = _service.getListServiceServiceComponents();

        // 5.
        for(ServiceServiceComponent eachSSC : listSSC){
            boolean flag = false;

            for(ServiceServiceComponentDTOres eachRequest : dto.getListServiceComponentOrdinal()){
                if(eachSSC.getId() == eachRequest.getSscId()){
                    flag = true;

                    Integer serviceComponentId = eachRequest.getServiceComponentId();
                    if(serviceComponentId ==  null){
                        throw new RuntimeException("Not empty service component id");
                    }

                    // Kiểm tra xem service_component_id  có tồn tại
                    Optional<ServiceComponent> serviceComponentOptional = serviceComponentRepository.findById(serviceComponentId);
                    if(serviceComponentOptional.isEmpty()){
                        throw new RuntimeException("Service component by serviceComponentId is not existed !!!");
                    }
                    ServiceComponent serviceComponent = serviceComponentOptional.get();

                    eachSSC.setServiceComponent(serviceComponent);
                    // lưu service_service_component
                    serviceServiceComponentRepository.save(eachSSC);
                }
            }

            if(!flag){
                // Nếu ssc có sẵn ko có trùng với danh sách request thì xóa đi
                serviceServiceComponentRepository.delete(eachSSC);
            }
        }




        return null;
    }


    // . Lấy tất cả nhân viên trong 1 công ty
    @Override
    public List<StaffInfoDTO> getAllStaffInCompany(int companyId) {
        try {
            return staffInfoRepository.getAllStaffInCompany(companyId);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }



    //==========================================================
    // ===================== API làm tốc độ dể demo cho thầy xem
    //=======================================================

    // Booking Request management: API lấy tất cả booking request của 1 company


    @Override
    public List<BookingRequestDTO> getAllRequestBookingOfOneCompany(Integer companyId) {

        List<BookingRequestDTO> listResult = new ArrayList<>();

        List<Booking> listRequestBooking = bookingRepository.getAllRequestBookingHaveStatusIsPending(companyId);
        if(!listRequestBooking.isEmpty()){
            for(Booking eachRequestBooking : listRequestBooking){
                BookingRequestDTO dto = new BookingRequestDTO();
                BeanUtils.copyProperties(eachRequestBooking, dto);
                dto.setCustomerPhone(eachRequestBooking.getCustomerInfo().getAccount().getPhone());
                listResult.add(dto);
            }
        }

        return listResult;
    }


    // Booking Request Management: API lấy ra các nhân viên có lịch rảnh trong 1 slot
    @Override
    public List<StaffInfoDTO> getStaffFreeInOneSlot( Integer bookingId) {

        // 1. Lấy ra 1 booking theo bookingId
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        if(bookingOptional.isEmpty()){
            throw new RuntimeException("Booking with bookingId is not existed !!!");
        }
        Booking booking = bookingOptional.get();
        String typeName = booking.getService().getType().getName();
        Integer companyId = booking.getService().getCompany().getId();
        Integer slotId = booking.getSlot().getId();
        java.sql.Date day = booking.getDay();

        // 2.Lấy list staffSchedule có thể làm trong 1 slot
        List<StaffSchedule> listStaffScheduleEnable = staffScheduleRepository.getAllStaffScheduleBySlotIdAndDay(slotId, day, typeName);
        if(listStaffScheduleEnable.isEmpty()){
            throw new RuntimeException("Not have any staff free in this slot !!!");
        }

        //  3. Lấy list booking có status assigned trong 1 slot
        List<Booking> listBookingOfOneCompany = bookingRepository.getAllBookingHaveStatusAssignedInOneSlotAnfOneDayOfOneCompany(slotId, day, companyId);

        List<StaffSchedule> listFree = new ArrayList<>();

        if(!listBookingOfOneCompany.isEmpty()){
            for(StaffSchedule eachStaffSchedule : listStaffScheduleEnable){
                boolean flag = false;
                for(Booking eachBooking : listBookingOfOneCompany){
                    if(eachBooking.getStaffInfo().getAccount().getId() == eachStaffSchedule.getStaffInfo().getAccount().getId()){
                        flag = true;
                    }
                }

                if(!flag){
                    listFree.add(eachStaffSchedule);
                }
            }
        }else{
            listFree = listStaffScheduleEnable;
        }

        List<StaffInfoDTO> listResult = new ArrayList<>();
        for(StaffSchedule eachStaffSchedule : listFree){
            StaffInfoDTO result = new StaffInfoDTO();
            StaffInfo staffInfo = eachStaffSchedule.getStaffInfo();

            BeanUtils.copyProperties(staffInfo, result);
            result.setStaffId(staffInfo.getAccount().getId());
            result.setPhoneNumber(staffInfo.getAccount().getPhone());

            listResult.add(result);
        }


        return listResult;
    }



    // Booking Request Management: API assign nhân viên vào request booking
    @Override
    public String assignStaffForRequestBooking(Integer bookingId, Integer staffId) {

        // 1. Kiểm tra booking
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        if(bookingOptional.isEmpty()){
            throw new RuntimeException("Booking with bookingId is not existed !!!");
        }

        // 2. Kiểm tra staff có tồn tại ko
        StaffInfo staffInfo = staffInfoRepository.findByStaffId(staffId);
        if(staffInfo == null){
            throw new RuntimeException("Staff with staffId is not existed !!!");
        }

        Booking booking = bookingOptional.get();

        // Add staffId và sửa status
        booking.setStaffInfo(staffInfo);
        booking.setStatus("assigned");

        // Lưu booking lại
        bookingRepository.save(booking);


        return "SUCCESS";
    }


    // 7. Staff Management : Lấy lịch làm việc của một Company theo ngày truyền vào
//    @Override
//    public  List<WorkTimeDTO> getScheduleOfEachCompanyForDate(Date startOfWeek, Date endOfWeek, Integer companyId, Integer staffId) {
//        try {
//            // 1. Lấy 1 list slot bằng companyId
//            List<Slot> listSlots = slotRepository.getAllSlotByCompanyId(companyId);
//
//            List<WorkTimeDTO> listResultDto = new ArrayList<>();
//
//
//
//           List<Date> listDate = getDatesBetweenUsingJava7(startOfWeek, endOfWeek);
//
//
//
//                for(Slot eachSlot : listSlots){
//                    WorkTimeDTO resultDto = new WorkTimeDTO();
//                    resultDto.setSlotId(eachSlot.getId());
//
//                    List<StaffScheduleForSlot> listStaffScheduleForSlots = new ArrayList<>();
//                    for(Date eachDate : listDate){
//                    StaffScheduleForSlot staffScheduleForSlot = new StaffScheduleForSlot();
//
//                    // 2. Lấy slotDetail bằng slotId và ngày trong listDate
//                    SlotDetail slotDetail = slotDetailRepository.getSlotDetailBySlotIdAndCurrentDay(eachSlot.getId(), eachDate);
//                    if(slotDetail != null){
//                        // 3. Lấy staffSchedule bằng slotDetailID và staffId
//                        StaffSchedule staffSchedule = staffScheduleRepository.getStaffScheduleBySlotDetailIdAndStaffId(slotDetail.getId(), staffId);
//
//                        if(staffSchedule != null){
//                            BeanUtils.copyProperties(staffSchedule, staffScheduleForSlot);
//                            staffScheduleForSlot.setStaffScheduleId(staffSchedule.getId());
//                            staffScheduleForSlot.setStaffId(staffId);
//                            staffScheduleForSlot.setSlotDetailId(staffSchedule.getSlotDetail().getId());
//                            staffScheduleForSlot.setDay(eachDate);
//                        }
//                    }
//                    staffScheduleForSlot.setStaffId(staffId);
//                    staffScheduleForSlot.setDay(eachDate);
//
//
//                    listStaffScheduleForSlots.add(staffScheduleForSlot);
//
//                }
//                    resultDto.setListStaffScheduleForSlots(listStaffScheduleForSlots);
//                    listResultDto.add(resultDto);
//            }
//
//
//
//            return listResultDto;
//        }catch(Exception ex){
//            ex.printStackTrace();
//            return null;
//        }
//    }

    // 8. Booking : Lấy tất cả các yêu cầu đặt lịch theo companyId
//    @Override
//    public List<BookingRequestQuery> getAllBookingRequestByCompanyId(Integer companyId) {
//        try {
//
//            List<BookingRequestQuery> listResult = bookingRequestRepository.getAllBookingRequestByCompanyId(companyId);
//            if(listResult.isEmpty()){
//                return null;
//            }
//
//            return listResult;
//
//        }catch(Exception ex){
//            ex.printStackTrace();
//            return null;
//        }
//    }


    // 9. Booking: Lấy tất cả nhân viên rảnh trong 1 slot detail
//    @Override
//    public List<StaffInfoDTO> getStaffFreeInOneSlotDetail(Integer slotDetailId, String typeName) {
//        try{
//            // 1. Lấy tất cả nhân viên có thể làm trong 1 slot
//            List<IntegerDTO> listStaffIdEnable =  staffScheduleRepository.getListStaffIdEnableWorkInASlot(slotDetailId,typeName);
//
//            if(listStaffIdEnable.isEmpty()){
//                // trả về lỗi vì đã ko có nhân viên nào đi làm vào slot này
//                return new ArrayList<>();
//            }
//
//            // 2. Kiểm tra trong staff slot detail xem có nhân viên nào đang làm slot này ko
//            List<Integer> listStaffIdWorking = bookingRepository.getListStaffIdBySlotDetailId(slotDetailId);
//
//            if(listStaffIdEnable.size() == listStaffIdWorking.size()){
//                return new ArrayList<>();
//                // trả về lỗi vì ko có nhân viên nào rảnh vào slot này
//            }
//            if(listStaffIdWorking.size() > listStaffIdEnable.size()){
//                return null;
//            }
//
//            // Xóa nhưng staff đang làm ra khỏi danh sách rảnh
//            for(IntegerDTO eachEnable : listStaffIdEnable){
//                for(Integer eachWorking : listStaffIdWorking){
//                    if(eachWorking == eachEnable.getId()){
//                        listStaffIdEnable.remove(eachEnable);
//                    }
//                }
//            }
//
//            List<StaffInfoDTO> listResult = new ArrayList<>();
//
//            if(!listStaffIdEnable.isEmpty()){
//                for(IntegerDTO eachEnable : listStaffIdEnable){
//                    StaffInfoDTO result = new StaffInfoDTO();
//                    StaffInfo staffInfo = staffInfoRepository.findByStaffId(eachEnable.getId());
//                    BeanUtils.copyProperties(staffInfo,result);
//                    result.setPhoneNumber(staffInfo.getAccount().getPhone());
//                    listResult.add(result);
//                }
//            }
//            return listResult;
//
//
//        }catch (Exception ex){
//            ex.printStackTrace();
//            return null;
//        }
//    }


    // Hàm trả về tổng số sản phẩm
    public Integer algorithmToGetTotalPage(int idCompany, String keyword, String typeName) {
        try {
            Integer numberSearch = serviceComponentRepository.getAllServiceComponentWhereKeywordAndType(idCompany, keyword, typeName).size();
            return numberSearch;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // void auto generator ngày trong khoảng
    public static List<Date> getDatesBetweenUsingJava7(
            Date startDate, Date endDate) {
        List<Date> datesInRange = new ArrayList<>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);

        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(endDate);
        endCalendar.add(Calendar.DATE, 1);

        while (calendar.before(endCalendar)) {
            Date result = calendar.getTime();
            datesInRange.add(result);
            calendar.add(Calendar.DATE, 1);
        }
        return datesInRange;
    }


}
