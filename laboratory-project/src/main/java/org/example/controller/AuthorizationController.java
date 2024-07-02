package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.PersonDto;
import org.example.service.impl.PersonServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthorizationController {
    private final PersonServiceImpl personService;

    @PostMapping("/register")
    public ResponseEntity<PersonDto> register(@RequestBody PersonDto personDto) {
        return ResponseEntity.ok(personService.create(personDto));
    }
}
