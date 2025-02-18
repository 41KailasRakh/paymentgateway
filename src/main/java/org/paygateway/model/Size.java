package org.paygateway.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Size {
    private String name;
    private String quantity;
}
