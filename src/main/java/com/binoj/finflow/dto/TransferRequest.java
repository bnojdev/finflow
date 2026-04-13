package com.binoj.finflow.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequest {

    private Long senderId;
    private Long receiverId;
    @DecimalMin("1.0")
    private BigDecimal amount;

}