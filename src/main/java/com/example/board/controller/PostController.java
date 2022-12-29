package com.example.board.controller;

import com.example.board.config.auth.PrincipalDetails;
import com.example.board.model.comment.CommentDto;
import com.example.board.model.post.Kind;
import com.example.board.model.post.PostDto;
import com.example.board.repository.CommentRepository;
import com.example.board.service.CommentService;
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

    private final PostService postService;
    private final CommentService commentService;

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
    public ResponseEntity read(@PathVariable Integer postId) {

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

    @RequestMapping(value = "/posts/{postId}", method = {RequestMethod.DELETE})
    public ResponseEntity delete(@PathVariable Integer postId) {

        postService.delete(postId);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PutMapping("/posts/{postId}")
    public ResponseEntity modify(@PathVariable Integer postId, @RequestBody PostDto postdto, Authentication authentication) {

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();;
        if (!principalDetails.getUserEmail().equals(postdto.getUser_email())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        PostDto postdtotemp = postService.postView(postId);
        postdtotemp.setTitle(postdto.getTitle());
        postdtotemp.setContent(postdto.getContent());
        postdtotemp.setKind(postdto.getKind());

        postService.modify(postdtotemp);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping("/posts/{postId}/comment")
    public ResponseEntity addComment(@PathVariable Integer postId, @RequestBody CommentDto commentDto, Authentication authentication) {

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        commentDto.setUserEmail(principalDetails.getUserEmail());
        commentDto.setPostId(postId);

        commentService.addComment(commentDto);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
    @PutMapping("/posts/{postid}/comment/{commentid}")
    public ResponseEntity modifyComment(@PathVariable Integer postId,@PathVariable Integer commentid, @RequestBody CommentDto commentDto,Authentication authentication){

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();;

        //본인이 쓴 댓글이 아니면
        if (!principalDetails.getUserEmail().equals(commentDto.getUserEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        CommentDto commentDtoTemp = commentService.commentView(commentid);
        commentDtoTemp.setContent(commentDto.getContent());
        commentService.modify(commentDtoTemp);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
    @DeleteMapping("/posts/{postid}/comment/{commentid}")
    public ResponseEntity deleteComment(@PathVariable Integer postId,@PathVariable Integer commentid, Authentication authentication){

        commentService.delete(commentid);

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