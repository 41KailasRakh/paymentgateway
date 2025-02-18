package org.paygateway.repositoryimpl;

import org.paygateway.exceptions.CartItemException;
import org.paygateway.exceptions.UserException;
import org.paygateway.model.CartItem;
import org.paygateway.model.Cart;
import org.paygateway.model.Product;
public interface CartItemService {

    CartItem createCartItem(CartItem cartItem);

    CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException, UserException;

    CartItem isCartItemExists(Cart cart, Product product, String size, Long userId);

    String removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException;
    CartItem findCartItemById(Long cartItemId) throws  CartItemException;
}
