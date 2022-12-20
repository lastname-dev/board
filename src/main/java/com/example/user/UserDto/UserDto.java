package com.example.user.UserDto;

import com.example.user.Gender;
import com.example.user.Role;
import com.example.user.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserDto {
    Integer id;
    String email;
    String password;
    String name;
    String call;
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
        this.call = user.getPhone();
        this.age = user.getAge();
        this.gender = user.getGender();
        this.role = user.getRole();
        this.recentLoginDate = user.getRecentLoginDate();
    }
}
