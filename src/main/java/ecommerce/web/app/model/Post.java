package ecommerce.web.app.model;

import ecommerce.web.app.model.enums.AdvertIndex;
import ecommerce.web.app.model.enums.Currency;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "post")
@Data
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "post_id")
    private long postId;

    @NotEmpty(message = "Title must not be empty")
    private String postTitle;

    @NotEmpty(message = "Description must not be empty")
    private String postDescription;

    @NotEmpty(message = "Price must not be empty")
    private String postPrice;
    private String postColor;
    private String postCode;

    @NotEmpty(message = "IsInSale must not be empty")
    private boolean isInSale;
    private String postSlug;
    private LocalDate postDate;
    private LocalTime postTime;
    //enum
    private Currency postCurrency;
    //enum
    @NotNull(message = "Advert Index must not be empty")
    private AdvertIndex postAdvertIndex;
    private String address;
    private long number;
    private String firstName;
    private String lastName;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private User user;

    @Size(max = 10, min = 1)
    @NotEmpty(message = "Images must not be empty")
    @NotNull(message = "Images must not be empty")
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "post_image_uploads")
    private List<ImageUpload> postImageUrl;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "post_category")
    private List<Categories> categories;
}
