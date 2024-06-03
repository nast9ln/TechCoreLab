package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.example.constansts.WebConstant;
import org.example.dao.impl.PersonDaoImpl;
import org.example.dao.impl.RoleDaoImpl;
import org.example.dto.PersonDto;
import org.example.mapper.PersonMapper;
import org.example.mapper.RoleMapper;
import org.example.service.PersonService;
import org.example.service.impl.PersonServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/person")
@AllArgsConstructor
public class PersonController extends HttpServlet {
    private final PersonMapper personMapper = new PersonMapper(new RoleMapper(), new ObjectMapper());
    private final PersonService personService = new PersonServiceImpl(new PersonDaoImpl(new RoleDaoImpl()),
            new PersonMapper(new RoleMapper(), new ObjectMapper()), new RoleDaoImpl());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String strPersonId = req.getParameter("id");
        resp.setContentType(WebConstant.TEXT_HTML);
        PrintWriter printWriter = resp.getWriter();
        PersonDto personDto = personService.read(Long.parseLong(strPersonId));
        printWriter.write(personDto.getName());
        printWriter.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PersonDto personDto = personMapper.fromJson(req.getReader());
        PersonDto createdPerson = personService.create(personDto);
        resp.setContentType(WebConstant.APPLICATION_JSON);
        resp.getWriter().write(personMapper.toJson(createdPerson));
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
        try {
            PersonDto personDto = personMapper.fromJson(request.getReader());
            PersonDto updatedPerson = personService.update(personDto);
            response.setContentType(WebConstant.APPLICATION_JSON);
            response.getWriter().write(personMapper.toJson(updatedPerson));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

