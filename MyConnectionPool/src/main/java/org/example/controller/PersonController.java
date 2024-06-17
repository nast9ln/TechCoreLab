//package org.example.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.example.dto.PersonDto;
//import org.example.exception.EntityNotFoundException;
//import org.example.service.PersonService;
//import org.example.service.impl.PersonServiceImpl;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.ModelAndView;
//
//import javax.servlet.ServletException;
//
//@RestController
//@RequestMapping("/person")
//@RequiredArgsConstructor
//public class PersonController {
//
//    private final PersonServiceImpl personService;
//    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);
//
//
//    @GetMapping("/{id}")
//    public PersonDto read(@PathVariable Long id) {
//        logger.info("read");
//        return personService.read(id);
//    }
//}
