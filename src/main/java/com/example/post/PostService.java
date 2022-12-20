package com.example.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;


    // 글 작성 처리
    public void write(PostDto postdto){
        Post post = Post.builder()
                .id(postdto.getId())
                .title(postdto.getTitle())
                .kind(postdto.getKind())
                .content(postdto.getContent())
                .user_id(postdto.getUser_id())
                .build();
        postRepository.save(post);
    }

    //게시글 리스트 보기
    public List<PostDto> pageList(Kind kind){
        List<Post> posts = postRepository.findByKind(kind);
        return posts.stream().map(Post::toDto).collect(Collectors.toList());
    }

    //특정 게시물 보기
    public PostDto postView(Integer id) {
        Post post = postRepository.findById(id).get();
        return post.toDto();
    }
}
