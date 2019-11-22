package net.atos.spring_webapp.service;

import net.atos.spring_webapp.model.Post;
import net.atos.spring_webapp.model.User;
import net.atos.spring_webapp.model.enums.Category;
import net.atos.spring_webapp.repository.PermissionRepository;
import net.atos.spring_webapp.repository.PostRepository;
import net.atos.spring_webapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    PostRepository postRepository;
    UserRepository userRepository;
    UserService userService;
    PermissionRepository permissionRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, UserService userService, PermissionRepository permissionRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.userService = userService;
        this.permissionRepository = permissionRepository;
    }


    public List<Post> getAllPostsOrdered(Sort.Direction direction){
        return postRepository.findAll(Sort.by(direction,"addedDate"));
    }

    public Post getPostById(long postId) {
        Optional<Post> optionalPost = postRepository.findById(postId);
        Post post = null;
        if( optionalPost.isPresent() ){
            post = optionalPost.get();
        }
        return post;
    }

    public void addNewPost(Post newpost, Authentication authentication){
        if(authentication!=null){
            Post post = newpost;
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            post.setAuthor(userRepository.findFirstByEmail(userDetails.getUsername()));
            postRepository.save(post);
        }
    }

    public String isAuthenticated(Authentication authentication){
        if(authentication!=null){
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        }
        return "";
    }

    public Boolean isAdmin(Authentication authentication){
        if(authentication!=null){
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email =  userDetails.getUsername();
            User user  = userRepository.findFirstByEmail(email);
            if( user.getRoles().contains(permissionRepository.getOne((byte) 2)) ){ //ROLE_ADMIN
                return true;
            }
        }
        return false;
    }

    public User getAuthenticatedUser(Authentication authentication){
        User user = null;
        if(authentication!=null){
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            user = userService.getUserByEmail(userDetails.getUsername());
        }
        return user;
    }

    public void deletePostById(long postId){
        postRepository.deleteById(postId);
    }

    public void editPostById(Post post){
        Post editedPost = getPostById(post.getPostId());
        editedPost.setTitle(post.getTitle());
        editedPost.setCategory(post.getCategory());
        editedPost.setContent(post.getContent());
        postRepository.save(editedPost);
    }
}
