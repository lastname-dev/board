package com.example.user.UserDto;

import com.example.user.Gender;
import com.example.user.Role;
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
