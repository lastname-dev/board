package com.example.board.service;

import com.example.board.model.comment.Comment;
import com.example.board.model.comment.CommentDto;
import com.example.board.model.post.Post;
import com.example.board.model.post.PostDto;
import com.example.board.model.user.User;
import com.example.board.repository.CommentRepository;
import com.example.board.repository.PostRepository;
import com.example.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class CommentService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public void addComment(CommentDto commentDto) {

        User user = userRepository.findByEmail(commentDto.getUserEmail());
        Post post = postRepository.findById(commentDto.getPostId()).get();
        Comment comment = toEntity(commentDto, user, post);
        user.addComment(comment);
        post.addComment(comment);
    }

    public CommentDto commentView(Integer commentId) {
        Comment comment = commentRepository.findById(commentId).get();
        return comment.toDto();
    }

    public void modify(CommentDto commentDto) {

        User user = userRepository.findByEmail(commentDto.getUserEmail());
        Post post = postRepository.findById(commentDto.getPostId()).get();
        commentRepository.save(toEntity(commentDto, user, post));
    }


    private Comment toEntity(CommentDto commentDto, User user, Post post) {
        Comment comment = Comment.builder()
                .post(post)
                .user(user)
                .content(commentDto.getContent())
                .build();
        return comment;
    }

    public void delete(Integer id) {
        Comment comment = commentRepository.findById(id).get();
        User user = comment.getUser();
        Post post = comment.getPost();
        user.deleteComment(comment);
        post.deleteComment(comment);
        commentRepository.deleteById(id);
    }

}
