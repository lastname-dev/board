package com.example.board.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);

    Long countByGender(Gender gender);

    @Query("select count(u) from User u WHERE u.age between :lowernum and :highernum")
    Long countByAgeRange(@Param("lowernum")Integer lowernum ,@Param("highernum")Integer highernum);

//    @Query("select count(u) from User u WHERE Date(u.recent_login_date) between :lowernum and :highernum")
//    Long countByTimeRange(@Param("lowernum")Integer lowernum ,@Param("highernum")Integer highernum);

}

