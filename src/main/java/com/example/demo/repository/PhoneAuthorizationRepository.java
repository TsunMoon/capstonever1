package com.example.demo.repository;

import com.example.demo.entity.PhoneAuthorization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneAuthorizationRepository extends JpaRepository<PhoneAuthorization, Integer> {
    PhoneAuthorization findByPhone (@Param("phone") String phone);
}
