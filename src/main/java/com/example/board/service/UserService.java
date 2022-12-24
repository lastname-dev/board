package com.example.board.service;

import com.example.board.model.user.userDto.JoinRequestDto;
import com.example.board.model.user.userDto.UpdateRequestDto;
import com.example.board.model.user.userDto.UserDto;
import com.example.board.repository.UserRepository;
import com.example.board.model.user.User;
import com.example.board.model.user.except.IncorrectPasswordException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void create(JoinRequestDto joinRequestDto) throws IllegalAccessException {

        if (userRepository.findByEmail(joinRequestDto.getEmail()) != null)
            throw new IllegalAccessException("이미 존재하는 아이디입니다");

        // 비밀번호 암호화
        String rawPassword = joinRequestDto.getPassword();
        joinRequestDto.setPassword(bCryptPasswordEncoder.encode(rawPassword));

        User user = User.builder()
                .joinRequestDto(joinRequestDto)
                .build();

        userRepository.save(user);
    }

    public void update(Integer id, UpdateRequestDto updateRequestDto) {
        User user = userRepository.getReferenceById(id);

        user.changeInfo(updateRequestDto);
    }

    public void delete(Integer id) {
        userRepository.deleteById(id);
    }

    public UserDto findEmail(String email) {
        User user = userRepository.findByEmail(email);

        UserDto userDto = UserDto.builder()
                .user(user)
                .build();

        return userDto;
    }
}
