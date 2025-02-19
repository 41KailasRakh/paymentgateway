package org.paygateway.controller;

import org.paygateway.exceptions.ProductException;
import org.paygateway.exceptions.UserException;
import org.paygateway.model.Review;
import org.paygateway.model.User;
import org.paygateway.pojo.ReviewRequest;
import org.paygateway.repositoryimpl.ReviewService;
import org.paygateway.repositoryimpl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {


    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @PostMapping("")
    public ResponseEntity<Review> addNewReviewHandler(@RequestHeader("Authorization") String jwt,
                                                      @RequestBody ReviewRequest reviewRequest) throws UserException, ProductException {
        User user = userService.findUserProfileByJwt(jwt);
        Review review = reviewService.createReview(reviewRequest, user);
        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getAllReviewsOfProduct(@PathVariable Long productId) {
        List<Review> list = reviewService.getAllReviewsByProductId(productId);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }


}
