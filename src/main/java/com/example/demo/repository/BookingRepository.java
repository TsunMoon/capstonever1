package com.example.demo.repository;

import com.example.demo.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    // Kiểm tra có bao nhiu nhân viên đang làm trong 1 slot
    @Query(value = "SELECT b.staffInfo.staffId FROM Booking b " +
            "JOIN StaffInfo si on si.staffId = b.staffInfo.staffId " +
            " WHERE b.slot.id =:slotId AND b.day =:day AND si.type.name = :typeName")
    List<Integer> getListStaffIdWorkingBySlotId(@Param("slotId") Integer slotId,
                                                @Param("day") Date day,
                                                @Param("typeName") String typeName);

    // Lấy tất cả booking có slot_id, day, serviceTypeName
    @Query(value = "SELECT b.id FROM Booking b " +
            "JOIN Service s on s.id = b.service.id " +
            " WHERE b.slot.id =:slotId AND b.day =:day ")
    List<Integer> getListBookingIdWorkingBySlotId(@Param("slotId") Integer slotId,
                                                @Param("day") Date day
                                               );



    @Query(value = "SELECT b FROM Booking b WHERE b.customerInfo.id = :customerId AND b.staffInfo IS NOT NULL")
    List<Booking> getBookingByCustomerId(@Param("customerId") Integer customerId);

    // Lấy tất cả các booking với customerId và day > currentday
    @Query(value = "SELECT b FROM Booking b WHERE b.customerInfo.account.id = :customerId AND b.day >= :currentDay")
    List<Booking> getAllBookingByCustomerIdAndLargerThanCurrentDay(@Param("customerId") Integer customerId, @Param("currentDay") Date currentDay);

    // Lấy id của booking của 1 customer có service là customized
    @Query(value = "SELECT b FROM Booking b WHERE b.customerInfo.account.id =:customerId AND b.service.isCustomized = true")
    List<Booking> getBookingIdWithCustomerIdHaveCustomizedService(@Param("customerId") Integer customerId);


    // Lấy tất cả các booking request có status là pending của 1 công ty
    @Query(value = "SELECT b from Booking b WHERE b.service.company.id =:companyId AND b.status ='pending'")
    List<Booking> getAllRequestBookingHaveStatusIsPending(@Param("companyId") Integer companyId);

    // Lấy tất cả booking request có status là assigned trong 1 slot vào 1 day
    @Query(value = "SELECT b from Booking b " +
            "WHERE b.status = 'assigned' AND b.slot.id =:slotId AND b.day =:day AND b.service.company.id =:companyId")
    List<Booking> getAllBookingHaveStatusAssignedInOneSlotAnfOneDayOfOneCompany(@Param("slotId") Integer slotId,
                                                                                @Param("day") Date day,
                                                                                @Param("companyId") Integer companyId);

}
