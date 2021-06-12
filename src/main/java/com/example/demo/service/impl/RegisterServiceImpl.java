package com.example.demo.service.impl;

import com.example.demo.dto.request.RegisterAccountDTO;
import com.example.demo.dto.request.VerificationCodeDTO;
import com.example.demo.entity.Account;
import com.example.demo.entity.CustomerInfo;
import com.example.demo.entity.PhoneAuthorization;
import com.example.demo.repository.*;
import com.example.demo.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Random;

@Service
public class RegisterServiceImpl implements RegisterService {

    private long CODE_EXPIRATION = 300000;
    private int min = 0;
    private int max = 9999;

    @Autowired
    PhoneAuthorizationRepository phoneAuthorizationRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    GenderRepository genderRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CustomerInfoRepository customerInfoRepository;

    @Override
    public String createPhoneAuthorizationCode(RegisterAccountDTO registerAccountDTO) {

        try{
            String phoneNumber = registerAccountDTO.getPhone();
            if (phoneNumber == "" || phoneNumber == null) {
                return null;
            }

            // 1. Check trùng trong bảng Account
            Account duplicateAccount = accountRepository.findAccountByPhone(phoneNumber);
            if(duplicateAccount != null){
                return "Duplicate Account";
            }

            // 2. Check trùng trong bảng phone_authorization
            PhoneAuthorization duplicatePhone = phoneAuthorizationRepository.findByPhone(phoneNumber);
            if (duplicatePhone == null) {
                PhoneAuthorization _phoneAuthorization = new PhoneAuthorization();
                _phoneAuthorization.setPhone(phoneNumber);
                return updateInfo(_phoneAuthorization, registerAccountDTO);

            }
            return updateInfo(duplicatePhone, registerAccountDTO);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public String verificationCode(VerificationCodeDTO verificationCodeDTO) {
        try{
            String phoneNumber = verificationCodeDTO.getPhoneNumber();
            String code = verificationCodeDTO.getCode();

            PhoneAuthorization duplicationPhone = phoneAuthorizationRepository.findByPhone(phoneNumber);

            if(duplicationPhone == null){
               return null;
            }

            if(code.trim().equals(duplicationPhone.getCode())){
                Timestamp currentTime = new Timestamp(System.currentTimeMillis());
                int result = currentTime.compareTo(duplicationPhone.getExpiredAt());
                if(result <= 0){
                    createNewAccountAndCustomerInfo(duplicationPhone);
                    phoneAuthorizationRepository.delete(duplicationPhone);
                    return "SUCCESS";
                }
            }
            return null;
        }catch (Exception ex){
            return null;
        }
    }

    private String createRandomCode(int min, int max){
        Random random = new Random();
        int randomNumber = random.ints(min, max).findFirst().getAsInt();
        if(randomNumber < 1000){
            return "0" + randomNumber;
        }
        return randomNumber + "";
    }

    private String convertPhone(String phoneNumber){
        String subPhone = phoneNumber.substring(1,phoneNumber.length()).trim();
        String messPhone = "+84" + subPhone;
        return messPhone;
    }

    private String updateInfo(PhoneAuthorization updatePhone, RegisterAccountDTO registerAccountDTO){
        updatePhone.setCreateAt(new Timestamp(System.currentTimeMillis()));
        updatePhone.setExpiredAt(new Timestamp(System.currentTimeMillis() + CODE_EXPIRATION));
        updatePhone.setCode(createRandomCode(min, max));
        updatePhone.setPassword(registerAccountDTO.getPassword());
        updatePhone.setFullname(registerAccountDTO.getFullname());

        phoneAuthorizationRepository.save(updatePhone);
       return updatePhone.getCode();
    }

    private void createNewAccountAndCustomerInfo(PhoneAuthorization phoneAuthorization){
        Account _account = new Account();
        _account.setPhone(phoneAuthorization.getPhone());
        _account.setPassword(phoneAuthorization.getPassword());
        _account.setRole(roleRepository.findByName("Customer"));

        Account saveAccount = accountRepository.save(_account);

        CustomerInfo _customerInfo = new CustomerInfo();
        _customerInfo.setAccount(saveAccount);
        _customerInfo.setDateOfBirth(null);
        _customerInfo.setDistrict(null);
        _customerInfo.setFullname(phoneAuthorization.getFullname());
        _customerInfo.setProvince(null);
        _customerInfo.setStreet(null);
        _customerInfo.setImage(null);
        _customerInfo.setGender(null);

       CustomerInfo saveCustomer = customerInfoRepository.save(_customerInfo);
    }

}
