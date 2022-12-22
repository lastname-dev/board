package com.example.board.stats;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatController {

    @GetMapping("/stats")
    public String show(Model model){
        model.addAttribute("list",StatService.show());

        return "statview";
    }
}
