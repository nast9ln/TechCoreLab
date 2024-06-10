package org.example.controller;

import org.example.constanst.WebConstant;
import org.example.dao.impl.PersonDaoImpl;
import org.example.dao.impl.RoleDaoImpl;
import org.example.mapper.PersonMapper;
import org.example.service.PersonService;
import org.example.service.impl.PersonServiceImpl;
import org.example.util.JspHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Сервлет, предназначенный для удаления пользователя.
 */
@WebServlet("/person/delete")
public class PersonDeleteServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(PersonDeleteServlet.class);
    private static final String PARAM_ID = "id";
    private static final String VIEW_DELETE_SUCCESS = "deleteSuccess";
    private static final String VIEW_ERROR = "error";
    private static final String ERROR_ID_MISSING = "ID parameter is missing";
    private static final String ERROR_INVALID_ID_FORMAT = "Invalid ID format";

    private final PersonService personService;

    public PersonDeleteServlet() {
        this.personService = new PersonServiceImpl(
                new PersonDaoImpl(new RoleDaoImpl()),
                PersonMapper.INSTANCE,
                new RoleDaoImpl()
        );
    }

    /**
     * Обрабатывает POST-запросы для удаления пользователя по идентификатору.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Long id = getPersonId(req);
            personService.delete(id);
            req.getRequestDispatcher(JspHelper.getPath(VIEW_DELETE_SUCCESS)).forward(req, resp);
            logger.info("Successfully deleted person with ID: {}", id);
        } catch (NumberFormatException e) {
            logger.error("Invalid ID format for deletion: {}", e.getMessage());
            handleException(req, resp, ERROR_INVALID_ID_FORMAT);
        } catch (Exception e) {
            logger.error("Error processing deletion request: {}", e.getMessage(), e);
            handleException(req, resp, "Error processing request: " + e.getMessage());
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
        String idParam = req.getParameter(PARAM_ID);
        if (idParam == null) {
            throw new ServletException(ERROR_ID_MISSING);
        }
        return Long.parseLong(idParam);
    }

    /**
     * Обрабатывает исключения и ошибки, перенаправляя на страницу ошибки.
     *
     * @param req HTTP-запрос.
     * @param resp HTTP-ответ.
     * @param errorMessage Сообщение об ошибке.
     * @throws ServletException в случае ошибок сервлета.
     * @throws IOException в случае ошибок ввода-вывода.
     */
    private void handleException(HttpServletRequest req, HttpServletResponse resp, String errorMessage) throws ServletException, IOException {
        req.setAttribute(WebConstant.ERROR_ATTRIBUTE.getValue(), errorMessage);
        req.getRequestDispatcher(JspHelper.getPath(VIEW_ERROR)).forward(req, resp);
    }
}
