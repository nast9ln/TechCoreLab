package org.example.controller;

import org.example.constanst.WebConstant;
import org.example.dao.impl.PersonDaoImpl;
import org.example.dao.impl.RoleDaoImpl;
import org.example.dto.PersonDto;
import org.example.dto.RoleDto;
import org.example.enums.RoleEnum;
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
import java.time.Instant;

/**
 * Сервлет для создания нового пользователя.
 */
@WebServlet("/person/create")
public class PersonCreateServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(PersonCreateServlet.class);
    private static final String ATTRIBUTE_ROLES = "roles";
    private static final String ATTRIBUTE_PERSON = "person";
    private static final String PARAMETER_NAME = "name";
    private static final String PARAMETER_LOGIN = "login";
    private static final String PARAMETER_ROLE_ID = "roleId";
    private static final String PARAMETER_ROLE_NAME = "roleName";
    private static final String VIEW_PERSON_CREATE = "personCreate";
    private static final String VIEW_PERSON_DETAIL = "personDetail";
    private static final String VIEW_ERROR = "error";
    private static final String ERROR_INVALID_ROLE_NAME = "Invalid role name provided.";
    private static final String ERROR_PROCESSING_REQUEST = "Error processing request: ";

    private final PersonService personService;

    public PersonCreateServlet() {
        this.personService = new PersonServiceImpl(
                new PersonDaoImpl(new RoleDaoImpl()),
                PersonMapper.INSTANCE,
                new RoleDaoImpl()
        );
    }

    /**
     * Обрабатывает GET-запросы для отображения формы создания нового пользователя.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute(ATTRIBUTE_ROLES, RoleEnum.values());
        req.getRequestDispatcher(JspHelper.getPath(VIEW_PERSON_CREATE)).forward(req, resp);
    }

    /**
     * Обрабатывает POST-запросы для создания нового пользователя.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            PersonDto personDto = buildPersonDto(req);
            validateAndSetRole(req, personDto);

            PersonDto createdPerson = personService.create(personDto);
            req.setAttribute(ATTRIBUTE_PERSON, createdPerson);
            req.getRequestDispatcher(JspHelper.getPath(VIEW_PERSON_DETAIL)).forward(req, resp);
            logger.info("Person created successfully with ID: {}", createdPerson.getId());
        } catch (ServletException e) {
            logger.error("Role validation error: {}", e.getMessage());
            handleError(req, resp, e.getMessage());
        } catch (Exception e) {
            logger.error("Error processing person creation: {}", e.getMessage(), e);
            handleError(req, resp, ERROR_PROCESSING_REQUEST + e.getMessage());
        }
    }

    /**
     * Создаёт объект DTO пользователя из параметров запроса.
     */
    private PersonDto buildPersonDto(HttpServletRequest req) {
        String name = req.getParameter(PARAMETER_NAME);
        String login = req.getParameter(PARAMETER_LOGIN);
        Instant registrationDate = Instant.now();

        return PersonDto.builder()
                .name(name)
                .login(login)
                .registrationDate(registrationDate)
                .build();
    }

    /**
     * Валидирует и устанавливает роль для пользователя.
     */
    private void validateAndSetRole(HttpServletRequest req, PersonDto personDto) throws ServletException {
        String roleIdParam = req.getParameter(PARAMETER_ROLE_ID);
        String roleNameParam = req.getParameter(PARAMETER_ROLE_NAME);

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
                throw new ServletException(ERROR_INVALID_ROLE_NAME);
            }
        }
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
    private void handleError(HttpServletRequest req, HttpServletResponse resp, String errorMessage) throws ServletException, IOException {
        req.setAttribute(WebConstant.ERROR_ATTRIBUTE.getValue(), errorMessage);
        req.getRequestDispatcher(JspHelper.getPath(VIEW_ERROR)).forward(req, resp);
    }
}
