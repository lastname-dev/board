package com.example.board.model.comment;

import com.example.board.model.BaseTimeEntity;
import com.example.board.model.post.Post;
import com.example.board.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Table(name = "COMMENTS")
@Entity
@Getter
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    Post post;

    String content;

    @Builder
    public Comment(Integer id,User user, Post post, String content){
        this.id = id;
        this.user = user;
        this.post = post;
        this.content =content;
    }

    void change(String content){
        this.content = content;
    }

    public CommentDto toDto(){
        return(new CommentDto(id,user.getEmail(),post.getId(),content));
    }
}
