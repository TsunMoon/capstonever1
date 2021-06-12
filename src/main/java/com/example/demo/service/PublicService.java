package com.example.demo.service;


import com.example.demo.dto.request.CompanyRequestDTO;
import com.example.demo.dto.response.*;

import java.sql.Date;
import java.util.List;

public interface PublicService {
    String createNewCompanyRequest(CompanyRequestDTO dto);
    String verifiedEmailWithCode(int companyRequestId, String emailCode);

    // 3. Mobile: trả về list Service gợi ý
    List<ServiceDTO> mobileGetAllSuggestionService();

    // 4. Mobile: trả về Service Component theo từng Service
    List<ServiceComponentDTO> getServiceComponentByIdService(int idService);

    // 5. Mobile: search listCompany và listService theo keyword
    ListServiceAndCompanyDTO searchServiceAndCompanyByKeyword(String keyword);

    // 6. Mobile: Lấy lịch slot trống trong 1 ngày
    List<SlotEmptyResponse> getAllEmptySlot(int companyId, String typeName, Date currentDay);


}
