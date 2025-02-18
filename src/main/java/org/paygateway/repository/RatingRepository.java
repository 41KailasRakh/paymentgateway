package org.paygateway.repository;

import org.paygateway.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query("SELECT r FROM Rating r WHERE r.product.id = :productId")
    List<Rating> getAllProductsRating(@Param("productId") Long productId);

    @Query("SELECT r FROM Rating r WHERE r.user.id = :userId AND r.product.id = :productId")
    Rating findByUserAndProductId(Long userId, Long productId);
}
