package com.example.demo.mapper;

import com.example.demo.dto.response.ServiceDTO;
import com.example.demo.entity.Service;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ServiceMapper {

    ServiceDTO entityToResponseDTO(Service service);

    List<ServiceDTO> listEntityToListResponseDTO(List<Service> listServices);

}
