package com.example.demo.repository;

import com.example.demo.dto.response.ServiceComponentDTO;
import com.example.demo.entity.ServiceComponent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceComponentRepository extends JpaRepository<ServiceComponent, Integer> {

    @Query(value = "SELECT new com.example.demo.dto.response.ServiceComponentDTO(sc.id, sc.createAt, sc.description,  sc.image, sc.isRemoved, sc.name, sc.company, sc.type, p.price ) " +
            "from ServiceComponent sc JOIN PriceByDate p on p.serviceComponent.id = sc.id " +
            "JOIN Type t on t.id = sc.type.id WHERE sc.company.id = :companyId " +
            "and sc.name LIKE CONCAT('%',:keyword,'%') and p.isUsed = true and t.name = :typeName and sc.isRemoved = false",
            countQuery ="SELECT new com.example.demo.dto.response.ServiceComponentDTO(sc.id, sc.createAt, sc.description,  sc.image, sc.isRemoved, sc.name, sc.company, sc.type, p.price ) " +
                    "from ServiceComponent sc JOIN PriceByDate p on p.serviceComponent.id = sc.id " +
                    "JOIN Type t on t.id = sc.type.id WHERE sc.company.id = :companyId " +
                    "and sc.name LIKE CONCAT('%',:keyword,'%') and p.isUsed = true and t.name = :typeName and sc.isRemoved = false"
    )
    List<ServiceComponentDTO> getAllServiceComponentByManager(@Param("keyword") String keyword, @Param("companyId") int companyId,@Param("typeName") String typeName, Pageable pageable);

    @Query(value = "SELECT sc from ServiceComponent sc WHERE sc.company.id = :companyId AND sc.name  LIKE CONCAT('%',:keyword,'%') AND sc.type.name = :typeName and sc.isRemoved = false")
    List<ServiceComponent> getAllServiceComponentWhereKeywordAndType(@Param("companyId") int idCompany , @Param("keyword") String keyword, @Param("typeName") String typeName);


}
