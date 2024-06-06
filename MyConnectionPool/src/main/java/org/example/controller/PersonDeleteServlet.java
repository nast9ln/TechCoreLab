package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.example.constanst.WebConstant;
import org.example.dao.impl.PersonDaoImpl;
import org.example.dao.impl.RoleDaoImpl;
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

@WebServlet("/person/delete")
public class PersonDeleteServlet extends HttpServlet {
    private final PersonService personService;

    public PersonDeleteServlet() {
        this.personService = new PersonServiceImpl(new PersonDaoImpl(new RoleDaoImpl()),
                new PersonMapper(new RoleMapper(), new ObjectMapper()), new RoleDaoImpl());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String idParam = req.getParameter("id");
            if (idParam == null) {
                req.setAttribute(WebConstant.ERROR_ATTRIBUTE, "ID parameter is missing");
                req.getRequestDispatcher(JspHelper.getPath("error")).forward(req, resp);
                return;
            }

            Long id = Long.parseLong(idParam);
            personService.delete(id);
            req.getRequestDispatcher(JspHelper.getPath("deleteSuccess")).forward(req, resp);

        } catch (NumberFormatException e) {
            req.setAttribute(WebConstant.ERROR_ATTRIBUTE, "Invalid ID format");
            req.getRequestDispatcher(JspHelper.getPath("error")).forward(req, resp);
        } catch (Exception e) {
            req.setAttribute(WebConstant.ERROR_ATTRIBUTE, "Error processing request: " + e.getMessage());
            req.getRequestDispatcher(JspHelper.getPath("error")).forward(req, resp);
        }
    }
}