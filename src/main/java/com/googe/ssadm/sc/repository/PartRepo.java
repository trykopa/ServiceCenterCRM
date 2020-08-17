package com.googe.ssadm.sc.repository;

import com.googe.ssadm.sc.entity.Order;
import com.googe.ssadm.sc.entity.Part;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartRepo extends JpaRepository<Part, Long> {
    Optional<Part> findBySerialNo(String serialNo);
    List<Part> findAllByPartNo(String partNo);
    Page<Order> findAllById(Pageable pageble, Long id);
}
