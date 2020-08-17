package com.googe.ssadm.sc.repository;

import com.googe.ssadm.sc.entity.RepairTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepTableRepo extends JpaRepository<RepairTable, Long> {
    Optional<RepairTable> findRepTableById(Long id);
    Optional<RepairTable> findRepairTableByOrder_Id(Long id);
}
