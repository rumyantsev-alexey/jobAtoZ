package ru.job4j.shortener.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {

    @GetMapping(value = "/")
    public String swaggerUi() {
        return "redirect:/swagger-ui.html";
    }


}
