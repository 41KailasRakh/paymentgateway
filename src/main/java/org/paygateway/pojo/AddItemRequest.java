package org.paygateway.pojo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddItemRequest {

    private Long productId;

    @NotNull
    private String size;

    @Min(1)
    private Integer quantity;
    private Integer price;
}
