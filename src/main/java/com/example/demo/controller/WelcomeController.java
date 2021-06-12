package com.example.demo.controller;



import com.example.demo.entity.*;
import com.example.demo.example.*;
import com.example.demo.global.ConstantVariable;
import com.example.demo.repository.*;
import com.example.demo.service.AuthenticateService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Date;
import java.util.List;


@RestController
@RequestMapping(value = ConstantVariable.WELCOME)
@CrossOrigin
public class WelcomeController {

    @Autowired
    AuthenticateService authenticateService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    GenderRepository genderRepository;

    @Autowired
    TypeRepository typeRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    CompanyTypeRepository companyTypeRepository;

    @Autowired
    ServiceRepository serviceRepository;


    @Autowired
    ServiceComponentRepository serviceComponentRepository;

    @Autowired
    ServiceServiceComponentRepository serviceServiceComponentRepository;

    @Autowired
    PriceByDateRepository priceByDateRepository;


    @Autowired
    CustomerInfoRepository customerInfoRepository;

    @Autowired
    StaffInfoRepository staffInfoRepository;


    @ApiOperation(value = "Hàm này để test thôi")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Thử nghiệm thành công"),
            @ApiResponse(code = 403, message = "Vậy là bạn chưa chơi đồ rồi", response = String.class),
    })
    @GetMapping("/admin")
    public Account testApi() {

        return accountRepository.findAccountByPhone("111");
    }

    // Tạo mới Role
    @PostMapping(value = "/create_new_role")
    public ResponseEntity<HttpStatus> createNewRole(@RequestBody RoleDTO roleDTO){
        try {
            Role _role = new Role();
            _role.setName(roleDTO.getName());
            roleRepository.save(_role);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/show_all_role")
    public ResponseEntity<List<Role>> showAllRole(){
        try{
            return new ResponseEntity<>(roleRepository.findAll(), HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Tạo mới gender
    @PostMapping(value = "/create_new_gender")
    public ResponseEntity<HttpStatus> createNewGender(@RequestBody GenderDTOEX genderDTOEX){
        try{
            Gender _gender = new Gender();
            _gender.setName(genderDTOEX.getName());
            genderRepository.save(_gender);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/show_all_gender")
    public ResponseEntity<List<Gender>> showAllGender(){
        try{
            return new ResponseEntity<>(genderRepository.findAll(), HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Tạo mới type
    @PostMapping(value = "/create_new_type")
    public ResponseEntity<HttpStatus> createNewType (@RequestBody TypeDTOEX typeDTO){
        try{
            Type _type = new Type();
            _type.setName(typeDTO.getName());
            typeRepository.save(_type);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/show_all_type")
    public ResponseEntity<List<Type>> showAllType(){
        try{
            return new ResponseEntity<>(typeRepository.findAll(), HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Tạo mới Company
    @PostMapping(value = "/create_new_company")
    public ResponseEntity<HttpStatus> createNewCompany (@RequestBody CompanyDTOEX companyDTO){
        try{
            Company _company = new Company();
            _company.setDistrict(companyDTO.getDistrict());
            _company.setLocation(companyDTO.getLocation());
            _company.setName(companyDTO.getName());
            _company.setEmail(companyDTO.getEmail());
            _company.setProvince(companyDTO.getProvince());
            _company.setStreet(companyDTO.getStreet());
            _company.setCreateAt(new Date(System.currentTimeMillis()));
            Company saveCompany = companyRepository.save(_company);

            // Tạo mới Company Type
            for(Integer typeId : companyDTO.getListTypeId()){
                CompanyType _companyType = new CompanyType();
                _companyType.setCompany(saveCompany);
                _companyType.setType(typeRepository.findById(typeId).get());

                companyTypeRepository.save(_companyType);
            }

            return new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/show_all_company")
    public ResponseEntity<List<Company>> showAllCompany(){
        try{
            return new ResponseEntity<>(companyRepository.findAll(), HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/show_all_company_type")
    public ResponseEntity<List<CompanyType>> showAllCompanyType(){
        try{
            return new ResponseEntity<>(companyTypeRepository.findAll(), HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    // Tạo mới Service
    @PostMapping(value = "/create_new_service")
    public ResponseEntity<HttpStatus> createNewService (@RequestBody ServiceDTOEX serviceDTOEX){
        try{
            Service _service = new Service();
            _service.setDescription(serviceDTOEX.getDescription());
            _service.setImage(serviceDTOEX.getImage());
            _service.setName(serviceDTOEX.getName());
            _service.setCompany(companyRepository.findById(serviceDTOEX.getCompanyId()).get());
            _service.setType(typeRepository.findById(serviceDTOEX.getTypeId()).get());
            Service saveService = serviceRepository.save(_service);

            // Thêm vào Service Service Step
            for(Integer serviceStepId : serviceDTOEX.getListServiceStepId()){
             ServiceServiceComponent _serviceServiceComponent = new ServiceServiceComponent();
             _serviceServiceComponent.setService(saveService);
             _serviceServiceComponent.setServiceComponent(serviceComponentRepository.findById(serviceStepId).get());
             serviceServiceComponentRepository.save(_serviceServiceComponent);
            }


            return new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/show_all_service")
    public ResponseEntity<List<Service>> showAllService(){
        try{
            return new ResponseEntity<>(serviceRepository.findAll(), HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    // Tạo mới Service Step
    @PostMapping(value = "/create_new_service_step")
    public ResponseEntity<HttpStatus> createNewServiceStep (@RequestBody ServiceStepDTOEX dto){
        try{
            ServiceComponent _serviceComponent = new ServiceComponent();
            _serviceComponent.setDescription(dto.getDescription());
            _serviceComponent.setImage(dto.getImage());
            _serviceComponent.setName(dto.getName());
            _serviceComponent.setCreateAt(new Date(System.currentTimeMillis()));
            _serviceComponent.setType(typeRepository.findById(dto.getTypeId()).get());
            _serviceComponent.setCompany(companyRepository.findById(dto.getCompanyId()).get());

           ServiceComponent saveServiceComponent = serviceComponentRepository.save(_serviceComponent);

            List<PriceByDate> listPriceByDates = priceByDateRepository.findByServiceComponentId(saveServiceComponent.getId());
            if(listPriceByDates.isEmpty()){
                // Tạo mới price_by_date
                PriceByDate _priceByDate = new PriceByDate();
                _priceByDate.setCreateAt(new Date((System.currentTimeMillis())));
                _priceByDate.setPrice(dto.getPrice());
                _priceByDate.setServiceComponent(saveServiceComponent);
                _priceByDate.setUsed(true);
                _priceByDate.setUpdateAt(null);

                priceByDateRepository.save(_priceByDate);
            }else{
                for(PriceByDate eachPrice : listPriceByDates){
                    if(eachPrice.getPrice() == dto.getPrice()){
                        eachPrice.setUsed(true);
                        eachPrice.setUpdateAt(new Date(System.currentTimeMillis()));
                        priceByDateRepository.save(eachPrice);
                    }
                    eachPrice.setUsed(false);
                }
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/show_all_service_step")
    public ResponseEntity<List<ServiceComponent>> showAllServiceStep(){
        try{
            return new ResponseEntity<>(serviceComponentRepository.findAll(), HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


//    // Tạo mới tài khoản admin staff
//    @PostMapping(value = "/create_new_admin_staff")
//    public ResponseEntity<HttpStatus> createNewAdminStaff (@Valid @RequestBody){
//        try{
//
//            return new ResponseEntity<>(HttpStatus.OK);
//        }catch(Exception ex){
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }


    // Tạo mới Account
    @PostMapping(value = "/create_new_account")
    public ResponseEntity<HttpStatus> createNewAccount (@RequestBody AccountDTO accountDTO){
        try{
            Account _account = new Account();
            _account.setPhone(accountDTO.getPhone());
            _account.setPassword(accountDTO.getPassword());
            _account.setRole(roleRepository.findById(accountDTO.getRoleId()).get());
            accountRepository.save(_account);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping(value = "/show_all_account")
    public ResponseEntity<List<Account>> showAllAccount(){
        try{
            return new ResponseEntity<>(accountRepository.findAll(), HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Xem tất cả Customer Info
    @GetMapping(value = "/show_all_customer_info")
    public ResponseEntity<List<CustomerInfo>> showAllCustomerInfo(){
        try{
            return new ResponseEntity<>(customerInfoRepository.findAll(), HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Xem tất cả Staff Info
    @GetMapping(value = "/show_all_staff_info")
    public ResponseEntity<List<StaffInfo>> showAllStaffInfo(){
        try{
            return new ResponseEntity<>(staffInfoRepository.findAll(), HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Tạo mới Manager cho Company
    @PostMapping(value = "/create_new_account_manager")
    public ResponseEntity<HttpStatus> createNewAccountManager (@RequestBody AccountManagerDTO dto){
        try{
            Account _account = new Account();
            StaffInfo _staffInfo = new StaffInfo();

            _account.setPassword(dto.getPassword());
            _account.setPhone(dto.getPhone());
            _account.setRole(roleRepository.findByName("Manager"));

            Account saveAccount = accountRepository.save(_account);

            _staffInfo.setAccount(saveAccount);
            _staffInfo.setDistrict(dto.getDistrict());
            _staffInfo.setFullname(dto.getFullname());
            _staffInfo.setImage(dto.getImage());
            _staffInfo.setProvince(dto.getProvince());
            _staffInfo.setStreet(dto.getStreet());
            _staffInfo.setCompany(companyRepository.findById(dto.getCompanyId()).get());
            _staffInfo.setGender(genderRepository.findById(dto.getGenderId()).get());

            StaffInfo saveStaffInfo = staffInfoRepository.save(_staffInfo);

            return new ResponseEntity<>(HttpStatus.OK);
        }catch(Exception ex){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Hàm tìm kiếm service theo tên
    @GetMapping(value = "/search_service_by_name")
    public ResponseEntity<List<Service>> searchServiceByName(@RequestParam("keyword") String keyword){
        try {
            return new ResponseEntity<>(serviceRepository.findByNameContaining(keyword.trim()), HttpStatus.OK);
        }catch(Exception ex){
            ex.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Autowired
    StaffScheduleRepository staffScheduleRepository;

    // Hàm test
//    @GetMapping(value = "/testabc")
//    public ResponseEntity<List<StaffSchedule>> testABC(@RequestParam("workDate") Date workDate,
//                                                       @RequestParam("time") Time time){
//        try {
//
//            return new ResponseEntity<>(staffScheduleRepository.getAllStaffScheduleWhere(workDate,time) , HttpStatus.OK);
////            Company company = companyRepository.findById(companyId).get();
////            WorkTimeDTO workTimeDTO = new WorkTimeDTO();
////            workTimeDTO.setWorkStart(company.getWorkStart());
////            workTimeDTO.setWorkEnd(company.getWorkEnd());
////            workTimeDTO.setBreakStart(company.getBreakStart());
////            workTimeDTO.setBreakEnd(company.getBreakEnd());
////            return  new ResponseEntity<>( workTimeDTO, HttpStatus.OK);
//        }catch(Exception ex){
//            ex.printStackTrace();
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }



}
