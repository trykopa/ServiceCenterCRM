package com.googe.ssadm.sc.repository;

import com.googe.ssadm.sc.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupplierRepo extends JpaRepository<Supplier, Long> {
    Optional<Supplier> findSupplierByEmail(String email);
    Optional<Supplier> findSupplierByMobile(String mobile);
}
