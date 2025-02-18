package org.paygateway.repositoryimpl;

import org.paygateway.exceptions.CartException;
import org.paygateway.exceptions.OrderException;
import org.paygateway.model.*;
import org.paygateway.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {


    @Autowired
    private CartService cartService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartItemRepository cartItemRepository;


    @Override
    public Order createOrder(User user, Address shippingAddress) throws CartException {
        shippingAddress.setUser(user);
        Address address = addressRepository.save(shippingAddress);
        user.getAddressList().add(address);
        userRepository.save(user);

        Cart cart = cartService.findUserCart(user.getId());
        if (cart.getCartItems().isEmpty()) throw new CartException("Cart is Empty");

        List<OrderItem> orderItems = new ArrayList<>();
        cart.getCartItems().forEach(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setSize(cartItem.getSize());
            orderItem.setDiscountedPrice(cartItem.getDiscountedPrice());
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setUserId(user.getId());
            OrderItem createdOrderItem = orderItemRepository.save(orderItem);
            orderItems.add(createdOrderItem);
        });
        Order createdOrder = new Order();
        createdOrder.setUser(user);
        createdOrder.setOrderItems(orderItems);
        createdOrder.setOrderDate(LocalDateTime.now());
        createdOrder.setDiscount(cart.getDiscount());
        createdOrder.setShippingAddress(address);
        createdOrder.setTotalItem(cart.getTotalItems());
        createdOrder.setTotalPrice(cart.getTotalPrice());
        createdOrder.setTotalDiscountedPrice(cart.getTotalDiscountedPrice());
        createdOrder.setOrderStatus("PENDING");
        createdOrder.getPaymentDetails().setStatus("PENDING");
        Order savedOrder = orderRepository.save(createdOrder);

        for (OrderItem item : orderItems) {
            item.setOrder(savedOrder);
            orderItemRepository.save(item);
        }

        for (CartItem item : cart.getCartItems()) {
            cartItemRepository.delete(item);
        }

        return savedOrder;

    }

    @Override
    public Order findOrderById(Long id) throws OrderException {
        return orderRepository.findById(id).orElseThrow(() -> new OrderException("Order not found"));
    }

    @Override
    public List<Order> userOrderHistory(Long userId) {
        return orderRepository.getUsersOrders(userId);
    }

    @Override
    public Order placeOrder(Long orderId) throws OrderException {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderException("Order Not found"));
        order.setOrderStatus("SHIPPED");
        return orderRepository.save(order);
    }

    @Override
    public Order confirmOrder(Long orderId) throws OrderException {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderException("Order Not found"));
        //TODO: Change status
        order.setOrderStatus("CONFIRMED");
        return orderRepository.save(order);
    }

    @Override
    public Order shippedOrder(Long orderId) throws OrderException {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderException("Order Not found"));
        order.setOrderStatus("SHIPPED");
        return orderRepository.save(order);
    }

    @Override
    public Order deliverOrder(Long orderId) throws OrderException {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderException("Order Not found"));
        order.setOrderStatus("DELIVERED");
        return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(Long orderId) throws OrderException {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderException("Order Not found"));
        order.setOrderStatus("CANCELLED");
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public String deleteOrder(Long orderId) throws OrderException {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderException("Order Not found"));
        orderRepository.delete(order);
        return "Order Deleted Successfully";
    }
}
