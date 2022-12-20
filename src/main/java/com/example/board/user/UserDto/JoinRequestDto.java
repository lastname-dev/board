package com.example.board.user.UserDto;

import com.example.board.user.Gender;
import com.example.board.user.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinRequestDto {
    String email;
    String password;
    String name;
    String call;
    int age;

    Gender gender;
    Role role;
}
