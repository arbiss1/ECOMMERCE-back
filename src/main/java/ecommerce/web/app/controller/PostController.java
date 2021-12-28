package ecommerce.web.app.controller;


import ecommerce.web.app.model.ImageUpload;
import ecommerce.web.app.model.Post;
import ecommerce.web.app.model.mapper.MapStructMapper;
import ecommerce.web.app.service.PostService;
import ecommerce.web.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;


@RestController
public class PostController {

    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    @Autowired
    MapStructMapper mapStructMapper;



    @PostMapping("/post")
    public ResponseEntity<Post> post(@Valid @RequestBody Post post, BindingResult result) throws InterruptedException {
        if(result.hasErrors()){
            return new ResponseEntity(result.getAllErrors(), HttpStatus.CONFLICT);
        }
        else{
            List<ImageUpload> postsImageUrls = post.getPostImageUrl();
            postService.savePost(post,userService.getAuthenticatedUser(),postsImageUrls);
            return new ResponseEntity(post,HttpStatus.ACCEPTED);
        }
    }

    @GetMapping("/list-all-posts")
    public ResponseEntity<Post> listAllPosts(){
        List<Post> listOfPosts = postService.findAll();
        if(listOfPosts.isEmpty()){
            return new ResponseEntity("No posts available",HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity(listOfPosts,HttpStatus.ACCEPTED);
        }
    }

    @GetMapping("/list-posts-by-user-auth")
    public ResponseEntity<Post> listPostsByAuthenticatedUser(){
        List<Post> listByAuthenticatedUser = postService.findByUserId(userService.getAuthenticatedUser().get().getId());
        if(listByAuthenticatedUser.isEmpty()){
            return new ResponseEntity("No post for user" + userService.getAuthenticatedUser().get().getUsername(),HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity(listByAuthenticatedUser,HttpStatus.ACCEPTED);
        }
    }

    @PutMapping("/edit-post/{postId}")
    public ResponseEntity<Post> editPost(@PathVariable (name = "postId") long postId ,@RequestBody Post post,BindingResult result) {
        Post findPost = postService.findByPostId(postId);
        if(result.hasErrors()){
            return new ResponseEntity(result.getAllErrors(),HttpStatus.CONFLICT);
        }
        else if(findPost.equals("")){
            return new ResponseEntity("Post not found",HttpStatus.NO_CONTENT);
        }
        else {
            List<ImageUpload> postsImageUrls = post.getPostImageUrl();
            postService.editPost(postId,post,userService.getAuthenticatedUser(),postsImageUrls);
            return new ResponseEntity(post,HttpStatus.ACCEPTED);
        }
    }
}
