package com.example.board.user;

import com.example.board.user.UserDto.JoinRequestDto;
import com.example.board.user.UserDto.LoginRequestDto;
import com.example.board.user.UserDto.SessionUserDto;
import com.example.board.user.UserDto.UpdateRequestDto;
import com.example.board.user.except.IncorrectPasswordException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


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
