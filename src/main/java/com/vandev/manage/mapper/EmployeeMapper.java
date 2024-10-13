package com.vandev.manage.mapper;

import com.vandev.manage.dto.EmployeeDTO;
import com.vandev.manage.pojo.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
@Mapper(componentModel = "spring")

public interface EmployeeMapper {
    EmployeeDTO toDTO(Employee employee);
    Employee toEntity(EmployeeDTO employeeDTO);
    void updateEntityFromDTO(EmployeeDTO employeeDTO, @MappingTarget Employee employee);

}
