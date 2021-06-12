package com.example.demo.repository;

import com.example.demo.entity.PriceByDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceByDateRepository extends JpaRepository<PriceByDate, Integer> {
    List<PriceByDate> findByServiceComponentId(int id);

    @Query(value = "SELECT p from PriceByDate p where p.isUsed = true and p.serviceComponent.id = :idServiceStep")
    PriceByDate findByServiceComponentIdWhereIsUsedTrue(@Param("idServiceStep") int idServiceStep);
}
