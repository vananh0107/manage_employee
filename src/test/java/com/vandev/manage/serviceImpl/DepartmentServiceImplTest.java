package com.vandev.manage.serviceImpl;

import com.vandev.manage.pojo.Department;
import com.vandev.manage.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DepartmentServiceImplTest {
    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentServiceImpl departmentServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createDepartment_DepartmentExists_ThrowsException() {
        Department department = new Department();
        department.setName("HR");
        when(departmentRepository.findByName("HR")).thenReturn(Optional.of(department));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            departmentServiceImpl.createDepartment(department);
        });

        assertEquals("Department name already exists.", exception.getMessage());

        verify(departmentRepository, times(1)).findByName("HR");
        verify(departmentRepository, never()).save(department);
    }

    @Test
    void createDepartment_DepartmentDoesNotExist_Success() {
        Department department = new Department();
        department.setName("Finance");
        when(departmentRepository.findByName("Finance")).thenReturn(Optional.empty());
        when(departmentRepository.save(department)).thenReturn(department);

        Department savedDepartment = departmentServiceImpl.createDepartment(department);

        assertNotNull(savedDepartment);
        assertEquals("Finance", savedDepartment.getName());

        verify(departmentRepository, times(1)).findByName("Finance");
        verify(departmentRepository, times(1)).save(department);
    }

    @Test
    void updateDepartment_DepartmentExists_Success() {
        Department existingDepartment = new Department();
        existingDepartment.setId(1);
        existingDepartment.setName("HR");

        Department updatedDepartment = new Department();
        updatedDepartment.setName("Updated HR");

        when(departmentRepository.findById(1)).thenReturn(Optional.of(existingDepartment));

        when(departmentRepository.save(any(Department.class))).thenReturn(existingDepartment);

        Department result = departmentServiceImpl.updateDepartment(1, updatedDepartment);
        assertNotNull(result);
        assertEquals("Updated HR", result.getName());

        verify(departmentRepository, times(1)).findById(1);
        verify(departmentRepository, times(1)).save(existingDepartment);
    }


    @Test
    void updateDepartment_DepartmentDoesNotExist_ThrowsException() {
        Department updatedDepartment = new Department();
        updatedDepartment.setName("Updated HR");

        when(departmentRepository.findById(1)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            departmentServiceImpl.updateDepartment(1, updatedDepartment);
        });

        assertEquals("Department not found.", exception.getMessage());

        verify(departmentRepository, times(1)).findById(1);
        verify(departmentRepository, never()).save(any(Department.class));
    }


    @Test
    void deleteDepartment_DepartmentExists_Success() {
        when(departmentRepository.existsById(1)).thenReturn(true);
        departmentServiceImpl.deleteDepartment(1);
        verify(departmentRepository, times(1)).existsById(1);
        verify(departmentRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteDepartment_DepartmentDoesNotExist_ThrowsException() {
        when(departmentRepository.existsById(1)).thenReturn(false);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            departmentServiceImpl.deleteDepartment(1);
        });
        assertEquals("Department not found.", exception.getMessage());
        verify(departmentRepository, times(1)).existsById(1);
        verify(departmentRepository, never()).deleteById(1);
    }

    @Test
    void getDepartmentById_DepartmentExists_Success() {
        Department department = new Department();
        department.setId(1);
        OngoingStubbing<Optional<Department>> optionalOngoingStubbing = when(departmentRepository.findById(1)).thenReturn(Optional.of(department));
        Department result = departmentServiceImpl.getDepartmentById(1);
        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(departmentRepository, times(1)).findById(1);
    }

    @Test
    void getDepartmentById_DepartmentDoesNotExist_ThrowsException() {
        when(departmentRepository.findById(1)).thenReturn(Optional.empty());
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            departmentServiceImpl.getDepartmentById(1);
        });
        assertEquals("Department not found.", exception.getMessage());
        verify(departmentRepository, times(1)).findById(1);
    }

    @Test
    void getAllDepartments_Success() {
        List<Department> departments = Arrays.asList(new Department(), new Department());
        when(departmentRepository.findAll()).thenReturn(departments);
        List<Department> result = departmentServiceImpl.getAllDepartments();
        assertEquals(2, result.size());
        verify(departmentRepository, times(1)).findAll();
    }

    @Test
    void findPaginated_Success() {
        Page<Department> page = new PageImpl<>(Arrays.asList(new Department(), new Department()));
        when(departmentRepository.findAll(any(Pageable.class))).thenReturn(page);
        Page<Department> result = departmentServiceImpl.findPaginated(0, 2);
        assertEquals(2, result.getTotalElements());
        verify(departmentRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void searchByName_Success() {
        Page<Department> page = new PageImpl<>(Arrays.asList(new Department(), new Department()));
        when(departmentRepository.findByNameContainingIgnoreCase(anyString(), any(Pageable.class))).thenReturn(page);
        Page<Department> result = departmentServiceImpl.searchByName("HR", PageRequest.of(0, 2));
        assertEquals(2, result.getTotalElements());
        verify(departmentRepository, times(1)).findByNameContainingIgnoreCase(anyString(), any(Pageable.class));
    }

}
