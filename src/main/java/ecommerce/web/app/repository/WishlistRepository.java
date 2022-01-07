package ecommerce.web.app.repository;

import ecommerce.web.app.model.Post;
import ecommerce.web.app.model.User;
import ecommerce.web.app.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist,Long> {

//    Wishlist deleteWishlistByWishlistId(long wishlistId);

    Wishlist findWishlistByUserAndPost(User authenticatedUser,Post findPost);

    List<Wishlist> findWishlistByUser(User authenticatedUser);

    @Modifying
    @Transactional
    @Query(nativeQuery = true , value = "DELETE FROM ecommerce.wishlist WHERE wishlist_id = :wishlistId ;")
    void removeFromWishlist(@Param(value = "wishlistId") long wishlistId);

}
