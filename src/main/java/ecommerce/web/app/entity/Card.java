package ecommerce.web.app.entity;

import lombok.Data;
import javax.persistence.*;

@Entity
@Data
@Table(name = "card")
public class Card extends BaseEntity  {

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "post_id")
    private Post post;

    private String totalPrice;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "ordered_by_user")
    private User user;

}
