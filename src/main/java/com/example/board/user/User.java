package com.example.board.user;

import com.example.board.post.Post;
import com.example.board.user.UserDto.JoinRequestDto;
import com.example.board.user.UserDto.UpdateRequestDto;
import com.example.board.user.except.IncorrectPasswordException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL)
    List<Post> postList = new ArrayList<>();

    @Builder
    public User(JoinRequestDto joinRequestDto) {
        this.email = joinRequestDto.getEmail();
        this.name = joinRequestDto.getName();
        this.password = joinRequestDto.getPassword();
        this.phone = joinRequestDto.getPhone();
        this.age = joinRequestDto.getAge();
        this.gender = joinRequestDto.getGender();

        this.role = Role.USER;

        renewalLoginDate();
    }

    public void addPost(Post post) {
        postList.add(post);
    }

    public void deletePost(Post post) {
        postList.remove(post);
    }

    public void checkPassword(String password) throws IncorrectPasswordException {
        Optional.of(password).filter(pwd -> pwd.equals(this.password)).orElseThrow(() -> new IncorrectPasswordException());
    }

    public void changeInfo(UpdateRequestDto updateRequestDto) {
        this.password = updateRequestDto.getPassword();
        this.name = updateRequestDto.getName();
        this.phone = updateRequestDto.getPhone();
        this.age = updateRequestDto.getAge();
        this.gender = updateRequestDto.getGender();
    }

    public void renewalLoginDate() {
        this.recentLoginDate = LocalDateTime.now();
    }
}
