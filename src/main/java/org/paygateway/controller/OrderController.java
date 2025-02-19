package org.paygateway.controller;

import org.paygateway.exceptions.CartException;
import org.paygateway.exceptions.OrderException;
import org.paygateway.exceptions.UserException;
import org.paygateway.model.Address;
import org.paygateway.model.Order;
import org.paygateway.model.User;
import org.paygateway.repositoryimpl.OrderServiceImpl;
import org.paygateway.repositoryimpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private OrderServiceImpl orderService;

    @PostMapping("/orders")
    public ResponseEntity<Order> createOrderHandler(@RequestBody Address shippingAddress, @RequestHeader("Authorization") String jwt) throws UserException, CartException {
        User user = userService.findUserProfileByJwt(jwt);
        Order order = orderService.createOrder(user, shippingAddress);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getOrderHistoryHandler(@RequestHeader("Authorization") String jwt) throws UserException {
        User user = userService.findUserProfileByJwt(jwt);
        List<Order> orderList = orderService.userOrderHistory(user.getId());
        return new ResponseEntity<>(orderList, HttpStatus.ACCEPTED);
    }

    @GetMapping("orders/{id}")
    public ResponseEntity<Order> findOrderByOrderIdHandler(@RequestHeader("Authorization") String jwt, @PathVariable Long id) throws OrderException {
        Order order = orderService.findOrderById(id);
        return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
    }


}
