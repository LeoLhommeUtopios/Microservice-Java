package org.example.module3.service;

import org.example.module3.Exception.OrderNotFoundException;
import org.example.module3.client.UserClient;
import org.example.module3.dto.OrderDto;
import org.example.module3.dto.OrderWithUserDto;
import org.example.module3.dto.UserDto;
import org.example.module3.mapper.OrderMapper;
import org.example.module3.model.Order;
import org.example.module3.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Service de gestion des commandes.
 *
 * Ce service utilise Feign pour communiquer avec le User Service
 * et Resilience4j pour la gestion des erreurs.
 */
@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final UserClient userClient;

    public OrderService(OrderRepository orderRepository,
                        OrderMapper orderMapper,
                        UserClient userClient) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.userClient = userClient;
    }

    /**
     * Récupère toutes les commandes.
     */
    public List<OrderDto> findAll() {
        log.debug("Récupération de toutes les commandes");
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    /**
     * Récupère une commande par son ID avec les informations utilisateur.
     *
     * Utilise le circuit breaker pour gérer l'indisponibilité du User Service.
     */
    public OrderWithUserDto findByIdWithUser(Long id) {
        log.debug("Recherche commande avec ID: {} et info utilisateur", id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("not found"));

        // Appel au User Service pour récupérer les infos utilisateur
        UserDto user = userClient.findById(order.getUserId());

        return orderMapper.toDtoWithUser(order, user);
    }

    /**
     * Récupère les commandes d'un utilisateur.
     */
    public List<OrderDto> findByUserId(Long userId) {
        log.debug("Récupération des commandes pour utilisateur: {}", userId);
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

}
