package org.paygateway.pojo;

import lombok.Data;

@Data
public class ReviewRequest {
    private Long productId;
    private String review;
}
