package com.example.board.repository;

import com.example.board.model.post.Kind;
import com.example.board.model.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post,Integer> {
    //List<Post> findByKind(Kind kind);
    Page<Post> findByKind(Kind kind, Pageable pageable);
}
