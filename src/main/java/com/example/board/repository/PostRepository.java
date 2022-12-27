package com.example.board.repository;

import com.example.board.model.post.Kind;
import com.example.board.model.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query(value = "select p from Post p where p.kind = :kind and p.title like %:keyword% order by p.writtenDate desc")
    Page<Post> findCustom(@Param("kind") Kind kind, @Param("keyword") String keyword, Pageable pageable);
}
