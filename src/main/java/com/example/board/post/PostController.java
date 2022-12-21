package com.example.board.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/")
    public String Home(){
        return "home";
    }
    @GetMapping("/postsForm")
    public String writeForm(){
        return "post";
    }

    @PostMapping("/posts")
    public String Write(PostDto postdto){

        postService.write(postdto);

        return "redirect:/";
    }
    @GetMapping("/posts")
    public String View(Model model , @RequestParam Kind kind, @RequestParam(defaultValue = "1") Integer page ) {

        model.addAttribute("list",postService.pageList(kind));

        return "postlist";
    }
    @GetMapping("/posts/{postId}")
    public String Read(Model model , @PathVariable Integer postId){

        model.addAttribute("post",postService.postView(postId));
        return "postview";
    }
    @DeleteMapping("/posts/{postId}")
    public String delete(@PathVariable Integer postId) {

        postService.delete(postId);

        return "redirect:/";
    }
    @GetMapping("/posts/{postId}/modify")
    public String modifyForm(@PathVariable Integer postId,
                             Model model){
        model.addAttribute("post",postService.postView(postId));

        return "postmodify";
    }
    @PutMapping("posts/{postId}")
    public String modify(@PathVariable Integer postId, PostDto postdto){

        PostDto postdtotemp = postService.postView(postId);
        postdtotemp.setTitle(postdto.getTitle());
        postdtotemp.setContent(postdto.getContent());
        postdtotemp.setKind(postdto.getKind());

        postService.write(postdtotemp);
        return "redirect:/";
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