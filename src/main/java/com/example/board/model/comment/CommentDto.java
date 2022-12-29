package com.example.board.model.comment;

import com.example.board.model.post.Post;
import com.example.board.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;


@Getter
@Setter
@AllArgsConstructor
public class CommentDto {

        private Integer id;
        private String userEmail;
        private Integer postId;
        private String content;


}
