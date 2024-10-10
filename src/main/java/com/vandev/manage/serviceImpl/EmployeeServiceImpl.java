package com.vandev.manage.serviceImpl;

import com.vandev.manage.pojo.Department;
import com.vandev.manage.pojo.Employee;
import com.vandev.manage.repository.EmployeeRepository;
import com.vandev.manage.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private  EmployeeRepository employeeRepository;

    @Override
    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public Employee updateEmployee(Integer employeeId, Employee employee) {
        return employeeRepository.findById(employeeId)
                .map(existingEmployee -> {
                    BeanUtils.copyProperties(employee, existingEmployee, "id", "user", "scores");
                    return employeeRepository.save(existingEmployee);
                })
                .orElseThrow(() -> new IllegalArgumentException("Employee not found."));
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
    @Override
    public List<Employee> findAllById(List<Integer> ids) {
        return employeeRepository.findAllById(ids);
    }

    @Override
    public void saveAll(List<Employee> employees) {
        employeeRepository.saveAll(employees);
    }

    @Override
    public List<Employee> getEmployeesByDepartment(Department department) {
        return employeeRepository.findByDepartment(department);
    }
    @Override
    public List<Employee> getTopEmployees() {
        Pageable top10 = PageRequest.of(0, 10);
        return employeeRepository.findTopEmployeesByRewardPoints(top10);
    }
    @Override
    public List<Employee> getEmployeesWithoutUser(){
        return employeeRepository.findByUserIsNull();
    }
    @Override
    public Page<Employee> searchByFullName(String fullName, Pageable pageable) {
        return employeeRepository.findByFullNameContainingIgnoreCase(fullName,pageable);
    }

}
