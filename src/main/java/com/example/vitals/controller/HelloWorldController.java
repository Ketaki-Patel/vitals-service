package com.example.vitals.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class HelloWorldController {

    @GetMapping("/")
    public String hello() {
        return "Hello from Vitals Service!";
    }
}
