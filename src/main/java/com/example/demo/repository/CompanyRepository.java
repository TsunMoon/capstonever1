package com.example.demo.repository;

import com.example.demo.entity.Company;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
    Company findByName(String name);
    Company findByEmail(String email);
    List<Company> findByNameContaining(String keyword);

    @Query(value = "SELECT c from Company c WHERE c.name LIKE CONCAT('%',:keyword,'%')",
    countQuery = "SELECT c from Company c WHERE c.name LIKE CONCAT('%',:keyword,'%')")
    List<Company> getAllCompanyByAdmin(@Param("keyword") String keyword, Pageable pageable);
}
