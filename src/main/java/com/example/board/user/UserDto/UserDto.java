package com.example.board.user.UserDto;

import com.example.board.user.Gender;
import com.example.board.user.Role;
import com.example.board.user.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserDto {
    Integer id;
    String email;
    String password;
    String name;
    String phone;
    int age;
    Gender gender;
    Role role;
    LocalDateTime recentLoginDate;

    @Builder
    public UserDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.name = user.getName();
        this.phone = user.getPhone();
        this.age = user.getAge();
        this.gender = user.getGender();
        this.role = user.getRole();
        this.recentLoginDate = user.getRecentLoginDate();
    }
}
