package com.vandev.manage.service;

import com.vandev.manage.dto.DepartmentDTO;
import com.vandev.manage.pojo.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DepartmentService {
    DepartmentDTO createDepartment(DepartmentDTO departmentDTO);
    DepartmentDTO updateDepartment(Integer departmentId, DepartmentDTO departmentDTO);
    void deleteDepartment(Integer departmentId);
    DepartmentDTO getDepartmentById(Integer departmentId);
    List<DepartmentDTO> getAllDepartments();
    Page<DepartmentDTO> findPaginated(int page, int size);
    Page<DepartmentDTO> searchByName(String name, Pageable pageable);
}
