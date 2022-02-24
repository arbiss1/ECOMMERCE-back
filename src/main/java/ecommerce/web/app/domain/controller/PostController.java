package ecommerce.web.app.domain.controller;


import ecommerce.web.app.domain.model.ImageUpload;
import ecommerce.web.app.domain.model.Post;
import ecommerce.web.app.domain.model.dto.PostRequest;
import ecommerce.web.app.i18nConfig.MessageByLocaleImpl;
import ecommerce.web.app.domain.service.PostService;
import ecommerce.web.app.exception.PostCustomException;
import ecommerce.web.app.domain.model.mapper.MapStructMapper;
import ecommerce.web.app.domain.service.UserService;
import javafx.geometry.Pos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin
public class PostController {

   public final UserService userService;
   public final PostService postService;
   public final MapStructMapper mapStructMapper;
   public final MessageByLocaleImpl messageByLocale;

    public PostController(UserService userService,PostService postService,
                          MapStructMapper mapStructMapper,MessageByLocaleImpl messageByLocale){
        this.userService = userService;
        this.postService = postService;
        this.mapStructMapper = mapStructMapper;
        this.messageByLocale = messageByLocale;
    }

    @PostMapping("/post")
    public ResponseEntity<Post> post(@Valid @RequestBody PostRequest post, BindingResult result)
            throws InterruptedException {
        if(result.hasErrors()){
            return new ResponseEntity(result.getAllErrors(), HttpStatus.CONFLICT);
        }
        else{
            List<ImageUpload> postsImageUrls = post.getImageUrls();
            return new ResponseEntity(postService.savePost(mapStructMapper.postDtoToPost(post)
                    ,userService.getAuthenticatedUser(),postsImageUrls)
                    ,HttpStatus.ACCEPTED);
        }
    }

    @PostMapping("/post/status-change")
    public ResponseEntity<Post> changePostStatusToActive(@RequestParam(value = "postId") long postId)
            throws PostCustomException {
        Optional<Post> findPostById = postService.findByPostId(postId);
        if(!findPostById.isPresent()){
            throw new PostCustomException(
                    messageByLocale.getMessage("error.404.postNotFound"));
        }
        else
        {
            Post getFoundPost = findPostById.get();
            return new ResponseEntity(postService.changeStatusToActive(
                    getFoundPost,userService.getAuthenticatedUser()),HttpStatus.OK);
        }
    }

    @GetMapping("/list-all-posts")
    public ResponseEntity<Post> listAllPosts() throws PostCustomException {
//        Pageable firstPageWithTwoElements = PageRequest.of(0,3);
        List<Post> listOfPosts = postService.findAll();
        if(listOfPosts.isEmpty()){
            throw new PostCustomException(
                    messageByLocale.getMessage("error.404.postNotFound"));
        }else {
            return new ResponseEntity(listOfPosts,HttpStatus.ACCEPTED);
        }
    }

    @GetMapping("/search-post")
    public ResponseEntity<Post> searchPosts(@RequestParam String keyword)
            throws PostCustomException {
        List<Post> seachForPosts = postService.searchPosts(keyword);
        if(keyword.equals("") || keyword.equals(" ") ||
           keyword.equals(null)){
            return new ResponseEntity(postService.findAll(),HttpStatus.ACCEPTED);
        }else if( seachForPosts.isEmpty()){
            throw new PostCustomException(
                    messageByLocale.getMessage("error.404.postNotFound"));
        }
        else {
            return new ResponseEntity(seachForPosts,HttpStatus.ACCEPTED);
        }
    }

    @GetMapping("/list-posts-by-user-auth")
    public ResponseEntity<Post> listPostsByAuthenticatedUser() throws PostCustomException {
        List<Post> listByAuthenticatedUser = postService.findByUserId(
                userService.getAuthenticatedUser().get().getId());
        if(listByAuthenticatedUser.isEmpty()){
            throw new PostCustomException(
                    messageByLocale.getMessage("error.404.postNotFound"));
        }else {
            return new ResponseEntity(listByAuthenticatedUser,HttpStatus.ACCEPTED);
        }
    }

    @PutMapping("/edit-post/{postId}")
    public ResponseEntity<Post> editPost(@PathVariable (name = "postId") long postId ,
                                         @RequestBody PostRequest post, BindingResult result)
                                         throws PostCustomException {
        Optional<Post> findPost = postService.findByPostId(postId);
        if(result.hasErrors()){
            return new ResponseEntity(result.getAllErrors(),HttpStatus.CONFLICT);
        }
        else if(!findPost.isPresent()){
            throw new PostCustomException(messageByLocale.getMessage("error.404.postNotFound"));
        }
        else {
            List<ImageUpload> postsImageUrls = post.getImageUrls();
            return new ResponseEntity(postService.editPost(postId,mapStructMapper.postDtoToPost(post),
                    userService.getAuthenticatedUser(),postsImageUrls)
                    ,HttpStatus.ACCEPTED);
        }
    }

    @DeleteMapping("/delete-post/{postId}")
    public ResponseEntity<Post> deletePost(@PathVariable(name = "postId") long postId)
            throws PostCustomException {
        Optional<Post> findPost = postService.findByPostId(postId);
        if(!findPost.isPresent()){
            throw new PostCustomException(
                    messageByLocale.getMessage("error.404.postNotFound"));
        }else {
            postService.deleteById(postId);
            return new ResponseEntity("Deleted successfully",HttpStatus.ACCEPTED);
        }
    }
}
