package com.example.board.model.comment;

import com.example.board.model.post.Post;
import com.example.board.model.user.User;
import lombok.*;
import org.springframework.stereotype.Service;


@Getter
@Setter
@NoArgsConstructor
public class CommentDto {

        private Integer id;
        private String userEmail;
        private Integer postId;
        private String content;
        @Builder
        public CommentDto(Integer id,String userEmail,Integer postId,String content){
                this.id = id;
                this.userEmail = userEmail;
                this.content = content;
                this.postId = postId;
        }

}
