package ecommerce.web.app.domain.model;

import ecommerce.web.app.domain.enums.PostStatus;
import ecommerce.web.app.domain.enums.AdvertIndex;
import ecommerce.web.app.domain.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "post")
@Data
public class Post extends BaseEntity {

    @NotEmpty(message = "Title must not be empty")
    private String title;
    @NotEmpty(message = "Description must not be empty")
    private String description;
    @NotEmpty(message = "Price must not be empty")
    private String price;
    private boolean isInSale;
    private String slug;
    @Enumerated(EnumType.STRING)
    private Currency currency;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Advert Index must not be empty")
    private AdvertIndex postAdvertIndex;
    @NotBlank(message = "Address must not be null")
    private String address;
    @NotBlank(message = "Phone number must not be null")
    private String phoneNumber;
    @NotBlank(message = "First name must not be null")
    private String firstName;
    @NotBlank(message = "Last name must not be null")
    private String lastName;
    private String country;
    private String city;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn
    private User user;
    @Size(max = 5, min = 1)
    @NotNull(message = "Images must not be empty")
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "post_image_uploads")
    private List<ImageUpload> imageUrls;
    private String category;
    private String subcategory;
    @Column(name = "post_status")
    @Enumerated(EnumType.STRING)
    private PostStatus status;

}
