package com.vandev.manage.serviceImpl;

import com.vandev.manage.pojo.Department;
import com.vandev.manage.repository.DepartmentRepository;
import com.vandev.manage.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Department createDepartment(Department department) {
        // Kiểm tra nếu tên phòng ban đã tồn tại hay chưa
        if (departmentRepository.findByName(department.getName()).isPresent()) {
            throw new IllegalArgumentException("Department name already exists.");
        }
        return departmentRepository.save(department);
    }

    @Override
    public Department updateDepartment(Integer departmentId, Department department) {
        // Kiểm tra xem phòng ban có tồn tại hay không
        Optional<Department> existingDepartment = departmentRepository.findById(departmentId);
        if (existingDepartment.isPresent()) {
            Department updatedDepartment = existingDepartment.get();
            updatedDepartment.setName(department.getName()); // Cập nhật tên phòng ban
            return departmentRepository.save(updatedDepartment);
        } else {
            throw new IllegalArgumentException("Department not found.");
        }
    }

    @Override
    public void deleteDepartment(Integer departmentId) {
        // Kiểm tra xem phòng ban có tồn tại hay không
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
}
