package net.atos.spring_webapp.controller;

import jdk.nashorn.internal.objects.annotations.Property;
import net.atos.spring_webapp.model.Post;
import net.atos.spring_webapp.model.enums.Category;
import net.atos.spring_webapp.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class PostController {

    PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/")
    public String allPosts(Model model, Authentication authentication){
        model.addAttribute("posts",postService.getAllPostsOrdered(Sort.Direction.DESC));
        model.addAttribute("newpost", new Post());
        model.addAttribute("categories", Category.values());
        model.addAttribute("isAuth",postService.isAuthenticated(authentication));
        model.addAttribute("isAdmin",postService.isAdmin(authentication));
        model.addAttribute("authUser",postService.getAuthenticatedUser(authentication));
        return "index"; //--nazwa widoku bez rozszerzenia html (resources.templates)
    }

    @GetMapping("/post&{postId}")
    public String getPostById(Model model, @PathVariable("postId") long postId){
        model.addAttribute("post",postService.getPostById(postId));
        return "post";
    }

    @PostMapping("/addpost")
    public String addNewPost(@ModelAttribute("newpost") @Valid Post newpost, BindingResult bindingResult, Model model, Authentication authentication){
        if(bindingResult.hasErrors()){
            model.addAttribute("posts",postService.getAllPostsOrdered(Sort.Direction.DESC));
            model.addAttribute("categories",Category.values());
            return "index";
        }
        postService.addNewPost(newpost,authentication);
        System.out.println("dodano nowy post!");
        return "redirect:/";
    }

    @GetMapping("/post/delete/{postId}")
    public String deletePost(@PathVariable("postId") long postId){
        postService.deletePostById(postId);
        System.out.println("post "+postId+" removed!");
        return "redirect:/";
    }

    @PostMapping("/post/delete")
    public String deletePostDELETE(@RequestParam("postId") long postId){
        postService.deletePostById(postId);
        System.out.println("post "+postId+" removed!");
        return "redirect:/";
    }

    @GetMapping("/post&edit&{postId}")
    public String editPost(@PathVariable("postId") long postId, Model model){
        model.addAttribute("post",postService.getPostById(postId));
        model.addAttribute("categories",Category.values());
        return "editPost";
    }

    @PostMapping("/editpost")
    public String editPost(@ModelAttribute("post") Post post, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "editpost";
        }
        postService.editPostById(post);
        return "redirect:/";
    }

}
