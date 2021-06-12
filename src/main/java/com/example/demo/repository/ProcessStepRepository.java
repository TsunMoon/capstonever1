package com.example.demo.repository;

import com.example.demo.entity.Process;
import com.example.demo.entity.ProcessStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessStepRepository extends JpaRepository<Process, Integer> {


    @Query(value = "SELECT ps FROM ProcessStep ps WHERE ps.process.id = :processId")
    List<ProcessStep> getAllByProcessId(@Param("processId") Integer processId);
}
