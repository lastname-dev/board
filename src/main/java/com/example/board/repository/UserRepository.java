package com.example.board.repository;

import com.example.board.model.user.Gender;
import com.example.board.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);

    Long countByGender(Gender gender);

    @Query("select count(u) from User u WHERE u.age between :lowernum and :highernum")
    Long countByAgeRange(@Param("lowernum") Integer lowernum, @Param("highernum") Integer highernum);

//    @Query(value = "select count(u) from User u WHERE HOUR(u.recentLoginDate) between :lowernum and :highernum", nativeQuery = true)
    @Query(value = "select count(user0_.`user_id`) as col_0_0_  from `users` user0_  WHERE HOUR(user0_.`recent_login_date`) between :lowernum and :highernum", nativeQuery = true)
    Long countByTimeRange(@Param("lowernum") Integer lowernum, @Param("highernum") Integer highernum);
}


