package com.example.user;

import com.example.user.UserDto.JoinRequestDto;
import com.example.user.UserDto.UpdateRequestDto;
import com.example.user.except.IncorrectPasswordException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@NoArgsConstructor
@Table(name = "USERS")
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    Integer id;

    String email;
    String password;
    String name;
    String phone;
    int age;

    @Enumerated(EnumType.STRING)
    Gender gender;

    @Enumerated(EnumType.STRING)
    Role role;

    @DateTimeFormat
    LocalDateTime recentLoginDate;


    //  @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "POST_ID")
//    List<Post> postList = new ArrayList<>();

    @Builder
    public User(JoinRequestDto joinRequestDto) {
        this.email = joinRequestDto.getEmail();
        this.name = joinRequestDto.getName();
        this.password = joinRequestDto.getPassword();
        this.phone = joinRequestDto.getCall();
        this.age = joinRequestDto.getAge();
        this.gender = joinRequestDto.getGender();

        this.role = Role.USER;

        renewalLoginDate();
    }

    public void addPost() {

    }

    public void deletePost() {

    }

    public void checkPassword(String password) throws IncorrectPasswordException {
        Optional.of(password).filter(pwd -> pwd.equals(this.password)).orElseThrow(() -> new IncorrectPasswordException());
    }

    public void changeInfo(UpdateRequestDto updateRequestDto) {
        this.password = updateRequestDto.getPassword();
        this.name = updateRequestDto.getName();
        this.phone = updateRequestDto.getCall();
        this.age = updateRequestDto.getAge();
        this.gender = updateRequestDto.getGender();
    }

    public void renewalLoginDate() {
        this.recentLoginDate = LocalDateTime.now();
    }
}
