package com.vandev.manage.serviceImpl;

import com.vandev.manage.dto.DepartmentDTO;
import com.vandev.manage.dto.EmployeeDTO;
import com.vandev.manage.mapper.DepartmentMapper;
import com.vandev.manage.mapper.EmployeeMapper; // Giả sử bạn đã có một lớp EmployeeMapper
import com.vandev.manage.pojo.Department;
import com.vandev.manage.pojo.Employee;
import com.vandev.manage.repository.EmployeeRepository;
import com.vandev.manage.service.EmployeeService;
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
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        Employee employee = employeeMapper.toEntity(employeeDTO);
        return employeeMapper.toDTO(employeeRepository.save(employee));
    }

    @Override
    public EmployeeDTO updateEmployee(Integer employeeId, EmployeeDTO employeeDTO) {
        return employeeRepository.findById(employeeId)
                .map(existingEmployee -> {
                    employeeMapper.updateEntityFromDTO(employeeDTO, existingEmployee);
                    return employeeMapper.toDTO(employeeRepository.save(existingEmployee));
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
    public EmployeeDTO getEmployeeById(Integer employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found."));
        return employeeMapper.toDTO(employee);
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(employeeMapper::toDTO)
                .toList();
    }

    @Override
    public Page<EmployeeDTO> getPagedEmployees(Pageable pageable) {
        Pageable fixedPageable = PageRequest.of(pageable.getPageNumber(), 8);
        return employeeRepository.findAll(fixedPageable)
                .map(employeeMapper::toDTO);
    }

    @Override
    public List<EmployeeDTO> getEmployeesWithoutDepartment() {
        return employeeRepository.findByDepartmentIsNull().stream()
                .map(employeeMapper::toDTO)
                .toList();
    }

    @Override
    public List<EmployeeDTO> findAllById(List<Integer> ids) {
        return employeeRepository.findAllById(ids).stream()
                .map(employeeMapper::toDTO)
                .toList();
    }

    @Override
    public void saveAll(List<EmployeeDTO> employeeDTOs) {
        List<Employee> employees = employeeDTOs.stream()
                .map(employeeMapper::toEntity)
                .toList();
        employeeRepository.saveAll(employees);
    }

    @Override
    public List<EmployeeDTO> getEmployeesByDepartment(DepartmentDTO departmentDTO) {
        Department department = departmentMapper.toEntity(departmentDTO);

        return employeeRepository.findByDepartment(department).stream()
                .map(employeeMapper::toDTO)
                .toList();
    }

    @Override
    public List<EmployeeDTO> getTopEmployees() {
        Pageable top10 = PageRequest.of(0, 10);
        return employeeRepository.findTopEmployeesByRewardPoints(top10).stream()
                .map(employeeMapper::toDTO)
                .toList();
    }

    @Override
    public List<EmployeeDTO> getEmployeesWithoutUser() {
        return employeeRepository.findByUserIsNull().stream()
                .map(employeeMapper::toDTO)
                .toList();
    }

    @Override
    public Page<EmployeeDTO> searchByFullName(String fullName, Pageable pageable) {
        return employeeRepository.findByFullNameContainingIgnoreCase(fullName, pageable)
                .map(employeeMapper::toDTO);
    }
}
