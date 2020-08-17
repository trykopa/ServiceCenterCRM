package com.googe.ssadm.sc.service;

import com.googe.ssadm.sc.entity.Supplier;
import com.googe.ssadm.sc.repository.SupplierRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {

    private final SupplierRepo supplierRepo;

    @Autowired
    public SupplierService(SupplierRepo supplierRepo) {
        this.supplierRepo = supplierRepo;
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ClientService.class);

    public List<Supplier> findAll(){
        log.info("SupplierService: findAll");
        return supplierRepo.findAll();
    }

    public Page<Supplier> findAllPaginated(Pageable pageable){
        log.info("SupplierService: findAll");
        return supplierRepo.findAll(pageable);
    }

    public Optional<Supplier> findById(Long id){
        log.info("SupplierService: findById " + id);
        return supplierRepo.findById(id);
    }

    public Optional<Supplier> findByMobile(String mobile){
        log.info("SupplierService: findByMobile " + mobile);
        return supplierRepo.findSupplierByMobile(mobile);
    }

    public Supplier save(Supplier supplier){
        log.info("SupplierService: save ");
        return supplierRepo.save(supplier);
    }

    public void deleteById(Long id){
        log.info("SupplierService: deleteById " + id);
        supplierRepo.deleteById(id);
    }

}
