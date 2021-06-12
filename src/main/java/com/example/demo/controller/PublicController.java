package com.example.demo.controller;

import com.example.demo.dto.request.CompanyRequestDTO;
import com.example.demo.dto.response.*;
import com.example.demo.service.PublicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/public")
@CrossOrigin
public class PublicController {

    @Autowired
    PublicService publicService;

    // 1. Tạo 1 company request => code xác thực
    @PostMapping(value = "/create_new_company_request")
    public ResponseEntity<String> createNewCompanyRequest(@RequestBody CompanyRequestDTO companyRequestDTO){
        String message = publicService.createNewCompanyRequest(companyRequestDTO);
        if(message.equals("SUCCESS")){
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 2. Xác thực email
    @GetMapping(value = "/verified_email_with_code")
    public ResponseEntity<String> verifiedGmailWithCode(@RequestParam("company_req_id") int companyRequestID
                                                                    , @RequestParam("email_code") String emailCode){

        String message = publicService.verifiedEmailWithCode(companyRequestID, emailCode);
        if(message.equals("SUCCESS")){
            return new ResponseEntity<>("Thành công xác thực mail, còn phải xác thực phone nữa", HttpStatus.OK);
        }
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 3. Mobile: trả về list Service gợi ý
    @GetMapping(value = "/mobile_get_all_suggestion_service")
    public ResponseEntity<List<ServiceDTO>> mobileGetAllSuggestionService(){
        List<ServiceDTO> listResult = publicService.mobileGetAllSuggestionService();
        if(listResult == null){
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(listResult.isEmpty()){
            return new ResponseEntity<>(listResult,HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(listResult, HttpStatus.OK);
    }

    // 4. Mobile: trả về Service Step theo từng Service
    @GetMapping(value = "/mobile_get_service_component_by_id_service")
    public ResponseEntity<List<ServiceComponentDTO>> mobileGetServiceComponentByIdService(@RequestParam("idService") int idService){
        List<ServiceComponentDTO> listResult = publicService.getServiceComponentByIdService(idService);
        if(listResult == null){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(listResult.isEmpty()){
            return new ResponseEntity<>(listResult, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(listResult, HttpStatus.OK);
    }

    // 5. Mobile: search listCompany và listService theo keyword
    @PostMapping(value = "/search_service_and_company_by_keyword")
    public ResponseEntity<ListServiceAndCompanyDTO> search_service_and_company_by_keyword(@RequestParam("keyword") String keyword){
        ListServiceAndCompanyDTO listResult = publicService.searchServiceAndCompanyByKeyword(keyword);

        if(listResult == null){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(listResult, HttpStatus.OK);
    }


    // 6. Mobile: Lấy lịch slot trống trong 1 ngày
    @GetMapping(value = "/get_empty_slot")
    public ResponseEntity<List<SlotEmptyResponse>> getEmptySlot(
            @RequestParam(name = "company_id") int companyId,
            @RequestParam(name = "type") String typeName,
            @RequestParam(name = "current_day") Date currentDay
    ){
        List<SlotEmptyResponse> listResponse = publicService.getAllEmptySlot(companyId, typeName, currentDay);
        if(listResponse.isEmpty() || listResponse == null){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(listResponse, HttpStatus.OK);
    }


}
