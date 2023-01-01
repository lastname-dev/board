package com.example.board.controller;

import com.example.board.config.auth.PrincipalDetails;
import com.example.board.config.resolver.UserEmail;
import com.example.board.model.comment.CommentDto;
import com.example.board.model.post.Kind;
import com.example.board.model.post.PostDto;
import com.example.board.service.CommentService;
import com.example.board.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
                            @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 3) Pageable pageable,
                            @RequestParam(value = "sort", defaultValue = "recent") String sort,
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

    //인터셉터로 막기
    @PostMapping("/posts")
    public ResponseEntity write(@RequestBody PostDto postdto,
                                @UserEmail String userEmail) {
        postdto.setUserEmail(userEmail);
        postService.write(postdto);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
    @RequestMapping(value = "/posts/{postId}", method = {RequestMethod.DELETE})
    public ResponseEntity delete(@PathVariable Integer postId) {

        postService.delete(postId);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PreAuthorize("#postdto.userEmail==principal.username")
    @PutMapping("/posts/{postId}")
    public ResponseEntity modify(@PathVariable Integer postId,
                                 @RequestBody PostDto postdto) {

        PostDto postdtotemp = postService.postView(postId);
        postdtotemp.setTitle(postdto.getTitle());
        postdtotemp.setContent(postdto.getContent());
        postdtotemp.setKind(postdto.getKind());

        postService.modify(postdtotemp);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PutMapping("posts/{postId}/like")
    public ResponseEntity like(@PathVariable("postId") Integer id,
                               @RequestParam(value = "value") Boolean value) {
        postService.like(id, value);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
    @PostMapping("/posts/{postId}/comment")
    public ResponseEntity writeComment(@PathVariable Integer postId,
                                       @RequestBody CommentDto commentDto,
                                       @UserEmail String userEmail) {

        commentDto.setUserEmail(userEmail);
        commentDto.setPostId(postId);

        commentService.addComment(commentDto);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PreAuthorize("#commentDto.userEmail==principal.username")
    @PutMapping("/posts/{postId}/comment/{commentId}")
    public ResponseEntity modifyComment(@PathVariable Integer postId,
                                        @PathVariable Integer commentId,
                                        @RequestBody CommentDto commentDto) {

        CommentDto commentDtoTemp = commentService.commentView(commentId);
        commentDtoTemp.setContent(commentDto.getContent());
        commentService.modify(commentDtoTemp);

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping("/posts/{postId}/comment/{commentId}")
    public ResponseEntity deleteComment(@PathVariable Integer postId,
                                        @PathVariable Integer commentId,
                                        Authentication authentication){

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        if(!principalDetails.getUsername().equals(commentService.commentView(postId).getUserEmail())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        commentService.delete(commentId);
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