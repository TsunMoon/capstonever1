package com.example.demo.repository;

import com.example.demo.entity.CompanyRequestType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRequestTypeRepository extends JpaRepository<CompanyRequestType, Integer> {
}
