package org.paygateway.repositoryimpl;

import org.paygateway.model.Order;
import org.paygateway.model.User;
import org.paygateway.model.Address;
import org.paygateway.exceptions.CartException;
import org.paygateway.exceptions.OrderException;
import java.util.List;

public interface OrderService {

    Order createOrder(User user, Address shippingAddress) throws CartException;

    Order findOrderById(Long id) throws OrderException;

    List<Order> userOrderHistory(Long userId);

    Order placeOrder(Long orderId) throws OrderException;

    Order confirmOrder(Long orderId) throws OrderException;

    Order shippedOrder(Long orderId) throws OrderException;

    Order deliverOrder(Long orderId) throws OrderException;

    Order cancelOrder(Long orderId) throws OrderException;

    List<Order> getAllOrders();

    String deleteOrder(Long orderId) throws OrderException;


}
