package com.vandev.manage.controller;

import com.vandev.manage.pojo.Department;
import com.vandev.manage.pojo.Employee;
import com.vandev.manage.serviceImpl.DepartmentServiceImpl;
import com.vandev.manage.serviceImpl.EmployeeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentServiceImpl departmentServiceImpl;

    @Autowired
    private EmployeeServiceImpl employeeServiceImpl;

    @GetMapping
    public String listDepartments(Model model,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size) {
        Page<Department> departmentPage = departmentServiceImpl.findPaginated(page, size);
        model.addAttribute("departmentPage", departmentPage);
        List<Employee> employeesWithoutDepartment = employeeServiceImpl.getEmployeesWithoutDepartment();
        model.addAttribute("employeesWithoutDepartment", employeesWithoutDepartment);
        model.addAttribute("employeeServiceImpl", employeeServiceImpl);
        return "department/index";
    }
    @PostMapping("/create")
    public String createDepartment(@ModelAttribute Department department,
                                   @RequestParam(value = "employeeIds", required = false) List<Integer> employeeIds,
                                   RedirectAttributes redirectAttributes) {
        departmentServiceImpl.createDepartment(department);

        if (employeeIds != null && !employeeIds.isEmpty()) {
            List<Employee> employees = employeeServiceImpl.findAllById(employeeIds);
            employees.forEach(employee -> employee.setDepartment(department));
            employeeServiceImpl.saveAll(employees);
        }

        redirectAttributes.addFlashAttribute("successMessage", "Department created successfully!");

        return "redirect:/departments";
    }
    @GetMapping("/view/{id}")
    public String viewDepartment(@PathVariable("id") Integer id, Model model) {
        Department department = departmentServiceImpl.getDepartmentById(id);
        List<Employee> employees = employeeServiceImpl.getEmployeesByDepartment(department);

        model.addAttribute("department", department);
        model.addAttribute("employees", employees);

        return "department/detail";
    }

    @GetMapping("/edit/{id}")
    public String editDepartment(@PathVariable("id") Integer id, Model model) {
        Department department = departmentServiceImpl.getDepartmentById(id);
        List<Employee> employeesInDepartment = employeeServiceImpl.getEmployeesByDepartment(department);
        List<Employee> employeesWithoutDepartment = employeeServiceImpl.getEmployeesWithoutDepartment();

        model.addAttribute("department", department);
        model.addAttribute("employeesInDepartment", employeesInDepartment);
        model.addAttribute("employeesWithoutDepartment", employeesWithoutDepartment);

        return "department/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateDepartment(@PathVariable("id") Integer id,
                                   @ModelAttribute Department department,
                                   @RequestParam(value = "currentEmployeeIds", required = false) List<Integer> currentEmployeeIds,
                                   @RequestParam(value = "newEmployeeIds", required = false) List<Integer> newEmployeeIds,
                                   RedirectAttributes redirectAttributes) {
        Department existingDepartment = departmentServiceImpl.getDepartmentById(id);
        existingDepartment.setName(department.getName());
        departmentServiceImpl.updateDepartment(id,existingDepartment);

        List<Employee> currentEmployees = employeeServiceImpl.getEmployeesByDepartment(existingDepartment);
        for (Employee employee : currentEmployees) {
            if (currentEmployeeIds == null || !currentEmployeeIds.contains(employee.getId())) {
                employee.setDepartment(null);
            }
        }
        employeeServiceImpl.saveAll(currentEmployees);
        if (newEmployeeIds != null && !newEmployeeIds.isEmpty()) {
            List<Employee> newEmployees = employeeServiceImpl.findAllById(newEmployeeIds);
            newEmployees.forEach(employee -> employee.setDepartment(existingDepartment));
            employeeServiceImpl.saveAll(newEmployees);
        }

        redirectAttributes.addFlashAttribute("successMessage", "Department updated successfully!");
        return "department/index";
    }
    @PostMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        Department department = departmentServiceImpl.getDepartmentById(id);
        if (department != null) {
            List<Employee> employees = employeeServiceImpl.getEmployeesByDepartment(department);
            employees.forEach(employee -> employee.setDepartment(null));
            employeeServiceImpl.saveAll(employees);
            departmentServiceImpl.deleteDepartment(id);
        }
        return "redirect:/departments";
    }
}

