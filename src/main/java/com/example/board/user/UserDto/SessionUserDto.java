package com.example.board.user.UserDto;

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
