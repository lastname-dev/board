package com.example.user;

import com.example.user.UserDto.*;
import com.example.user.except.IncorrectPasswordException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    public void create(JoinRequestDto joinRequestDto) throws IllegalAccessException {

        if(userRepository.findByEmail(joinRequestDto.getEmail()) != null)
            throw new IllegalAccessException("이미 존재하는 아이디입니다");

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

    public void login(LoginRequestDto loginRequestDto) throws IncorrectPasswordException {
        User user = userRepository.findByEmail(loginRequestDto.getEmail());
        user.checkPassword(loginRequestDto.getPassword());

        user.renewalLoginDate();
    }

    public UserDto findEmail(String email) {
        User user = userRepository.findByEmail(email);

        UserDto userDto = UserDto.builder()
                .user(user)
                .build();

        return userDto;
    }
}
