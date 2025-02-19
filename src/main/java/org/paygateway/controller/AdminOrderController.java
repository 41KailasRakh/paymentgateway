package org.paygateway.controller;


import org.paygateway.exceptions.OrderException;
import org.paygateway.model.Order;
import org.paygateway.repositoryimpl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

    @Autowired
    private OrderServiceImpl orderService;

    @GetMapping("")
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orderList = orderService.getAllOrders();
        return new ResponseEntity<>(orderList, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{id}/confirmed")
    public ResponseEntity<Order> confirmOrderHandler(@RequestHeader("Authorization") String jwt, @PathVariable Long id) throws OrderException {
        Order order = orderService.confirmOrder(id);
        return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{id}/shipped")
    public ResponseEntity<Order> shippedOrderHandler(@RequestHeader("Authorization") String jwt, @PathVariable Long id) throws OrderException {
        Order order = orderService.shippedOrder(id);
        return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{id}/delivered")
    public ResponseEntity<Order> deliveredOrderHandler(@RequestHeader("Authorization") String jwt, @PathVariable Long id) throws OrderException {
        Order order = orderService.deliverOrder(id);
        return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{id}/cancelled")
    public ResponseEntity<Order> cancelOrderHandler(@RequestHeader("Authorization") String jwt, @PathVariable Long id) throws OrderException {
        Order order = orderService.cancelOrder(id);
        return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrderHandler(@RequestHeader("Authorization") String jwt, @PathVariable Long id) throws OrderException {
        var order = orderService.deleteOrder(id);
        return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
    }


}
