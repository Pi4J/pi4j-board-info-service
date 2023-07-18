package com.pi4j.boardinfoservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class RedirectController {

    @GetMapping("/web")
    public ModelAndView redirectWeb(ModelMap model) {
        return new ModelAndView("redirect:/", model);
    }

    @GetMapping("/web/")
    public ModelAndView redirectWebDirectory(ModelMap model) {
        return new ModelAndView("redirect:/", model);
    }
}
