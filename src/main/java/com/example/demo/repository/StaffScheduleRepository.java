package com.example.demo.repository;

import com.example.demo.dto.response.IntegerDTO;
import com.example.demo.entity.StaffSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Timer;

@Repository
public interface StaffScheduleRepository extends JpaRepository<StaffSchedule, Integer> {

    @Query(value = "SELECT sc.staffInfo.staffId FROM StaffSchedule sc " +
            "JOIN StaffInfo si on sc.staffInfo.staffId = si.staffId " +
            "WHERE sc.slot.id = :slotId AND sc.day = :day AND si.type.name = :typeName AND sc.isOff = false")
    List<Integer> getListStaffIdEnableWorkInASlot(@Param("slotId")Integer slotId,
                                                  @Param("day") Date day,
                                                  @Param("typeName") String typeName);

    @Query(value = "SELECT ss from StaffSchedule ss WHERE ss.staffInfo.staffId =:staffId")
    List<StaffSchedule> getListByStaffId(@Param("staffId") Integer staffId);

//    @Query(value = "SELECT sch FROM StaffSchedule sch WHERE sch.staffInfo.staffId = :staffId")
//    List<StaffSchedule> getListStaffScheduleByStaffId(@Param("staffId") Integer staffId);


//    @Query(value = "SELECT sch FROM StaffSchedule sch WHERE sch.slotDetail.id = :slotDetailId AND sch.staffInfo.staffId = :staffId")
//    StaffSchedule getStaffScheduleBySlotDetailIdAndStaffId(@Param("slotDetailId") Integer slotDetailId, @Param("staffId") Integer staffId);


    @Query(value = "SELECT ss from StaffSchedule ss WHERE ss.isOff = false AND ss.slot.id =:slotId AND ss.day =:day AND ss.staffInfo.type.name =:typeName")
    List<StaffSchedule> getAllStaffScheduleBySlotIdAndDay(@Param("slotId") Integer slotId, @Param("day") Date day, @Param("typeName") String typeName);

}
