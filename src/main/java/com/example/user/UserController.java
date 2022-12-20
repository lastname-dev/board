package com.example.user;

import com.example.user.UserDto.JoinRequestDto;
import com.example.user.UserDto.LoginRequestDto;
import com.example.user.UserDto.UpdateRequestDto;
import com.example.user.except.IncorrectPasswordException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


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
    public String login(@RequestBody LoginRequestDto loginRequestDto) throws IncorrectPasswordException {
        userService.login(loginRequestDto);

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
