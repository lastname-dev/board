package com.example.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Data
@Builder
@Getter
@AllArgsConstructor
public class PostDto {


    private Integer id;
    private String title;
    private String Content;
    private String user_id="aa";
    @Enumerated(EnumType.STRING)
    private Kind kind;
    private LocalDateTime writtenDate;

    public Post toEntity() {
        return new Post(id,title,Content,user_id,kind);
    }
}
