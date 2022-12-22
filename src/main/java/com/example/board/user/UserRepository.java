package com.example.board.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);

    Long countByAge(Integer age);

    Long countByGender(Gender gender);

    Long countByRecentLoginDate(LocalDateTime dateTime);
}

