package org.paygateway.repository;

import org.paygateway.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o from Order o WHERE ( o.user.id = :userId )" +
            "AND (o.orderStatus = 'PLACED' OR o.orderStatus = 'CONFIRMED' OR o.orderStatus = 'SHIPPED' OR o.orderStatus = 'DELIVERED' OR o.orderStatus = 'PENDING' )" +
            "ORDER BY o.id DESC"
    )
    List<Order> getUsersOrders(@Param("userId") Long userId);
}
