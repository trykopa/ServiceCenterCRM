package com.googe.ssadm.sc.repository;

import com.googe.ssadm.sc.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {
    Optional<Order> findOrderById(Long id);
    Optional<Order> findOrderBySerialNo(String serialNo);
    Page<Order> findAllById(Pageable pageable, Long id);
    List<Order> findAllBySerialNo(String serial);

    @Override
    @EntityGraph(attributePaths = {"client"})
    Page<Order> findAll(Pageable pageable);
}
