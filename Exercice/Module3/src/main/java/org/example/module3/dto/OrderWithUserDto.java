package org.example.module3.dto;

import org.example.module3.model.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderWithUserDto(
        Long id,
        Long userId,
        String email,
        String firstName,
        String lastName,
        String fullName,
        String phoneNumber,
        String orderNumber,
        OrderStatus status ,
        BigDecimal totalAmount,
        String shippingAddress,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

}
