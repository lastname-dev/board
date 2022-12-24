package com.example.board.model.stats;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class StatDto {
    Long maleCount;
    Long femaleCount;
    List<Long> ageCount;
    List<Long> timeCount;

    public StatDto(){
        ageCount = new ArrayList<Long>();
        timeCount = new ArrayList<Long>();
    }


    public void addAgeCount(Long num){
        ageCount.add(num);
    }

    public void addTimeCount(Long num) {timeCount.add(num);}
}
