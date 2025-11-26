package com.andrea.fitness.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
public class MemberViewController {

    @GetMapping
    public String memberHomePage() {
        return "member";
    }

    @GetMapping("/purchases")
    public String purchasesPage() {
        return "member-purchases";
    }

    @GetMapping("/reservations")
    public String reservationsPage() {
        return "member-reservations";
    }
}
