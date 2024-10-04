package com.vandev.manage.controller;

import com.vandev.manage.pojo.Employee;
import com.vandev.manage.serviceImpl.DepartmentServiceImpl;
import com.vandev.manage.serviceImpl.EmployeeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class CreateEmployeeController {
    private final DepartmentServiceImpl departmentServiceImpl;
    private final EmployeeServiceImpl employeeServiceImpl;

    @Autowired
    public CreateEmployeeController(DepartmentServiceImpl departmentServiceImpl, EmployeeServiceImpl employeeServiceImpl) {
        this.departmentServiceImpl = departmentServiceImpl;
        this.employeeServiceImpl = employeeServiceImpl;
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("departments", departmentServiceImpl.getAllDepartments()); // Assuming you have a service to get departments
        return "create-employee";
    }

    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute("employee") Employee employee,
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("departments", departmentServiceImpl.getAllDepartments());
            return "create-employee";
        }
        employeeServiceImpl.createEmployee(employee);
        return "redirect:/employees";
    }
}
