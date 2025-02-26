package org.paygateway.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @ManyToOne()
    @JsonIgnore
    private Cart cart;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Product product;
    private String size;
    private int quantity;
    private int price;
    private int discountedPrice;
    private Long userId;
}
