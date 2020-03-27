package ru.job4j.todo;

import javax.servlet.*;
import java.io.IOException;

public class ToDoFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        servletResponse.setCharacterEncoding("UTF-8");
        servletResponse.setContentType("application/json");

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
