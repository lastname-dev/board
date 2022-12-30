package com.example.board.model.post;

import com.example.board.model.comment.Comment;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDto {


    @Builder
    public PostDto(Integer id, String title, String content, String userEmail, Kind kind, LocalDateTime writtenDate, Integer likes, Integer unlikes, Long views,List<Comment> commentList) {
        this.title = title;
        this.Content = content;
        this.kind = kind;
        this.id = id;
        this.userEmail = userEmail;
        this.writtenDate = writtenDate;
        this.likes = likes;
        this.unlikes = unlikes;
        this.views = views;
        this.commentList = commentList;
    }

    private Integer id;
    private String title;
    private String Content;
    private String userEmail;

    @Enumerated(EnumType.STRING)
    private Kind kind;

    private LocalDateTime writtenDate;

    private Integer likes;
    private Integer unlikes;
    private Long views;
    List<Comment> commentList = new ArrayList<>();
}
