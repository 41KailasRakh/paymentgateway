package org.paygateway.controller;

import org.paygateway.exceptions.ProductException;
import org.paygateway.exceptions.RatingException;
import org.paygateway.exceptions.UserException;
import org.paygateway.model.Rating;
import org.paygateway.model.User;
import org.paygateway.pojo.RatingRequest;
import org.paygateway.repositoryimpl.RatingService;
import org.paygateway.repositoryimpl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    @Autowired
    private UserService userService;

    @Autowired
    private RatingService ratingService;

    @PostMapping("")
    public ResponseEntity<Rating> createRatingHandler(@RequestBody RatingRequest rating,
                                                      @RequestHeader("Authorization") String jwt) throws UserException, ProductException, RatingException {
        User user = userService.findUserProfileByJwt(jwt);
        Rating newRating = ratingService.createRating(rating, user);
        return new ResponseEntity<>(newRating, HttpStatus.CREATED);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<List<Rating>> getAllRatingsForProductHandler(@PathVariable Long id) {
        List<Rating> list = ratingService.getProductsRating(id);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

}
