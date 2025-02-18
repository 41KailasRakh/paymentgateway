package org.paygateway.pojo;

import lombok.Data;

@Data
public class RatingRequest {
    private Long productId;
    private double rating;
}
