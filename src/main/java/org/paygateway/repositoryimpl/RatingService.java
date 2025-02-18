package org.paygateway.repositoryimpl;

import org.paygateway.exceptions.ProductException;
import org.paygateway.exceptions.RatingException;
import org.paygateway.model.Rating;
import org.paygateway.model.User;
import org.paygateway.pojo.RatingRequest;

import java.util.List;

public interface RatingService {
    Rating createRating(RatingRequest request, User user) throws ProductException, RatingException;

    List<Rating> getProductsRating(Long productId);
}
