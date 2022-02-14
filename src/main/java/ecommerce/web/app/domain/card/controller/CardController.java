package ecommerce.web.app.domain.card.controller;

import ecommerce.web.app.domain.card.service.CardService;
import ecommerce.web.app.domain.card.model.Card;
import ecommerce.web.app.domain.post.model.Post;
import ecommerce.web.app.domain.user.model.User;
import ecommerce.web.app.domain.post.service.PostService;
import ecommerce.web.app.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CardController {

    @Autowired
    CardService cardService;

    @Autowired
    PostService postService;

    @Autowired
    UserService userService;

    @PostMapping("/add-to-card/{postId}")
    public ResponseEntity addToCard(@PathVariable(name = "postId") long postId) {
        Post findPost = postService.findByPostId(postId);
        User getAuthenticatedUser = userService.getAuthenticatedUser().get();
        Card findIfPostAlreadyExists = cardService.findCardByUserAndPost(getAuthenticatedUser,findPost);
        if(!(findIfPostAlreadyExists == null)){
            return new ResponseEntity("This item is already saved to your card",HttpStatus.CONFLICT);
        }else if(findPost.equals("") || findPost == null){
            return new ResponseEntity("Post not found",HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity(cardService.addToCard(findPost, getAuthenticatedUser), HttpStatus.ACCEPTED);
        }
    }

    @DeleteMapping("/remove-from-card/{postId}")
    public ResponseEntity removeFromCard(@PathVariable(name = "postId") long postId) {
        Post findPost = postService.findByPostId(postId);
        User getAuthenticatedUser = userService.getAuthenticatedUser().get();
        if (findPost.equals("") || getAuthenticatedUser.equals("")) {
            return new ResponseEntity("No post or user authenticated found !", HttpStatus.NO_CONTENT);
        } else {
            Card findCardByPostAndUser = cardService.findCardByUserAndPost(getAuthenticatedUser, findPost);
            if (findCardByPostAndUser.equals("")) {
                return new ResponseEntity("No items found in card", HttpStatus.NO_CONTENT);
            } else {
                cardService.removeFromCard(findCardByPostAndUser);
                return new ResponseEntity("Removed successfully from wishlist", HttpStatus.ACCEPTED);
            }
        }
    }

    @GetMapping("/show-card")
    public ResponseEntity showCard(){
        User getAuthenticatedUser = userService.getAuthenticatedUser().get();
        if(getAuthenticatedUser == null || getAuthenticatedUser == null){
            return new ResponseEntity("No authenticated user , permission denied",HttpStatus.CONFLICT);
        }
        else if(cardService.findByUser(getAuthenticatedUser).isEmpty() || cardService.findByUser(getAuthenticatedUser) == null){
            return new ResponseEntity("Card is empty",HttpStatus.NO_CONTENT);
        }
        else {
            System.out.println(cardService.findByUser(getAuthenticatedUser).stream().count());
            return new ResponseEntity(cardService.findByUser(getAuthenticatedUser),HttpStatus.ACCEPTED);
        }
    }
}