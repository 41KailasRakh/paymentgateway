package org.paygateway.repositoryimpl;

import org.paygateway.exceptions.CartException;
import org.paygateway.exceptions.ProductException;
import org.paygateway.model.Cart;
import org.paygateway.model.User;
import org.paygateway.pojo.AddItemRequest;

public interface CartService {
    void createCart(User user);

    String addCartItem(Long userId, AddItemRequest request) throws ProductException;

    Cart findUserCart(Long userId) throws CartException;

}
