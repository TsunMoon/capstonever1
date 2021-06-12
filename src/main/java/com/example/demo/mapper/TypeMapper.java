package com.example.demo.mapper;

import com.example.demo.dto.response.TypeDTO;
import com.example.demo.entity.Type;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TypeMapper {
    TypeDTO entityToResponseDTO(Type type);
}
