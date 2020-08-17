package com.googe.ssadm.sc.service;

import com.googe.ssadm.sc.entity.RepairTable;
import com.googe.ssadm.sc.repository.RepTableRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RepTableService {

    private final RepTableRepo repTableRepo;

    @Autowired
    public RepTableService(RepTableRepo repTableRepo) {
        this.repTableRepo = repTableRepo;
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RepTableService.class);

    public List<RepairTable> findAll(){
        log.info("RepTableService: findAll");
        return repTableRepo.findAll();
    }

    public Optional<RepairTable> findRepTableById(Long id){
        log.info("RepTableService: findRepTableById " + id );
        return repTableRepo.findRepTableById(id);
    }

    public Optional<RepairTable> findByOrderId(Long id){
        log.info("RepTableService: findByOrderId " + id );
        return repTableRepo.findRepairTableByOrder_Id(id);
    }

    public RepairTable save(RepairTable repairTable){
        log.info("RepTableService: saveOrUpdate" + repairTable.getId());
        return repTableRepo.save(repairTable);
    }
}
