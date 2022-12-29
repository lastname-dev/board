package com.example.board.service;

import com.example.board.model.post.Kind;
import com.example.board.model.post.Post;
import com.example.board.model.post.PostDto;
import com.example.board.model.user.User;
import com.example.board.repository.PostRepository;
import com.example.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 글 작성 처리

    public void write(PostDto postdto) {
        User user = userRepository.findByEmail(postdto.getUserEmail());
        Post post = toEntity(postdto, user);
        user.addPost(post);
        postRepository.save(post);
    }

    public void modify(PostDto postdto) {

        User user = userRepository.findByEmail(postdto.getUserEmail());
        Post post = toEntity(postdto, user);
        post.setWrittenDate(postdto.getWrittenDate());
        postRepository.save(post);
    }

    //게시글 리스트 보기

    public List<PostDto> pageList(Kind kind, String sort, String keyword, Pageable pageable) {
        Page<Post> posts = postRepository.findCustom(kind,keyword, pageable);

        List<PostDto> postL = posts.stream().map(Post::toDto).collect(Collectors.toList());

        return postL;
    }

    //특정 게시물 보기

    public PostDto postView(Integer id) {
        Post post = postRepository.findById(id).get();
        post.viewIncrease();

        return post.toDto();
    }

    @Transactional
    public void delete(Integer id) {
        Post post = postRepository.findById(id).get();
        User user = post.getUser();
        user.deletePost(post);
        postRepository.deleteById(id);
    }

    @Transactional
    public void like(Integer id, Boolean value){

        Post post = postRepository.findById(id).get();

        // like
        if(value) {
            post.likeIncrease();
        }
        // unlike
        else{
            post.unLikesIncrease();
        }
    }

    private Post toEntity(PostDto postdto, User user) {
        Post post = Post.builder()
                .id(postdto.getId())
                .title(postdto.getTitle())
                .kind(postdto.getKind())
                .content(postdto.getContent())
                .user(user)
                .build();
        return post;
    }

}
