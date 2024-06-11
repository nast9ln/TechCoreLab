package org.example.controller;

import org.example.dao.impl.PersonDaoImpl;
import org.example.dao.impl.RoleDaoImpl;
import org.example.dto.PersonDto;
import org.example.dto.RoleDto;
import org.example.exception.EntityNotFoundException;
import org.example.mapper.PersonMapper;
import org.example.service.PersonService;
import org.example.service.impl.PersonServiceImpl;
import org.example.util.JspHelper;
import org.example.util.ServletUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Сервлет для обработки запроса на обновление данных пользователя.
 */
@WebServlet("/person/update")
public class PersonUpdateServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(PersonUpdateServlet.class);
    private static final String PARAM_ID = "id";
    private static final String PARAM_NAME = "name";
    private static final String PARAM_LOGIN = "login";
    private static final String PARAM_ROLE = "role";
    private static final String ATTRIBUTE_PERSON = "person";
    private static final String VIEW_PERSON_DETAIL = "personDetail";
    private static final String VIEW_ERROR = "error";

    private final PersonService personService;

    public PersonUpdateServlet() {
        this.personService = new PersonServiceImpl(
                new PersonDaoImpl(new RoleDaoImpl()),
                PersonMapper.INSTANCE,
                new RoleDaoImpl()
        );
    }

    /**
     * Обрабатывает POST-запросы для обновления информации о пользователе.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            PersonDto personDto = buildPersonDto(req);
            PersonDto updatedPerson = personService.update(personDto);
            req.setAttribute(ATTRIBUTE_PERSON, updatedPerson);
            req.getRequestDispatcher(JspHelper.getPath(VIEW_PERSON_DETAIL)).forward(req, resp);
            logger.info("Successfully updated person with ID: {}", personDto.getId());
        } catch (NumberFormatException e) {
            logger.error("Invalid ID format: {}", e.getMessage());
            ServletUtil.handleError(req, resp, "Invalid ID format", VIEW_ERROR);
        } catch (EntityNotFoundException e) {
            logger.error("Person not found: {}", e.getMessage());
            ServletUtil.handleError(req, resp, e.getMessage(), VIEW_ERROR);
        } catch (Exception e) {
            logger.error("Error processing update request: {}", e.getMessage(), e);
            ServletUtil.handleError(req, resp, "Error processing request: " + e.getMessage(), VIEW_ERROR);
        }
    }

    /**
     * Строит объект DTO пользователя на основе данных запроса.
     *
     * @param req HTTP-запрос.
     * @return объект DTO пользователя.
     * @throws NumberFormatException если формат ID не является допустимым числом.
     */
    private PersonDto buildPersonDto(HttpServletRequest req) throws NumberFormatException {
        Long id = Long.parseLong(req.getParameter(PARAM_ID));
        String name = req.getParameter(PARAM_NAME);
        String login = req.getParameter(PARAM_LOGIN);
        Long roleId = Long.parseLong(req.getParameter(PARAM_ROLE));

        PersonDto existingPerson = personService.read(id);

        return PersonDto.builder()
                .id(id)
                .name(name)
                .login(login)
                .role(RoleDto.builder().id(roleId).build())
                .registrationDate(existingPerson.getRegistrationDate())
                .build();
    }
}
