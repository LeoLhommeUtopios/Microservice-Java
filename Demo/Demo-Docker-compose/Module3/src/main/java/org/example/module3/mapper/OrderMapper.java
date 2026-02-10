package org.example.module3.mapper;

import org.example.module3.dto.OrderDto;
import org.example.module3.dto.OrderWithUserDto;
import org.example.module3.dto.UserDto;
import org.example.module3.model.Order;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderDto toDto (Order order){
        return new OrderDto(
                order.getId(),
                order.getUserId(),
                order.getOrderNumber(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getShippingAddress(),
                order.getNotes(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }

    public OrderWithUserDto toDtoWithUser (Order order, UserDto userDto){
        return new OrderWithUserDto(
                order.getId(),
                order.getUserId(),
                userDto.email(),
                userDto.firstName(),
                userDto.lastName(),
                userDto.fullName(),
                userDto.phoneNumber(),
                order.getOrderNumber(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getShippingAddress(),
                order.getNotes(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}
