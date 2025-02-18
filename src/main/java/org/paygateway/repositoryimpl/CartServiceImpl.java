package org.paygateway.repositoryimpl;

import org.paygateway.exceptions.ProductException;
import org.paygateway.model.Cart;
import org.paygateway.model.CartItem;
import org.paygateway.model.Product;
import org.paygateway.model.User;
import org.paygateway.pojo.AddItemRequest;
import org.paygateway.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private ProductService productService;

    @Override
    public void createCart(User user) {
        Cart cart = new Cart();
        Set<CartItem> cartItems = new HashSet<>();
        cart.setCartItems(cartItems);
        cart.setUser(user);
        cartRepository.save(cart);
    }

    @Override
    public String addCartItem(Long userId, AddItemRequest request) throws ProductException {
        Cart cart = cartRepository.findByUserId(userId);
        Product product = productService.findProductById(request.getProductId());
        CartItem isPresent = cartItemService.isCartItemExists(cart, product, request.getSize(), userId);
        if (isPresent == null) {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(cart);
            cartItem.setQuantity(request.getQuantity() == null ? 1 : request.getQuantity());
            cartItem.setUserId(userId);
            cartItem.setDiscountedPrice(product.getDiscountedPrice());
            cartItem.setPrice(product.getDiscountedPrice() * cartItem.getQuantity());
            cartItem.setSize(request.getSize());

            CartItem createdCartItem = cartItemService.createCartItem(cartItem);
            System.out.println(createdCartItem);
            cart.getCartItems().add(createdCartItem);
            System.out.println(cart);
        } else return "Product Already Exists in card";
        return "Item Added to Cart";

    }

    @Override
    public Cart findUserCart(Long userId) {

        Cart cart = cartRepository.findByUserId(userId);
        int totalPrice = 0;
        int totalDiscountedPrice = 0;
        int totalItem = 0;
        for (CartItem cartItem : cart.getCartItems()) {
            totalPrice += cartItem.getPrice();
            totalItem += cartItem.getQuantity();
            totalDiscountedPrice += cartItem.getDiscountedPrice();
        }
        cart.setTotalPrice(totalPrice);
        cart.setTotalDiscountedPrice(totalDiscountedPrice);
        cart.setTotalItems(totalItem);
        cart.setDiscount(totalPrice - totalDiscountedPrice);
        cartRepository.save(cart);
        System.out.println("CART IS " + cart);
        return cart;

    }
}
