package com.example.demo.repository;

import com.example.demo.dto.response.StaffInfoDTO;
import com.example.demo.entity.StaffInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffInfoRepository extends JpaRepository<StaffInfo, Integer> {
    StaffInfo findByStaffId(int id);

    @Query(value = "SELECT new com.example.demo.dto.response.StaffInfoDTO(si.staffId, si.district, si.fullname, si.image, si.province, si.street,si.company , g, t, ac.phone) " +
            "FROM StaffInfo si " +
            "JOIN Gender g on g.id = si.gender.id " +
            "JOIN Type t on t.id = si.type.id " +
            "JOIN Account ac on ac.id = si.staffId " +
            "WHERE si.company.id = :companyId")
    List<StaffInfoDTO> getAllStaffInCompany(int companyId);
}
