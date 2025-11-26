package com.andrea.fitness.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/employee")
public class EmployeeViewController {

    @GetMapping
    public String employeeHomePage() {
        return "employee";
    }

    @GetMapping("/members")
    public String membersPage() {
        return "employee-members";
    }

    @GetMapping("/services")
    public String servicesPage() {
        return "employee-services";
    }

    @GetMapping("/appointments")
    public String appointmentsPage() {
        return "employee-appointments";
    }
}
