package com.example.board.model.user.userDto;

import com.example.board.model.user.Gender;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateRequestDto {
    String password;
    String name;
    String phone;
    int age;
    Gender gender;
}
