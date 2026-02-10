package org.example.module3.repository;

import org.example.module3.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    List<Order> findByUserIdOrderByCreatedAtDesc (long userid);
}
