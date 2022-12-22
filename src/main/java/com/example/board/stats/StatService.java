package com.example.board.stats;

import com.example.board.post.PostRepository;
import com.example.board.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class StatService {

    PostRepository postRepository;
    UserRepository userRepository;

    public List<Integer> show(){
        List<Integer> countAge = new ArrayList<>();
//        userRepository.countByAge();
//        userRepository.countByGender();
    }
}
