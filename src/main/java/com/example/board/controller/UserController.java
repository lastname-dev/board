package com.example.board.controller;

import com.example.board.model.user.userDto.JoinRequestDto;
import com.example.board.model.user.userDto.UpdateRequestDto;
import com.example.board.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {

    private final UserService userService;


    @PostMapping
    public ResponseEntity join(@RequestBody JoinRequestDto joinRequestDto) throws IllegalAccessException {

        try {
            userService.create(joinRequestDto);
        } catch (IllegalAccessException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
//
//    @GetMapping("/login")
//    public String login() {
//        return "redirect:/";
//    }
//
//    @GetMapping("/logout")
//    public String logout() {
//        return "redirect:/";
//    }

    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody UpdateRequestDto updateRequestDto,
                                 @PathVariable Integer id) {
        userService.update(id, updateRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Integer id) {
        userService.delete(id);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
