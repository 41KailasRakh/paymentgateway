package org.paygateway.controller;

import org.paygateway.exceptions.CartException;
import org.paygateway.exceptions.CartItemException;
import org.paygateway.exceptions.ProductException;
import org.paygateway.exceptions.UserException;
import org.paygateway.model.Cart;
import org.paygateway.model.User;
import org.paygateway.pojo.AddItemRequest;
import org.paygateway.repositoryimpl.CartItemServiceImpl;
import org.paygateway.repositoryimpl.CartServiceImpl;
import org.paygateway.repositoryimpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    private CartServiceImpl cartService;

    @Autowired
    private CartItemServiceImpl cartItemService;

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/cart")
    public ResponseEntity<Cart> cartHandler(@RequestHeader("Authorization") String jwt) throws CartException, UserException {
        User user = userService.findUserProfileByJwt(jwt);
        Cart cart = cartService.findUserCart(user.getId());
        return new ResponseEntity<>(cart, HttpStatus.ACCEPTED);
    }

    @PostMapping("/cart")
    public ResponseEntity<String> addProductToCart(@RequestHeader("Authorization") String jwt, @RequestBody AddItemRequest addItemRequest) throws UserException, ProductException, CartException {
        User user = userService.findUserProfileByJwt(jwt);
        String response = cartService.addCartItem(user.getId(), addItemRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/cart/{id}")
    public ResponseEntity<String> removeProductFromCartHandler(@RequestHeader("Authorization") String jwt, @PathVariable Long id) throws UserException, CartItemException {
        User user = userService.findUserProfileByJwt(jwt);
        String response = cartItemService.removeCartItem(user.getId(), id);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }
}
