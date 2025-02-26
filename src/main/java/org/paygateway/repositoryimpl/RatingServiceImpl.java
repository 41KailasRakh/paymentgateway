package org.paygateway.repositoryimpl;

import org.paygateway.exceptions.ProductException;
import org.paygateway.exceptions.RatingException;
import org.paygateway.model.Product;
import org.paygateway.model.Rating;
import org.paygateway.model.User;
import org.paygateway.pojo.RatingRequest;
import org.paygateway.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private ProductService productService;

    @Override
    public Rating createRating(RatingRequest request, User user) throws ProductException, RatingException {
        Product product = productService.findProductById(request.getProductId());
        Rating existingRating = ratingRepository.findByUserAndProductId(user.getId(), product.getId());
        Rating rating = new Rating();
        if(existingRating != null){
            throw new RatingException("Rating already Provided");
        }
        rating.setProduct(product);
        rating.setRating(request.getRating());
        rating.setUser(user);
        rating.setCreatedAt(LocalDateTime.now());
        rating = ratingRepository.save(rating);
        return rating;
    }

    @Override
    public List<Rating> getProductsRating(Long productId) {
        List<Rating> list = ratingRepository.getAllProductsRating(productId);
        return list;
    }
}
