package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.constanst.WebConstant;
import org.example.dao.impl.PersonDaoImpl;
import org.example.dao.impl.RoleDaoImpl;
import org.example.dto.PersonDto;
import org.example.dto.RoleDto;
import org.example.enums.RoleEnum;
import org.example.mapper.PersonMapper;
import org.example.mapper.RoleMapper;
import org.example.service.PersonService;
import org.example.service.impl.PersonServiceImpl;
import org.example.util.JspHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;

@WebServlet("/person/create")
public class PersonCreateServlet extends HttpServlet {
    private final PersonService personService;

    public PersonCreateServlet() {
        this.personService = new PersonServiceImpl(
                new PersonDaoImpl(new RoleDaoImpl()),
                new PersonMapper(new RoleMapper(), new ObjectMapper()),
                new RoleDaoImpl()
        );
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("roles", RoleEnum.values());
        req.getRequestDispatcher(JspHelper.getPath("personCreate")).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String name = req.getParameter("name");
            String login = req.getParameter("login");
            Instant registrationDate = Instant.now();

            PersonDto personDto = new PersonDto();
            personDto.setName(name);
            personDto.setLogin(login);
            personDto.setRegistrationDate(registrationDate);

            String roleIdParam = req.getParameter("roleId");
            String roleNameParam = req.getParameter("roleName");
            if (roleIdParam != null && !roleIdParam.isBlank()) {
                Long roleId = Long.parseLong(roleIdParam);
                RoleDto roleDto = new RoleDto();
                roleDto.setId(roleId);
                personDto.setRole(roleDto);
            } else if (roleNameParam != null && !roleNameParam.isBlank()) {
                try {
                    RoleEnum roleEnum = RoleEnum.valueOf(roleNameParam.toUpperCase());
                    RoleDto roleDto = new RoleDto();
                    roleDto.setName(roleEnum);
                    personDto.setRole(roleDto);
                } catch (IllegalArgumentException e) {
                    throw new ServletException("Invalid role name provided.");
                }
            }

            PersonDto createdPerson = personService.create(personDto);

            req.setAttribute("person", createdPerson);
            req.getRequestDispatcher(JspHelper.getPath("personDetail")).forward(req, resp);
        } catch (Exception e) {
            req.setAttribute(WebConstant.ERROR_ATTRIBUTE, "Error processing request: " + e.getMessage());
            req.getRequestDispatcher(JspHelper.getPath("error")).forward(req, resp);
        }
    }
}
