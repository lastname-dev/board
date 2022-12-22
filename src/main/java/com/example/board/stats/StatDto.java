package com.example.board.stats;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class StatDto {
    Long MaleCount;
    Long FemaleCount;
    List<Long> Agecount;

    StatDto(){
        Agecount = new ArrayList<Long>();
    }

    public void addAgeCount(Long num){
        Agecount.add(num);
    }
}
