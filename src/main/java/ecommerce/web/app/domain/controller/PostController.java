package ecommerce.web.app.domain.controller;

import ecommerce.web.app.entity.ImageUpload;
import ecommerce.web.app.entity.Post;
import ecommerce.web.app.domain.model.PostRequest;
import ecommerce.web.app.domain.service.PostService;
import ecommerce.web.app.exception.customExceptions.PostCustomException;
import ecommerce.web.app.configs.mapper.MapStructMapper;
import ecommerce.web.app.domain.service.UserService;
import ecommerce.web.app.exception.customExceptions.UserNotFoundException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
@CrossOrigin
public class PostController {

   public final UserService userService;
   public final PostService postService;
   public final MapStructMapper mapStructMapper;
   public final MessageSource messageByLocale;

    public PostController(UserService userService,PostService postService,
                          MapStructMapper mapStructMapper,MessageSource messageByLocale){
        this.userService = userService;
        this.postService = postService;
        this.mapStructMapper = mapStructMapper;
        this.messageByLocale = messageByLocale;
    }

    private final Locale locale = Locale.ENGLISH;

    @PostMapping("/post/add")
    public ResponseEntity<Post> post(@Valid @RequestBody PostRequest post, BindingResult result)
            throws InterruptedException, UserNotFoundException {
        if(result.hasErrors()){
            return new ResponseEntity(result.getAllErrors(), HttpStatus.CONFLICT);
        }
        else{
            List<ImageUpload> postsImageUrls = post.getImageUrls();
            return new ResponseEntity(postService.savePost(mapStructMapper.postDtoToPost(post)
                    ,userService.getAuthenticatedUser(), postsImageUrls)
                    ,HttpStatus.ACCEPTED);
        }
    }

    @PostMapping("/post/status/change/{postId}")
    public ResponseEntity<Post> changePostStatusToActive(@PathVariable(value = "postId") long postId)
            throws PostCustomException {
        Optional<Post> findPostById = postService.findByPostId(postId);
        if(!findPostById.isPresent()){
            throw new PostCustomException(
                    messageByLocale.getMessage("error.404.postNotFound",null,locale));
        }
        else
        {
            Post getFoundPost = findPostById.get();
            return new ResponseEntity(postService.changeStatusToActive(
                    getFoundPost,userService.getAuthenticatedUser()),HttpStatus.OK);
        }
    }

    @GetMapping("/post/all")
    public ResponseEntity<Post> listAllPosts() throws PostCustomException {
        List<Post> listOfPosts = postService.findAll();
        if(listOfPosts.isEmpty()){
            throw new PostCustomException(
                    messageByLocale.getMessage("error.404.postNotFound",null,locale));
        }else {
            return new ResponseEntity(listOfPosts,HttpStatus.ACCEPTED);
        }
    }

    @GetMapping("/post/search")
    public ResponseEntity<Post> searchPosts(@RequestParam String keyword)
            throws PostCustomException {
        List<Post> seachForPosts = postService.searchPosts(keyword);
        if(keyword.equals("") || keyword.equals(" ") ||
           keyword.equals(null)){
            return new ResponseEntity(postService.findAll(),HttpStatus.ACCEPTED);
        }else if( seachForPosts.isEmpty()){
            throw new PostCustomException(
                    messageByLocale.getMessage("error.404.postNotFound",null,locale));
        }
        else {
            return new ResponseEntity(seachForPosts,HttpStatus.ACCEPTED);
        }
    }

    @GetMapping("/user/post/all")
    public ResponseEntity<Post> listPostsByAuthenticatedUser() throws PostCustomException {
        List<Post> listByAuthenticatedUser = postService.findByUserId(
                userService.getAuthenticatedUser().get().getId());
        if(listByAuthenticatedUser.isEmpty()){
            throw new PostCustomException(
                    messageByLocale.getMessage("error.404.postNotFound",null,locale));
        }else {
            return new ResponseEntity(listByAuthenticatedUser,HttpStatus.ACCEPTED);
        }
    }

    @PutMapping("/post/edit/{postId}")
    public ResponseEntity<Post> editPost(@PathVariable (name = "postId") long postId ,
                                         @RequestBody PostRequest post, BindingResult result)
            throws PostCustomException, UserNotFoundException {
        Optional<Post> findPost = postService.findByPostId(postId);
        if(result.hasErrors()){
            return new ResponseEntity(result.getAllErrors(),HttpStatus.CONFLICT);
        }
        else if(!findPost.isPresent()){
            throw new PostCustomException(messageByLocale.getMessage("error.404.postNotFound",null,locale));
        }
        else {
            List<ImageUpload> postsImageUrls = post.getImageUrls();
            return new ResponseEntity(postService.editPost(postId,mapStructMapper.postDtoToPost(post),
                    userService.getAuthenticatedUser(),postsImageUrls)
                    ,HttpStatus.ACCEPTED);
        }
    }

    @DeleteMapping("/post/delete/{postId}")
    public ResponseEntity<Post> deletePost(@PathVariable(name = "postId") long postId)
            throws PostCustomException {
        Optional<Post> findPost = postService.findByPostId(postId);
        if(!findPost.isPresent()){
            throw new PostCustomException(
                    messageByLocale.getMessage("error.404.postNotFound",null,locale));
        }else {
            postService.deleteById(postId);
            return new ResponseEntity("Deleted successfully",HttpStatus.ACCEPTED);
        }
    }
}
