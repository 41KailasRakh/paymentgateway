package org.paygateway.repositoryimpl;

import org.paygateway.exceptions.ProductException;
import org.paygateway.model.Product;
import org.paygateway.model.Review;
import org.paygateway.model.User;
import org.paygateway.pojo.ReviewRequest;
import org.paygateway.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductService productService;

    @Override
    public Review createReview(ReviewRequest request, User user) throws ProductException {
        Product product = productService.findProductById(request.getProductId());
        if (product == null) throw new ProductException("Product not found");
        Review review = new Review();
        review.setReview(request.getReview());
        review.setUser(user);
        review.setProduct(product);
        review.setCreatedAt(LocalDateTime.now());
        review = reviewRepository.save(review);
        return review;
    }

    @Override
    public List<Review> getAllReviewsByProductId(Long productId) {
        List<Review> list = reviewRepository.getAllReviewsByProductId(productId);
        return list;
    }
}
