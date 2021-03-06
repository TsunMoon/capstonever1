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
    // gmail h???t h???n trong v??ng 7 ng??y
    private final long GMAIL_EXPIRATION = 604800000;

    // phone h???t h???n trong v??ng 5 ph??t
    private final long PHONE_EXPIRATION = 300000;

    // C???u h??nh send phone messgae twilio
    private String ACCOUNT_SID = "AC09e33d1014c54d9dbc2f302ca6bc3ff1";
    private String AUTH_TOKEN = "870f44d37f7b46d1d04738a0e9aa3378";
    private String TRIAL_NUMBER = "+16033927171";

    @Override
    public String createNewCompanyRequest(CompanyRequestDTO dto) {
        try{
            String phoneNumber = dto.getPhone();

            // 1. Ki???m tra c?? tr??ng phone trong CompanyRequest

            CompanyRequest dupCompanyRequest = companyRequestRepository.findByPhone(phoneNumber);
            if(dupCompanyRequest != null) {
                return "Tr??ng s??? ??i???n tho???i v???i m???t y??u c???u t???o c??ng ty kh??c";
            }

            Account dupAccount = accountRepository.findAccountByPhone(phoneNumber);
            if (dupAccount != null) {
                if (dupAccount.getRole().getName().equals("Admin") || dupAccount.getRole().getName().equals("Staff"))
                    return "Tr??ng s??? ??i???n tho???i v???i m???t t??i kho???n nh??n vi??n kh??c";
            }

            CompanyRequest _companyRequest = new CompanyRequest();

            // 2. Ki???m tra name spa c?? tr??ng ko ?
            String name = dto.getName().trim();
            CompanyRequest dupNameCompanyRequest = companyRequestRepository.findByName(name);
            if (dupNameCompanyRequest != null) {
                return "???? c?? t??n c??ng ty n??y trong danh s??ch t???o r???i!!!";
            }

            Company dupNameCompany = companyRepository.findByName(name);
            if (dupNameCompany != null) {
                return "???? c?? t??n c??ng ty !!!";
            }

            // 3. Ki???m tra gmail c?? tr??ng ko
            String email = dto.getEmail().trim();
            CompanyRequest dupGmailCompanyRequest = companyRequestRepository.findByEmail(email);
            if(dupGmailCompanyRequest != null){
                return "Email n??y ???? t???n t???i trong danh s??ch ????ng k?? !!!";
            }

            Company dupGmailCompany = companyRepository.findByEmail(email);
            if(dupGmailCompany != null){
                return "Email n??y ???? ???????c ????ng k?? r???i !!!";
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
            // 4. L???y list type v?? l??u v??o b???ng trung gian
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
                return "Kh??ng c?? y??u c???u ????ng k?? t???n t???i !!!";
            }

            if(!dupCompanyRequest.getEmailCode().trim().equals(emailCode.trim())){
                return "Sai m?? code r???i !!!";
            }

            Date currentDate = new Date(System.currentTimeMillis());
            int result = currentDate.compareTo(dupCompanyRequest.getEmailExpiredAt());
            if(result > 0){
                return "M?? x??c th???c gmail c???a b???n ???? h???t h???n";
            }

            // Qua h???t t???t c??? c??c if l?? ???? th??nh c??ng =>  t???o m???i phone code => g???i v??? sdt
            dupCompanyRequest.setPhoneCode(createRandomCode(min,max));
            dupCompanyRequest.setPhoneExpiredAt(new Date(System.currentTimeMillis() + PHONE_EXPIRATION));

            CompanyRequest saveCompanyRequest = companyRequestRepository.save(dupCompanyRequest);

           // sendPhoneMessage(saveCompanyRequest.getPhoneCode(),convertPhone(saveCompanyRequest.getPhone()));

            return "SUCCESS";

        }catch(Exception ex){
            ex.printStackTrace();
            return "L???i DB r???i !!!";
        }
    }

    // 3. Mobile: tr??? v??? list Service g???i ??
    @Override
    public List<ServiceDTO> mobileGetAllSuggestionService() {
        try {
            return serviceMapper.listEntityToListResponseDTO(serviceRepository.findAll());
        }catch (Exception ex){
            return null;
        }

    }

    // 4. Mobile: tr??? v??? Service Component theo t???ng Service
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

    // 5. Mobile: search listCompany v?? listService theo keyword
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



    // 6. Mobile: L???y l???ch slot tr???ng trong 1 ng??y
    @Override
    public List<SlotEmptyResponse> getAllEmptySlot(int companyId, String typeName, java.sql.Date currentDay) {
        try {

            List<SlotEmptyResponse> listResponses = new ArrayList<>();

            // L???y t???t c??? slot c???a 1 c??ng ty
            List<Slot> listSlotByCompanyId = slotRepository.getAllSlotByCompanyId(companyId);

            for(Slot eachSlot : listSlotByCompanyId){
                SlotEmptyResponse emptyResponse = new SlotEmptyResponse();

                // c???t chu???i ??? ????y
                String[]  timeName = eachSlot.getTime().toString().split(":");
                if(timeName[0].substring(0,1).equalsIgnoreCase("0")){
                    emptyResponse.setTimeName(timeName[0].substring(1) + "h");
                }else{
                    emptyResponse.setTimeName(timeName[0] + "h");
                }

                    // 1. L???y t???t c??? nh??n vi??n c?? th??? l??m trong 1 slot
                    List<Integer> listStaffIdEnable = staffScheduleRepository.getListStaffIdEnableWorkInASlot(eachSlot.getId(), currentDay, typeName);
                    emptyResponse.setSlotId(eachSlot.getId());
                    // 2. L???y list staff ???? c?? l???ch
                    List<Integer> listStaffIdWorking = bookingRepository.getListStaffIdWorkingBySlotId(eachSlot.getId(), currentDay, typeName);

                    // 3. So s??nh ????? ki???m tra empty
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
        message.setSubject("Th?? x??c nh???n c???a Spa Beauty And More");
        mailSender.send(message);
        System.out.println("Mail Send...");
    }

    private void sendPhoneMessage(String code, String phoneNumber){
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(new PhoneNumber(phoneNumber), new PhoneNumber(TRIAL_NUMBER),
                "M?? x??c th???c cho s??? ??i???n tho???i c???a b???n l??: " + code ).create();
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
