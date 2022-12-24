package com.example.board.controller;

import com.example.board.model.user.userDto.JoinRequestDto;
import com.example.board.model.user.userDto.UpdateRequestDto;
import com.example.board.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RequestMapping("/users")
@Controller
public class UserController {

    private final UserService userService;

    @GetMapping("")
    public String join(){
        return "join";
    }
    @GetMapping("/loginForm")
    public String loginForm(){
        return "login";
    }

    @PostMapping
    public String join(JoinRequestDto joinRequestDto) throws IllegalAccessException {
        userService.create(joinRequestDto);

        return "redirect:/"; // 메인화면
    }

    @GetMapping("/login")
    public String login() {
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }

    @PutMapping("/{id}")
    public void update(UpdateRequestDto updateRequestDto,
                       @PathVariable Integer id) {
        userService.update(id, updateRequestDto);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        userService.delete(id);

        return "/";
    }
}
