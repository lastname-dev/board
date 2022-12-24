package com.example.board.service;

import com.example.board.repository.PostRepository;
import com.example.board.model.stats.StatDto;
import com.example.board.model.user.Gender;
import com.example.board.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        for (int i=0; i<=5; i++){
            statdto.addTimeCount(userRepository.countByTimeRange(i*4,i*4+3));
        }
        return statdto;
    }
}
