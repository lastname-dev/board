package com.example.board.model.post;

import com.example.board.model.BaseTimeEntity;
import com.example.board.model.comment.Comment;
import com.example.board.model.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor
@Table(name = "POSTS")
@Entity
public class Post extends BaseTimeEntity {

    @Column(name = "POST_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String title;
    String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    List<Comment> commentList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    Kind kind;

    Integer likes;
    Integer unlikes;
    Long views;

    @Builder
    public Post(Integer id, String title, String content, User user, Kind kind, String writtenDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.user = user;
        this.kind = kind;

        views = 0L;
        likes = 0;
        unlikes = 0;
    }

    public void addComment(Comment comment) {
        commentList.add(comment);
    }

    public void deleteComment(Comment comment) {
        commentList.remove(comment);
    }

    public void viewIncrease(){
        this.views++;
    }
    public void likeIncrease(){
        this.likes++;
    }
    public void unLikesIncrease(){
        this.unlikes++;
    }

    public PostDto toDto() {
        return new PostDto(id, title, content, user.getEmail(), kind, getWrittenDate(), likes, unlikes, views);
    }
}
