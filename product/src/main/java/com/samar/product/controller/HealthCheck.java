package com.samar.product.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class HealthCheck {

    @GetMapping
    public String check(){
        return "Working";
    }
}
