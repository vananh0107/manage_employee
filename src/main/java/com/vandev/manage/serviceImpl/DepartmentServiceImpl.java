package com.vandev.manage.serviceImpl;

import com.vandev.manage.pojo.Department;
import com.vandev.manage.repository.DepartmentRepository;
import com.vandev.manage.repository.EmployeeRepository;
import com.vandev.manage.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private  DepartmentRepository departmentRepository;

    @Override
    public Department createDepartment(Department department) {
        if (departmentRepository.findByName(department.getName()).isPresent()) {
            throw new IllegalArgumentException("Department name already exists.");
        }
        return departmentRepository.save(department);
    }

    @Override
    public Department updateDepartment(Integer departmentId, Department department) {
        Optional<Department> existingDepartment = departmentRepository.findById(departmentId);
        if (existingDepartment.isPresent()) {
            Department updatedDepartment = existingDepartment.get();
            updatedDepartment.setName(department.getName());
            return departmentRepository.save(updatedDepartment);
        } else {
            throw new IllegalArgumentException("Department not found.");
        }
    }

    @Override
    public void deleteDepartment(Integer departmentId) {
        if (!departmentRepository.existsById(departmentId)) {
            throw new IllegalArgumentException("Department not found.");
        }
        departmentRepository.deleteById(departmentId);
    }

    @Override
    public Department getDepartmentById(Integer departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new IllegalArgumentException("Department not found."));
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public Page<Department> findPaginated(int page, int size) {
        return departmentRepository.findAll(PageRequest.of(page, size));
    }
}
