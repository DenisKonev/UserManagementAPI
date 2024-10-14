package io.github.deniskonev.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//TODO Сделать редизайн контроллеров, выделить общую логику и поместить ее в абстрактный класс, остальные контроллеры будут насследоваться от него
@Controller
public class RootController {

    @GetMapping("/")
    public String home() {
        return "redirect:/swagger-ui.html";
    }
}
