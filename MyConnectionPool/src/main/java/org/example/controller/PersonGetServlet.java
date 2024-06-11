package org.example.controller;

import org.example.constanst.WebConstant;
import org.example.dao.impl.PersonDaoImpl;
import org.example.dao.impl.RoleDaoImpl;
import org.example.dto.PersonDto;
import org.example.exception.EntityNotFoundException;
import org.example.mapper.PersonMapper;
import org.example.service.PersonService;
import org.example.service.impl.PersonServiceImpl;
import org.example.util.JspHelper;
import org.example.util.ServletUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Сервлет для получения данных о конкретном пользователе.
 */
@WebServlet("/person")
public class PersonGetServlet extends HttpServlet {
    private static final String PARAM_ID = WebConstant.IDENTIFICATION_FIELD.getValue();
    private static final String ATTRIBUTE_PERSON = "person";
    private static final String VIEW_PERSON_DETAIL = "personDetail";
    private static final String VIEW_ERROR = "error";
    private static final String ERROR_ID_MISSING = "ID parameter is missing";
    private static final String ERROR_INVALID_ID_FORMAT = "Invalid ID format";

    private final PersonService personService;

    public PersonGetServlet() {
        this.personService = new PersonServiceImpl(
                new PersonDaoImpl(new RoleDaoImpl()),
                PersonMapper.INSTANCE,
                new RoleDaoImpl()
        );
    }

    /**
     * Обрабатывает GET-запросы для получения детальной информации о пользователе по ID.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try {
            Long personId = getPersonId(req);
            PersonDto personDto = personService.read(personId);
            req.setAttribute(ATTRIBUTE_PERSON, personDto);
            req.getRequestDispatcher(JspHelper.getPath(VIEW_PERSON_DETAIL)).forward(req, resp);
        } catch (NumberFormatException e) {
            ServletUtil.handleError(req, resp, ERROR_INVALID_ID_FORMAT, VIEW_ERROR);
        } catch (EntityNotFoundException e) {
            ServletUtil.handleError(req, resp, e.getMessage(), VIEW_ERROR);
        } catch (Exception e) {
            ServletUtil.handleError(req, resp, "Error processing request: " + e.getMessage(), VIEW_ERROR);
        }
    }

    /**
     * Получает ID пользователя из параметра запроса.
     *
     * @param req HTTP-запрос.
     * @return ID пользователя.
     * @throws ServletException если ID отсутствует в запросе.
     */
    private Long getPersonId(HttpServletRequest req) throws ServletException {
        String strPersonId = req.getParameter(PARAM_ID);
        if (strPersonId == null) {
            throw new ServletException(ERROR_ID_MISSING);
        }
        return Long.parseLong(strPersonId);
    }
}
