package com.vandev.manage.serviceImpl;

import com.vandev.manage.dto.DepartmentDTO;
import com.vandev.manage.mapper.DepartmentMapper;
import com.vandev.manage.pojo.Department;
import com.vandev.manage.repository.DepartmentRepository;
import com.vandev.manage.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private  DepartmentRepository departmentRepository;
    @Autowired
    private DepartmentMapper departmentMapper;
    @Override
    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO) {
        if (departmentRepository.findByName(departmentDTO.getName()).isPresent()) {
            throw new IllegalArgumentException("Department name already exists.");
        }
        Department department = departmentMapper.toEntity(departmentDTO);
        return departmentMapper.toDTO(departmentRepository.save(department));
    }

    @Override
    public DepartmentDTO updateDepartment(Integer departmentId, DepartmentDTO departmentDTO) {
        Optional<Department> existingDepartment = departmentRepository.findById(departmentId);
        if (existingDepartment.isPresent()) {
            Department updatedDepartment = existingDepartment.get();
            updatedDepartment.setName(departmentDTO.getName());
            return departmentMapper.toDTO(departmentRepository.save(updatedDepartment));
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
    public DepartmentDTO getDepartmentById(Integer departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new IllegalArgumentException("Department not found."));
        return departmentMapper.toDTO(department);
    }
    @Override
    public List<DepartmentDTO> getAllDepartments() {

        return departmentRepository.findAll()
                .stream()
                .map(departmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<DepartmentDTO> findPaginated(int page, int size) {
        return departmentRepository.findAll(PageRequest.of(page, size))
                .map(departmentMapper::toDTO);
    }
    @Override
    public Page<DepartmentDTO> searchByName(String name, Pageable pageable) {
        return departmentRepository.findByNameContainingIgnoreCase(name, pageable)
                .map(departmentMapper::toDTO);
    }
}
