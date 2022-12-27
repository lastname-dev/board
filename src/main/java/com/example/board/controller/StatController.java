package com.example.board.controller;

import com.example.board.model.stats.StatDto;
import com.example.board.service.StatService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class StatController {


    private final StatService statService;
    @GetMapping("/stats")
    public ResponseEntity show(Model model){
        StatDto show = statService.show();

        return ResponseEntity.status(HttpStatus.OK).body(show);

    }
}
