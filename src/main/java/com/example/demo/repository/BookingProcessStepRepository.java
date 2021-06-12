package com.example.demo.repository;

import com.example.demo.entity.BookingProcessStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface BookingProcessStepRepository extends JpaRepository<BookingProcessStep, Integer> {
    BookingProcessStep getByProcessStepIdAndBookingId(Integer processId, Integer bookingId);

    // 1. Lấy list booking_process_step có customerId và day > currentDay và status = unfinished

    @Query(value = "SELECT bps FROM BookingProcessStep bps " +
            "JOIN Booking b on b.id = bps.booking.id " +
            "WHERE b.customerInfo.account.id = :customerId AND b.day >= :currentDay ")
    List<BookingProcessStep> getAllByCustomerIdAndLargerThanCurrentDay(@Param("customerId") Integer customerId, @Param("currentDay")Date currentDay);


}
