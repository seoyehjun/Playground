package com.example.playground.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/map")
public class MapController
{
    @GetMapping("/tomap")
    public String toMap(Model model)
    {
        return "thymeleaf/map";
    }
}
