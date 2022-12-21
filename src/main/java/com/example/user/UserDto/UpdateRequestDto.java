package com.example.user.UserDto;

import com.example.user.Gender;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateRequestDto {
    String password;
    String name;
    String call;
    int age;
    Gender gender;
}
