package com.example.demo.repository;

import com.example.demo.entity.CompanyRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRequestRepository extends JpaRepository<CompanyRequest, Integer> {
    CompanyRequest findByPhone(String phone);
    CompanyRequest findByName(String name);
    CompanyRequest findByEmail(String email);
}
