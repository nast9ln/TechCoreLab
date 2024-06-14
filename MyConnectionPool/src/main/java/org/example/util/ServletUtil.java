package org.example.util;

import org.example.constanst.WebConstant;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ServletUtil {

    /**
     * Обрабатывает исключения и ошибки, перенаправляя на страницу ошибки.
     *
     * @param req          HTTP-запрос.
     * @param resp         HTTP-ответ.
     * @param errorMessage Сообщение об ошибке.
     * @param viewError    Страница ошибки.
     * @throws ServletException в случае ошибок сервлета.
     * @throws IOException      в случае ошибок ввода-вывода.
     */
    public static void handleError(HttpServletRequest req, HttpServletResponse resp, String errorMessage, String viewError) throws ServletException, IOException {
        req.setAttribute(WebConstant.ERROR_ATTRIBUTE.getValue(), errorMessage);
        req.getRequestDispatcher(JspHelper.getPath(viewError)).forward(req, resp);
    }
}
