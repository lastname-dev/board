package com.example.board.controller;

import com.example.board.config.auth.PrincipalDetails;
import com.example.board.model.post.Kind;
import com.example.board.model.post.PostDto;
import com.example.board.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final  PostService postService;

    @GetMapping("/board/{kind}")
    public ResponseEntity<List<PostDto>> viewBoard(
                            @PathVariable("kind") String kindStr,
                            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                            @RequestParam(value = "sort", defaultValue = "writtenDate") String sort,
                            @RequestParam(value = "keyword",defaultValue = "") String keyword) {

        Kind kind = Kind.valueOf(kindStr.toUpperCase());

        List<PostDto> posts = postService.pageList(kind, sort, keyword, pageable);

        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }


    @GetMapping("/posts/{postId}")
    public ResponseEntity read(Model model, @PathVariable Integer postId) {

        PostDto postDto = postService.postView(postId);
        return ResponseEntity.status(HttpStatus.OK).body(postDto);
    }

    @PostMapping("/posts")
    public ResponseEntity write(@RequestBody PostDto postdto, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        postdto.setUser_email(principalDetails.getUserEmail());
        postService.write(postdto);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @RequestMapping(value = "/posts/delete/{postId}", method = {RequestMethod.DELETE,RequestMethod.GET})
    public ResponseEntity delete(@PathVariable Integer postId) {

        postService.delete(postId);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity modify(@PathVariable Integer postId, PostDto postdto, Authentication authentication) {

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        if (principalDetails.getUserEmail() != postdto.getUser_email())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        PostDto postdtotemp = postService.postView(postId);
        postdtotemp.setTitle(postdto.getTitle());
        postdtotemp.setContent(postdto.getContent());
        postdtotemp.setKind(postdto.getKind());

        postService.modify(postdtotemp);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}


//DELIMITER $$
//
//CREATE PROCEDURE testDataInsert()
//BEGIN
//    DECLARE i INT DEFAULT 1;
//
//    WHILE i <= 120 DO
//        INSERT INTO board(title, content)
//          VALUES(concat('제목',i), concat('내용',i));
//        SET i = i + 1;
//    END WHILE;
//END$$
//DELIMITER $$