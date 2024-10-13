package com.vandev.manage.mapper;

import com.vandev.manage.dto.DepartmentDTO;
import com.vandev.manage.pojo.Department;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface DepartmentMapper {

    DepartmentDTO toDTO(Department department);

    Department toEntity(DepartmentDTO departmentDTO);
}
