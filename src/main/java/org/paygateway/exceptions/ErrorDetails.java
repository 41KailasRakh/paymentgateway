package org.paygateway.exceptions;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ErrorDetails {
    private String message;
    private LocalDateTime timeStamp = LocalDateTime.now();
}
