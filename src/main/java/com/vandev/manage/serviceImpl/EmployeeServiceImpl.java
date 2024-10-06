package com.vandev.manage.serviceImpl;

import com.vandev.manage.pojo.Department;
import com.vandev.manage.pojo.Employee;
import com.vandev.manage.pojo.UserSystem;
import com.vandev.manage.repository.EmployeeRepository;
import com.vandev.manage.service.EmployeeService;
import com.vandev.manage.service.UserService;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserService userService;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, UserService userService) {
        this.employeeRepository = employeeRepository;
        this.userService = userService;
    }

    @Override
    public Employee createEmployee(Employee employee) {
//        UserSystem currentUser = userService.getCurrentUser();

//        if (!currentUser.getRole().equals("admin")) {
//            throw new ValidationException("Only admins can create employees.");
//        }

        return employeeRepository.save(employee);
    }

    @Override
    public Employee updateEmployee(Integer employeeId, Employee employee) {
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
            updatedEmployee.setDepartment(employee.getDepartment());
            return employeeRepository.save(updatedEmployee);
        } else {
            throw new IllegalArgumentException("Employee not found.");
        }
    }

    @Override
    public void deleteEmployee(Integer employeeId) {
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
    public Page<Employee> getPagedEmployees(Pageable pageable) {
        Pageable fixedPageable = PageRequest.of(pageable.getPageNumber(), 8);
        return employeeRepository.findAll(fixedPageable);
    }
    @Override
    public List<Employee> getEmployeesWithoutDepartment() {
        return employeeRepository.findByDepartmentIsNull();
    }
    public List<Employee> findAllById(List<Integer> ids) {
        return employeeRepository.findAllById(ids);
    }

    // Thêm phương thức saveAll nếu chưa có
    public void saveAll(List<Employee> employees) {
        employeeRepository.saveAll(employees);
    }
    @Override
    public List<Employee> getEmployeesByDepartment(Department department) {
        return employeeRepository.findByDepartment(department);
    }
}
