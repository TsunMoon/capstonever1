package com.example.demo.repository;

import com.example.demo.entity.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository  extends JpaRepository<Service,Integer> {
    // 1. Tìm kiếm list Service theo tên
    List<Service> findByNameContaining(String name);

    // 2. Tìm tất cả default service của 1 cong ty
    @Query(value = "SELECT s from Service s WHERE s.company.id =:companyId AND s.isCustomized = false AND s.isRemoved = false " +
            "AND s.name LIKE CONCAT('%',:keyword,'%') AND s.type.name =:typeName",
        countQuery = "SELECT s from Service s WHERE s.company.id =:companyId AND s.isCustomized = false AND s.isRemoved = false " +
                "AND s.name LIKE CONCAT('%',:keyword,'%') AND s.type.name =:typeName"
    )
    List<Service> getAllDefaultServiceByCompanyId(@Param("companyId") Integer companyId,
                                                  @Param("keyword") String keyword,
                                                  @Param("typeName") String typeName,
                                                  Pageable pageable);

    // 3. Tìm tất cả customized service của 1 công ty
    @Query(value = "SELECT s from Service s WHERE s.company.id =:companyId AND s.isCustomized = true AND s.isRemoved = false " +
            "AND s.name LIKE CONCAT('%',:keyword,'%') AND s.type.name =:typeName",
            countQuery = "SELECT s from Service s WHERE s.company.id =:companyId AND s.isCustomized = true AND s.isRemoved = false " +
                    "AND s.name LIKE CONCAT('%',:keyword,'%') AND s.type.name =:typeName"
    )
    List<Service> getAllCustomizedServiceByCompanyId(@Param("companyId") Integer companyId,
                                          @Param("keyword") String keyword,
                                          @Param("typeName") String typeName,
                                          Pageable pageable);

    @Query(value = "SELECT s from Service s WHERE s.isCustomized = false AND s.isRemoved = false AND s.id =:serviceId")
    Service getDefaultServiceByServiceId(@Param("serviceId") Integer serviceId);

}
