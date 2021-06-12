package com.example.demo.repository;

import com.example.demo.dto.response.SlotEmptyDTO;
import com.example.demo.entity.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Integer> {


//    @Query(value = "SELECT new com.example.demo.dto.response.SlotEmptyDTO(sl.id , sl.time, slde.day, ssd.customerInfo.id, ssd.service.id, ssd.staffInfo.staffId, si.fullname)  FROM Slot sl " +
//            "JOIN SlotDetail slde on sl.id = slde.slot.id " +
//            "JOIN StaffSlotDetail ssd on ssd.slotDetail.id = slde.id " +
//            "JOIN StaffInfo si on si.staffId = ssd.staffInfo.staffId " +
//            "WHERE sl.company.id = :companyId AND slde.day = :currentDay AND ssd.service.id is NULL AND si.type.name = :typeName")
//    List<SlotEmptyDTO>getAllEmptySlot(@Param("companyId") int companyId,
//                                      @Param("currentDay")Date currentDay,
//                                      @Param("typeName") String typeName);

    @Query(value = "SELECT sl FROM Slot sl " +
            "WHERE sl.company.id = :companyId")
    List<Slot> getAllSlotByCompanyId (@Param("companyId") int companyId);
}
