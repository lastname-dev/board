package com.example.user.UserDto;

import com.example.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Getter
public class SessionUserDto implements Serializable {

    String email;

    public SessionUserDto(String email) {
        this.email = email;
    }
}
