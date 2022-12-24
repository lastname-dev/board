package com.example.board.model.comment;

import com.example.board.model.BaseTimeEntity;
import com.example.board.model.post.Post;
import com.example.board.model.user.User;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Table(name = "COMMENTS")
@Entity
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    Integer id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    User user;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    Post post;

    String comment;
}
