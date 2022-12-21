package com.example.board.user;

import com.example.board.user.UserDto.JoinRequestDto;
import com.example.board.user.UserDto.LoginRequestDto;
import com.example.board.user.UserDto.SessionUserDto;
import com.example.board.user.UserDto.UpdateRequestDto;
import com.example.board.user.except.IncorrectPasswordException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping
    public String join(@RequestBody JoinRequestDto joinRequestDto) throws IllegalAccessException {
        userService.create(joinRequestDto);

        return "/"; // 메인화면
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequestDto loginRequestDto,
                        HttpServletRequest httpServletRequest) throws IncorrectPasswordException {
        userService.login(loginRequestDto);

        HttpSession httpSession = httpServletRequest.getSession();

        httpSession.setAttribute(loginRequestDto.getEmail(), new SessionUserDto(loginRequestDto.getEmail()));

        return "/";
    }

    @GetMapping("/logout")
    public String logout() {
        // 로그아웃
        return "/";
    }

    @PutMapping("/{id}")
    public void update(@RequestBody UpdateRequestDto updateRequestDto,
                       @PathVariable Integer id) {
        userService.update(id, updateRequestDto);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Integer id) {
        userService.delete(id);

        return "/";
    }
}
