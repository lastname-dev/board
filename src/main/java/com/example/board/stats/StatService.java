package com.example.board.stats;

import com.example.board.post.PostRepository;
import com.example.board.user.Gender;
import com.example.board.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class StatService {

    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;

    public StatDto show(){
        StatDto statdto = new StatDto();
        statdto.setFemaleCount(userRepository.countByGender(Gender.FEMALE));
        statdto.setMaleCount(userRepository.countByGender(Gender.MALE));
        for (int i = 0; i <=9 ; i++) {
            statdto.addAgeCount(userRepository.countByAgeRange(i*10,i*10+9));
        }
        System.out.println("zzzzzzzz1");
        for (int i=0; i<=5; i++){
            statdto.addTimeCount(userRepository.countByTimeRange(i*4,i*4+3));
        }
        System.out.println("zzzzzzzz2");

        return statdto;
    }
}
