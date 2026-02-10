package org.example.module3.dto;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.example.module3.model.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderDto(
        Long id,
        Long userId,
        String orderNumber,
        OrderStatus status ,
        BigDecimal totalAmount,
        String shippingAddress,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
