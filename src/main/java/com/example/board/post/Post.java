package com.example.board.post;

import com.example.user.User;
import lombok.*;

import javax.persistence.*;


@Getter
@NoArgsConstructor
@Table(name= "POSTS")
@Entity
public class Post extends BaseTimeEntity {

    @Column(name = "POST_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String title;
    String content;


    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "USER_ID")
    User user;

    @Enumerated(EnumType.STRING)
    Kind kind;

    @Builder
    public Post(Integer id, String title, String content, User user, Kind kind) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.user = user;
        this.kind = kind;
    }

    public PostDto toDto() {
        return new PostDto(id,title,content,user.getEmail(),kind,getWrittenDate());
    }
}
