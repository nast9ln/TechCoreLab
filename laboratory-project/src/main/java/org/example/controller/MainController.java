package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Основной контроллер")
@RestController
public class MainController {
    @Operation(summary = "Домашняя страница")
    @GetMapping("/")
    public String homePage() {
        return "home";
    }
}
