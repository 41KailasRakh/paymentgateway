package org.paygateway.repositoryimpl;

import org.paygateway.exceptions.ProductException;
import org.paygateway.model.Review;
import org.paygateway.model.User;
import org.paygateway.pojo.ReviewRequest;

import java.util.List;

public interface ReviewService {

    Review createReview(ReviewRequest request, User user) throws ProductException;

    List<Review> getAllReviewsByProductId(Long productId);
}
