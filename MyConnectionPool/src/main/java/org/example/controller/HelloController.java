package org.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.enterprise.inject.Model;

@RestController
public class HelloController {
    @GetMapping("/h")
    public int index() {
        return 2;
    }
}