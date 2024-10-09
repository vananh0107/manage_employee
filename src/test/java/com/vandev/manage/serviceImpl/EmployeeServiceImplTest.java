package com.vandev.manage.serviceImpl;

import com.vandev.manage.pojo.Department;
import com.vandev.manage.pojo.Employee;
import com.vandev.manage.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class EmployeeServiceImplTest {
    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createEmployee_Success() {
        Employee employee = new Employee();
        when(employeeRepository.save(employee)).thenReturn(employee);

        Employee result = employeeServiceImpl.createEmployee(employee);

        assertNotNull(result);
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void updateEmployee_EmployeeExists_Success() {
        Employee existingEmployee = new Employee();
        existingEmployee.setId(1);
        Employee updatedEmployee = new Employee();
        updatedEmployee.setFullName("Michel");

        when(employeeRepository.findById(1)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(existingEmployee)).thenReturn(existingEmployee);

        Employee result = employeeServiceImpl.updateEmployee(1, updatedEmployee);

        assertNotNull(result);
        assertEquals("Michel", existingEmployee.getFullName());
        verify(employeeRepository, times(1)).findById(1);
        verify(employeeRepository, times(1)).save(existingEmployee);
    }

    @Test
    void updateEmployee_EmployeeNotFound_ThrowsException() {
        Employee updatedEmployee = new Employee();
        when(employeeRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            employeeServiceImpl.updateEmployee(1, updatedEmployee);
        });

        assertEquals("Employee not found.", exception.getMessage());
        verify(employeeRepository, times(1)).findById(1);
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void deleteEmployee_EmployeeExists_Success() {
        when(employeeRepository.existsById(1)).thenReturn(true);

        employeeServiceImpl.deleteEmployee(1);

        verify(employeeRepository, times(1)).existsById(1);
        verify(employeeRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteEmployee_EmployeeNotFound_ThrowsException() {
        when(employeeRepository.existsById(1)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            employeeServiceImpl.deleteEmployee(1);
        });

        assertEquals("Employee not found.", exception.getMessage());
        verify(employeeRepository, times(1)).existsById(1);
        verify(employeeRepository, never()).deleteById(1);
    }

    @Test
    void getEmployeeById_EmployeeExists_Success() {
        Employee employee = new Employee();
        employee.setId(1);
        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));

        Employee result = employeeServiceImpl.getEmployeeById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(employeeRepository, times(1)).findById(1);
    }

    @Test
    void getEmployeeById_EmployeeNotFound_ThrowsException() {
        when(employeeRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            employeeServiceImpl.getEmployeeById(1);
        });

        assertEquals("Employee not found.", exception.getMessage());
        verify(employeeRepository, times(1)).findById(1);
    }

    @Test
    void getAllEmployees_Success() {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> result = employeeServiceImpl.getAllEmployees();

        assertEquals(2, result.size());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void getPagedEmployees_Success() {
        Page<Employee> page = new PageImpl<>(Arrays.asList(new Employee(), new Employee()));
        when(employeeRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Employee> result = employeeServiceImpl.getPagedEmployees(PageRequest.of(0, 8));

        assertEquals(2, result.getTotalElements());
        verify(employeeRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getEmployeesWithoutDepartment_Success() {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(employeeRepository.findByDepartmentIsNull()).thenReturn(employees);

        List<Employee> result = employeeServiceImpl.getEmployeesWithoutDepartment();

        assertEquals(2, result.size());
        verify(employeeRepository, times(1)).findByDepartmentIsNull();
    }

    @Test
    void getEmployeesByDepartment_Success() {
        Department department = new Department();
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(employeeRepository.findByDepartment(department)).thenReturn(employees);

        List<Employee> result = employeeServiceImpl.getEmployeesByDepartment(department);

        assertEquals(2, result.size());
        verify(employeeRepository, times(1)).findByDepartment(department);
    }

    @Test
    void getTop10Employees_Success() {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(employeeRepository.findTop10EmployeesByRewardPoints(any(Pageable.class))).thenReturn(employees);

        List<Employee> result = employeeServiceImpl.getTop10Employees();

        assertEquals(2, result.size());
        verify(employeeRepository, times(1)).findTop10EmployeesByRewardPoints(any(Pageable.class));
    }

    @Test
    void getEmployeesWithoutUser_Success() {
        List<Employee> employees = Arrays.asList(new Employee(), new Employee());
        when(employeeRepository.findByUserIsNull()).thenReturn(employees);

        List<Employee> result = employeeServiceImpl.getEmployeesWithoutUser();

        assertEquals(2, result.size());
        verify(employeeRepository, times(1)).findByUserIsNull();
    }

    @Test
    void searchByFullName_Success() {
        Page<Employee> page = new PageImpl<>(Arrays.asList(new Employee(), new Employee()));
        when(employeeRepository.findByFullNameContainingIgnoreCase(anyString(), any(Pageable.class))).thenReturn(page);

        Page<Employee> result = employeeServiceImpl.searchByFullName("John", PageRequest.of(0, 2));

        assertEquals(2, result.getTotalElements());
        verify(employeeRepository, times(1)).findByFullNameContainingIgnoreCase(anyString(), any(Pageable.class));
    }
}
