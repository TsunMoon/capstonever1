package com.example.demo.repository;

import com.example.demo.entity.CompanyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyTypeRepository extends JpaRepository<CompanyType, Integer> {

    @Query(value = "SELECT ct FROM CompanyType ct WHERE ct.company.id = :companyId AND ct.type.name = :typeName")
    CompanyType findByCompanyIdAndTypeName(@Param("companyId") Integer companyId, @Param("typeName") String typeName);
}
