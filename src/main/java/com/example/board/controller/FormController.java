//package com.example.board.controller;
//
//import com.example.board.service.PostService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//
//@Controller
//public class FormController {
//
//    @Autowired
//    PostService postService;
//    //PostController form
//    @GetMapping("/")
//    public String home() {
//        return "home";
//    }
//
//    @GetMapping("/postsForm")
//    public String writeForm() {
//        return "post";
//    }
//
//    @GetMapping("/posts/{postId}/modify")
//    public String modifyForm(@PathVariable Integer postId,
//                             Model model) {
//        model.addAttribute("post", postService.postView(postId));
//        return "postmodify";
//    }
//
//    //UserController form
//    @GetMapping("")
//    public String join(){
//        return "join";
//    }
//    @GetMapping("/loginForm")
//    public String loginForm(){
//        return "login";
//    }
//
//}
