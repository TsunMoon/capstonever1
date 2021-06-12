package com.example.demo.service.impl;

import com.example.demo.dto.response.CompanyDTO;
import com.example.demo.dto.response.CompanyOutput;
import com.example.demo.entity.Company;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.service.AdminService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {


    @Autowired
    CompanyRepository companyRepository;


    // 1. Lấy tất cả các company
    @Override
    public CompanyOutput getAllCompany(int page, int limit, String keyword, String sortBy) {
        try {

            CompanyOutput output = new CompanyOutput();
            output.setPage(page);
            Pageable pageable = null;

            switch(sortBy){
                case "none":
                    pageable = PageRequest.of(page -1,limit);
                    break;
                case "name_ascending":
                    pageable = PageRequest.of(page -1,limit, Sort.by("name").ascending());
                    break;
                case "name_descending":
                    pageable = PageRequest.of(page -1,limit, Sort.by("name").descending());
                    break;
                case "date_ascending":
                    pageable = PageRequest.of(page -1,limit, Sort.by("createAt").ascending());
                    break;
                case "date_descending":
                    pageable = PageRequest.of(page -1,limit, Sort.by("createAt").descending());
                    break;
            }
            if(pageable == null){
                return output;
            }

            List<Company> listAfterSearch = new ArrayList<>();
            List<Company> listAllCompanies = companyRepository.getAllCompanyByAdmin(keyword, pageable);
            for(Company eachCompany : listAllCompanies){
                if(eachCompany.getName().trim().contains(keyword.trim())){
                    listAfterSearch.add(eachCompany);
                }
            }

            List<CompanyDTO> listResult = new ArrayList<>();

            for(Company eachCompany : listAfterSearch){
                CompanyDTO dto = new CompanyDTO();
                BeanUtils.copyProperties(eachCompany, dto);
                dto.setListTypes(eachCompany.getListCompanyTypes().stream()
                                .map(eachCompanyType -> eachCompanyType.getType())
                                .collect(Collectors.toList())
                );

                listResult.add(dto);
            }
            output.setListCompanyDTOS(listResult);
            Integer numberSearch = listAllCompanies.size();

            output.setTotalPage((int) Math.ceil((double) numberSearch/limit));
            output.setTotalItem(numberSearch);


            return output;

        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }


}
