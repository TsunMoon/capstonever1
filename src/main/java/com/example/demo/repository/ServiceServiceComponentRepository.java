package com.example.demo.repository;

import com.example.demo.entity.ServiceServiceComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceServiceComponentRepository extends JpaRepository<ServiceServiceComponent, Integer> {
    List<ServiceServiceComponent> findByServiceId(int idService);
}
