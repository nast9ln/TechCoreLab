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
@AllArgsConstructor
public class PersonController extends HttpServlet {
    private final PersonMapper personMapper = new PersonMapper(new RoleMapper(), new ObjectMapper());
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            PersonDto personDto = personMapper.fromJson(req.getReader());
            PersonDto createdPerson = personService.create(personDto);

            resp.setContentType(WebConstant.APPLICATION_JSON);
            resp.getWriter().write(personMapper.toJson(createdPerson));
        } catch (Exception e) {
            handleError(resp, "Error processing request: " + e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
        try {
            PersonDto personDto = personMapper.fromJson(request.getReader());
            PersonDto updatedPerson = personService.update(personDto);

            response.setContentType(WebConstant.APPLICATION_JSON);
            response.getWriter().write(personMapper.toJson(updatedPerson));
        } catch (Exception e) {
            try {
                handleError(response, "Error processing request: " + e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String idParam = request.getParameter(WebConstant.IDENTIFICATION_FIELD);
            if (idParam == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("ID parameter is missing");
                return;
            }

            Long id = Long.parseLong(idParam);
            personService.delete(id);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (NumberFormatException e) {
            handleError(response, "Invalid Id format: " + e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            handleError(response, "Error processing request: " + e.getMessage(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void handleError(HttpServletResponse response, String message, int statusCode) throws IOException {
        response.setStatus(statusCode);
        response.setContentType(WebConstant.TEXT_HTML);
        response.getWriter().write(message);
    }
}

