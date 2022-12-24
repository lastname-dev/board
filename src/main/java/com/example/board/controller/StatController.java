package com.example.board.controller;

import com.example.board.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatController {

    @Autowired
    StatService statService;
    @GetMapping("/stats")
    public String show(Model model){
        model.addAttribute("stat",statService.show());

        return "statview";
    }
}
