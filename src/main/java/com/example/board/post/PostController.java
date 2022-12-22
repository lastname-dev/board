package com.example.board.post;

import com.example.board.config.auth.PrincipalDetails;
import com.example.board.user.User;
import com.example.board.user.UserDto.SessionUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/")
    public String home(){
        return "home";
    }
    @GetMapping("/postsForm")
    public String writeForm(){
        return "post";
    }
    @GetMapping("/posts")
    public String view(Model model , @RequestParam Kind kind, @RequestParam(defaultValue = "1") Integer page ) {

        model.addAttribute("list",postService.pageList(kind));

        return "postlist";
    }
    @GetMapping("/posts/{postId}/modify")
    public String modifyForm(@PathVariable Integer postId,
                             Model model){
        model.addAttribute("post",postService.postView(postId));

        return "postmodify";
    }
    @GetMapping("/posts/{postId}")
    public String read(Model model , @PathVariable Integer postId){

        model.addAttribute("post",postService.postView(postId));
        return "postview";
    }

    @PostMapping("/posts")
    public String Write(PostDto postdto, Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        postdto.setUser_email(principalDetails.getUserEmail());
        postService.write(postdto);
        return "redirect:/";
    }

    @PostMapping("/posts/delete/{postId}")
    public String delete(@PathVariable Integer postId) {

        postService.delete(postId);

        return "redirect:/";
    }

    @PostMapping("/posts/{postId}")
    public String modify(@PathVariable Integer postId, PostDto postdto){

        PostDto postdtotemp = postService.postView(postId);
        System.out.println("시간" +postdtotemp.getWrittenDate());
        postdtotemp.setTitle(postdto.getTitle());
        postdtotemp.setContent(postdto.getContent());
        postdtotemp.setKind(postdto.getKind());

        postService.modify(postdtotemp);
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