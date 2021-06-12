package com.example.demo.mapper;

import com.example.demo.dto.response.GenderDTO;
import com.example.demo.entity.Gender;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GenderMapper {


    GenderDTO entityToResponseDTO(Gender gender);
}
