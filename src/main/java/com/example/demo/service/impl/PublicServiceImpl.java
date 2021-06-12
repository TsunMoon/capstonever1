package com.example.demo.service.impl;

import com.example.demo.dto.request.CompanyRequestDTO;
import com.example.demo.dto.response.*;
import com.example.demo.entity.*;
import com.example.demo.mapper.ServiceMapper;
import com.example.demo.mapper.ServiceStepMapper;
import com.example.demo.repository.*;
import com.example.demo.service.PublicService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PublicServiceImpl implements PublicService {

    @Autowired
    CompanyRequestRepository companyRequestRepository;

    @Autowired
    CompanyRequestTypeRepository companyRequestTypeRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TypeRepository typeRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    CustomerInfoRepository customerInfoRepository;

    @Autowired
    GenderRepository genderRepository;

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    ServiceComponentRepository serviceComponentRepository;

    @Autowired
    ServiceServiceComponentRepository serviceServiceComponentRepository;

    @Autowired
    PriceByDateRepository priceByDateRepository;

    @Autowired
    SlotRepository slotRepository;


    @Autowired
    StaffScheduleRepository staffScheduleRepository;

    @Autowired
    BookingRepository bookingRepository;

    private ServiceMapper serviceMapper = Mappers.getMapper(ServiceMapper.class);
    private ServiceStepMapper serviceStepMapper = Mappers.getMapper(ServiceStepMapper.class);


    private final int min = 0;
    private final int max = 9999;
    // gmail hết hạn trong vòng 7 ngày
    private final long GMAIL_EXPIRATION = 604800000;

    // phone hết hạn trong vòng 5 phút
    private final long PHONE_EXPIRATION = 300000;

    // Cầu hình send phone messgae twilio
    private String ACCOUNT_SID = "AC09e33d1014c54d9dbc2f302ca6bc3ff1";
    private String AUTH_TOKEN = "870f44d37f7b46d1d04738a0e9aa3378";
    private String TRIAL_NUMBER = "+16033927171";

    @Override
    public String createNewCompanyRequest(CompanyRequestDTO dto) {
        try{
            String phoneNumber = dto.getPhone();

            // 1. Kiểm tra có trùng phone trong CompanyRequest

            CompanyRequest dupCompanyRequest = companyRequestRepository.findByPhone(phoneNumber);
            if(dupCompanyRequest != null) {
                return "Trùng số điện thoại với một yêu cầu tạo công ty khác";
            }

            Account dupAccount = accountRepository.findAccountByPhone(phoneNumber);
            if (dupAccount != null) {
                if (dupAccount.getRole().getName().equals("Admin") || dupAccount.getRole().getName().equals("Staff"))
                    return "Trùng số điện thoại với một tài khoản nhân viên khác";
            }

            CompanyRequest _companyRequest = new CompanyRequest();

            // 2. Kiểm tra name spa có trùng ko ?
            String name = dto.getName().trim();
            CompanyRequest dupNameCompanyRequest = companyRequestRepository.findByName(name);
            if (dupNameCompanyRequest != null) {
                return "Đã có tên công ty này trong danh sách tạo rồi!!!";
            }

            Company dupNameCompany = companyRepository.findByName(name);
            if (dupNameCompany != null) {
                return "Đã có tên công ty !!!";
            }

            // 3. Kiểm tra gmail có trùng ko
            String email = dto.getEmail().trim();
            CompanyRequest dupGmailCompanyRequest = companyRequestRepository.findByEmail(email);
            if(dupGmailCompanyRequest != null){
                return "Email này đã tồn tại trong danh sách đăng kí !!!";
            }

            Company dupGmailCompany = companyRepository.findByEmail(email);
            if(dupGmailCompany != null){
                return "Email này đã được đăng kí rồi !!!";
            }



            _companyRequest.setName(dto.getName());
            _companyRequest.setStreet(dto.getStreet());
            _companyRequest.setDistrict(dto.getDistrict());
            _companyRequest.setProvince(dto.getProvince());
            _companyRequest.setLocation(dto.getLocation());
            _companyRequest.setEmail(dto.getEmail());
            _companyRequest.setPhone(phoneNumber);
            _companyRequest.setCreateAt(new Date(System.currentTimeMillis()));
            _companyRequest.setEmailCode(createRandomCode(min, max));
            _companyRequest.setEmailExpiredAt(new Date(System.currentTimeMillis() + GMAIL_EXPIRATION));


            CompanyRequest saveCompanyRequest = companyRequestRepository.save(_companyRequest);

            List<String> listType = dto.getListType();
            // 4. LẤy list type và lưu vào bảng trung gian
            for (String eachStringType : listType) {
                CompanyRequestType _companyRequestType = new CompanyRequestType();
                _companyRequestType.setCompanyRequest(saveCompanyRequest);
                _companyRequestType.setType(typeRepository.findByName(eachStringType.trim()));
                companyRequestTypeRepository.save(_companyRequestType);
            }
            sendEmailToNotification(dto.getEmail(),saveCompanyRequest.getId(),saveCompanyRequest.getEmailCode());
            return "SUCCESS";

        }catch(Exception ex){
            return ex.getMessage();
        }
    }

    @Override
    public String verifiedEmailWithCode(int companyRequestId, String emailCode) {
        try {
            CompanyRequest dupCompanyRequest = companyRequestRepository.findById(companyRequestId).get();

            if(dupCompanyRequest == null){
                return "Không có yêu cầu đăng ký tồn tại !!!";
            }

            if(!dupCompanyRequest.getEmailCode().trim().equals(emailCode.trim())){
                return "Sai mã code rồi !!!";
            }

            Date currentDate = new Date(System.currentTimeMillis());
            int result = currentDate.compareTo(dupCompanyRequest.getEmailExpiredAt());
            if(result > 0){
                return "Mã xác thực gmail của bạn đã hết hạn";
            }

            // Qua hết tất cả các if là đã thành công =>  tạo mới phone code => gửi về sdt
            dupCompanyRequest.setPhoneCode(createRandomCode(min,max));
            dupCompanyRequest.setPhoneExpiredAt(new Date(System.currentTimeMillis() + PHONE_EXPIRATION));

            CompanyRequest saveCompanyRequest = companyRequestRepository.save(dupCompanyRequest);

           // sendPhoneMessage(saveCompanyRequest.getPhoneCode(),convertPhone(saveCompanyRequest.getPhone()));

            return "SUCCESS";

        }catch(Exception ex){
            ex.printStackTrace();
            return "Lỗi DB rồi !!!";
        }
    }

    // 3. Mobile: trả về list Service gợi ý
    @Override
    public List<ServiceDTO> mobileGetAllSuggestionService() {
        try {
            return serviceMapper.listEntityToListResponseDTO(serviceRepository.findAll());
        }catch (Exception ex){
            return null;
        }

    }

    // 4. Mobile: trả về Service Component theo từng Service
    @Override
    public List<ServiceComponentDTO> getServiceComponentByIdService(int idService) {
        try{
            List<ServiceServiceComponent> subListServiceServiceComponents = serviceServiceComponentRepository.findByServiceId(idService);
            List<ServiceComponentDTO> result = new ArrayList<>();

            for(ServiceServiceComponent eachSub : subListServiceServiceComponents){
                ServiceComponentDTO dto = new ServiceComponentDTO();
                BeanUtils.copyProperties(eachSub.getServiceComponent(), dto);
                dto.setPrice(priceByDateRepository.findByServiceComponentIdWhereIsUsedTrue(eachSub.getServiceComponent().getId()).getPrice());
                result.add(dto);
            }
        return result;

        }catch(Exception ex){
            return null;
        }
    }

    // 5. Mobile: search listCompany và listService theo keyword
    @Override
    public ListServiceAndCompanyDTO searchServiceAndCompanyByKeyword(String keyword) {
        try{
            List<com.example.demo.entity.Service> listServices = serviceRepository.findByNameContaining(keyword.trim());


            List<Company> listCompanies = companyRepository.findByNameContaining(keyword.trim());


            ListServiceAndCompanyDTO listServiceAndCompanyDTO = new ListServiceAndCompanyDTO();

            listServiceAndCompanyDTO.setListServiceDTOS(serviceMapper.listEntityToListResponseDTO(listServices));

            List<CompanyDTO> listCompanyDTOS = new ArrayList<>();
            for(Company eachCompany : listCompanies){
                CompanyDTO dto = new CompanyDTO();
                BeanUtils.copyProperties(eachCompany,dto);
                dto.setListTypes(eachCompany.getListCompanyTypes().stream()
                        .map(eachCompanyTypes -> eachCompanyTypes.getType())
                        .collect(Collectors.toList())
                );
                listCompanyDTOS.add(dto);
            }

            listServiceAndCompanyDTO.setListCompanyDTOS(listCompanyDTOS);

            return listServiceAndCompanyDTO;

        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }



    // 6. Mobile: Lấy lịch slot trống trong 1 ngày
    @Override
    public List<SlotEmptyResponse> getAllEmptySlot(int companyId, String typeName, java.sql.Date currentDay) {
        try {

            List<SlotEmptyResponse> listResponses = new ArrayList<>();

            // Lấy tất cả slot của 1 công ty
            List<Slot> listSlotByCompanyId = slotRepository.getAllSlotByCompanyId(companyId);

            for(Slot eachSlot : listSlotByCompanyId){
                SlotEmptyResponse emptyResponse = new SlotEmptyResponse();

                // cắt chuỗi ở đây
                String[]  timeName = eachSlot.getTime().toString().split(":");
                if(timeName[0].substring(0,1).equalsIgnoreCase("0")){
                    emptyResponse.setTimeName(timeName[0].substring(1) + "h");
                }else{
                    emptyResponse.setTimeName(timeName[0] + "h");
                }

                    // 1. Lấy tất cả nhân viên có thể làm trong 1 slot
                    List<Integer> listStaffIdEnable = staffScheduleRepository.getListStaffIdEnableWorkInASlot(eachSlot.getId(), currentDay, typeName);
                    emptyResponse.setSlotId(eachSlot.getId());
                    // 2. Lấy list staff đã có lịch
                    List<Integer> listStaffIdWorking = bookingRepository.getListStaffIdWorkingBySlotId(eachSlot.getId(), currentDay, typeName);

                    // 3. So sánh để kiểm tra empty
                    if(listStaffIdEnable.size() > listStaffIdWorking.size()){
                        emptyResponse.setEmpty(true);
                    }else{
                        emptyResponse.setEmpty(false);
                    }
                    listResponses.add(emptyResponse);
            }


            return listResponses;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }






    private void sendEmailToNotification(String toEmail, int companyRequestId, String gmailCode){
        SimpleMailMessage message = new SimpleMailMessage();

        String content = "http://localhost:8080/public/verified_gmail_with_code?company_req_id=" + companyRequestId + "&gmail_code="+gmailCode;
        message.setFrom("kimhexi1998@gmail.com");
        message.setTo(toEmail);
        message.setText(content);
        message.setSubject("Thư xác nhận của Spa Beauty And More");
        mailSender.send(message);
        System.out.println("Mail Send...");
    }

    private void sendPhoneMessage(String code, String phoneNumber){
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(new PhoneNumber(phoneNumber), new PhoneNumber(TRIAL_NUMBER),
                "Mã xác thực cho số điện thoại của bạn là: " + code ).create();
    }

    private String convertPhone(String phoneNumber){
        String subPhone =  phoneNumber.substring(1,phoneNumber.length()).trim();
        String messPhone = "+84" + subPhone;
        return  messPhone;
    }

    private String createRandomCode(int min, int max){
        Random random = new Random();
        int randomNumber = random.ints(min, max).findFirst().getAsInt();
        if(randomNumber < 1000){
            return "0" + randomNumber;
        }
        return randomNumber + "";
    }

}
