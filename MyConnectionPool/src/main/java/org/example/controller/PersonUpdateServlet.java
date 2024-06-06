package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.example.constanst.WebConstant;
import org.example.dao.impl.PersonDaoImpl;
import org.example.dao.impl.RoleDaoImpl;
import org.example.dto.PersonDto;
import org.example.dto.RoleDto;
import org.example.exception.EntityNotFoundException;
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

@WebServlet("/person/update")
public class PersonUpdateServlet extends HttpServlet {
    private final PersonService personService;

    public PersonUpdateServlet() {
        this.personService = new PersonServiceImpl(new PersonDaoImpl(new RoleDaoImpl()),
                new PersonMapper(new RoleMapper(), new ObjectMapper()), new RoleDaoImpl());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Long id = Long.parseLong(req.getParameter("id"));
            String name = req.getParameter("name");
            String login = req.getParameter("login");
            Long roleId = Long.parseLong(req.getParameter("role"));

            PersonDto existingPerson = personService.read(id);

            PersonDto personDto = PersonDto.builder()
                    .id(id)
                    .name(name)
                    .login(login)
                    .role(RoleDto.builder().id(roleId).build())
                    .registrationDate(existingPerson.getRegistrationDate())
                    .build();

            PersonDto updatedPerson = personService.update(personDto);
            req.setAttribute("person", updatedPerson);
            req.getRequestDispatcher(JspHelper.getPath("personDetail")).forward(req, resp);
        } catch (NumberFormatException e) {
            req.setAttribute(WebConstant.ERROR_ATTRIBUTE, "Invalid ID format");
            req.getRequestDispatcher(JspHelper.getPath("error")).forward(req, resp);
        } catch (EntityNotFoundException e) {
            req.setAttribute(WebConstant.ERROR_ATTRIBUTE, e.getMessage());
            req.getRequestDispatcher(JspHelper.getPath("error")).forward(req, resp);
        } catch (Exception e) {
            req.setAttribute(WebConstant.ERROR_ATTRIBUTE, "Error processing request: " + e.getMessage());
            req.getRequestDispatcher(JspHelper.getPath("error")).forward(req, resp);
        }
    }
}
