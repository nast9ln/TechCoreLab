package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.example.constanst.WebConstant;
import org.example.dao.impl.PersonDaoImpl;
import org.example.dao.impl.RoleDaoImpl;
import org.example.dto.PersonDto;
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

@WebServlet("/person")
public class PersonGetServlet extends HttpServlet {
    private final PersonService personService = new PersonServiceImpl(new PersonDaoImpl(new RoleDaoImpl()),
            new PersonMapper(new RoleMapper(), new ObjectMapper()), new RoleDaoImpl());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try {
            String strPersonId = req.getParameter(WebConstant.IDENTIFICATION_FIELD);
            if (strPersonId == null) {
                req.setAttribute(WebConstant.ERROR_ATTRIBUTE, "ID parameter is missing");
                req.getRequestDispatcher(JspHelper.getPath("error")).forward(req, resp);
                return;
            }

            Long personId = Long.parseLong(strPersonId);
            PersonDto personDto = personService.read(personId);
            req.setAttribute("person", personDto);
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
