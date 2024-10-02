package com.vandev.manage.serviceImpl;

import com.vandev.manage.pojo.Employee;
import com.vandev.manage.repository.EmployeeRepository;
import com.vandev.manage.service.EmployeeService;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee createEmployee(Employee employee) {
        // Kiểm tra vai trò
        if (!employee.getRole().equals("user") && !employee.getRole().equals("admin")) {
            throw new ValidationException("Role must be 'user' or 'admin'.");
        }
        return employeeRepository.save(employee);
    }

    @Override
    public Employee updateEmployee(Integer employeeId, Employee employee) {
        // Kiểm tra xem nhân viên có tồn tại hay không
        Optional<Employee> existingEmployee = employeeRepository.findById(employeeId);
        if (existingEmployee.isPresent()) {
            Employee updatedEmployee = existingEmployee.get();
            updatedEmployee.setFullName(employee.getFullName());
            updatedEmployee.setGender(employee.getGender());
            updatedEmployee.setImage(employee.getImage());
            updatedEmployee.setBirthDate(employee.getBirthDate());
            updatedEmployee.setSalary(employee.getSalary());
            updatedEmployee.setLevel(employee.getLevel());
            updatedEmployee.setEmail(employee.getEmail());
            updatedEmployee.setPhone(employee.getPhone());
            updatedEmployee.setNotes(employee.getNotes());
            updatedEmployee.setRole(employee.getRole());
            updatedEmployee.setDepartment(employee.getDepartment());
            return employeeRepository.save(updatedEmployee);
        } else {
            throw new IllegalArgumentException("Employee not found.");
        }
    }

    @Override
    public void deleteEmployee(Integer employeeId) {
        // Kiểm tra xem nhân viên có tồn tại hay không
        if (!employeeRepository.existsById(employeeId)) {
            throw new IllegalArgumentException("Employee not found.");
        }
        employeeRepository.deleteById(employeeId);
    }

    @Override
    public Employee getEmployeeById(Integer employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found."));
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public List<Employee> searchEmployees(String query) {
        // Tìm kiếm nhân viên theo tên hoặc email
        return employeeRepository.findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(query, query);
    }
}
