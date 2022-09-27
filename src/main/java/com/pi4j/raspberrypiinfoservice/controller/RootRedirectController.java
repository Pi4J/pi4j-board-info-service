package com.pi4j.raspberrypiinfoservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/")
public class RootRedirectController {

    @GetMapping
    public RedirectView redirectWithUsingRedirectView() {
        return new RedirectView("/web");
    }
}
