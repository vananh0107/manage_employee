package com.vandev.manage.mapper;

import com.vandev.manage.dto.EmployeeDTO;
import com.vandev.manage.pojo.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    @Mapping(source = "department.id", target = "departmentId")
    EmployeeDTO toDTO(Employee employee);
    @Mapping(source = "department.id", target = "departmentId")
    Employee toEntity(EmployeeDTO employeeDTO);
    void updateEntityFromDTO(EmployeeDTO employeeDTO, @MappingTarget Employee employee);

}
