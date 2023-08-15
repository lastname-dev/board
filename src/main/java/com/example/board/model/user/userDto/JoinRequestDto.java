package com.example.board.model.user.userDto;

import com.example.board.model.user.Gender;
import com.example.board.model.user.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinRequestDto {
    String email;
    String password;
    String name;
    String phone;
    int age;

    Gender gender;
    Role role;
}
