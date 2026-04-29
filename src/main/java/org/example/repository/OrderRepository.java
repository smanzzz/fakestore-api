package org.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.concurrent.atomic.LongAccumulator;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
