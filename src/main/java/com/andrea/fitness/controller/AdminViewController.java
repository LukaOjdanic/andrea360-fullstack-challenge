package com.andrea.fitness.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminViewController {

    @GetMapping
    public String adminHomePage() {
        return "admin";
    }

    @GetMapping("/locations")
    public String locationsPage() {
        return "admin-locations";
    }

    @GetMapping("/employees")
    public String employeesPage() {
        return "admin-employees";
    }
}
