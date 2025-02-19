package org.paygateway.controller;

import jakarta.validation.Valid;
import org.paygateway.exceptions.CartItemException;
import org.paygateway.exceptions.UserException;
import org.paygateway.model.CartItem;
import org.paygateway.model.User;
import org.paygateway.pojo.AddItemRequest;
import org.paygateway.repositoryimpl.CartItemService;
import org.paygateway.repositoryimpl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart/cart-item")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private UserService userService;


    @PutMapping("/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItem(@RequestHeader("Authorization") String jwt,
                                                   @PathVariable Long cartItemId,
                                                   @RequestBody @Valid AddItemRequest item) throws UserException, CartItemException {
        User user = userService.findUserProfileByJwt(jwt);
        CartItem cartItem = new CartItem();
        cartItem.setSize(item.getSize());
        cartItem.setQuantity(item.getQuantity());
        CartItem updatedItem = cartItemService.updateCartItem(user.getId(), cartItemId, cartItem);
        return new ResponseEntity<>(updatedItem, HttpStatus.ACCEPTED);
    }


}
