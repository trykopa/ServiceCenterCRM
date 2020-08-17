package com.googe.ssadm.sc.service;

import com.googe.ssadm.sc.entity.Part;
import com.googe.ssadm.sc.repository.PartRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PartService {

    private final PartRepo partRepo;

    @Autowired
    public PartService(PartRepo partRepo) {
        this.partRepo = partRepo;
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(PartService.class);

    public List<Part> findAll(){
        log.info("PartService: findAll");
        return partRepo.findAll();
    }

    public Page<Part> findAllPaginated(Pageable pageable){
        log.info("PartService: findAllPaginated");
        return partRepo.findAll(pageable);
    }

    public Optional<Part> findById(Long id){
        log.info("PartService: findById " + id);
        return partRepo.findById(id);
    }

    public List<Part> findByPartNo(String partNo){
        log.info("PartService: findByPartNo " + partNo);
        return partRepo.findAllByPartNo(partNo);
    }

    public Optional<Part> findBySerialNo(String serialNo){
        log.info("PartService: findBySerialNo " + serialNo);
        return partRepo.findBySerialNo(serialNo);
    }

    public Part save(Part part){
        log.info("PartService: save ");
        return partRepo.save(part);
    }

    public void deleteById(Long id){
        log.info("PartService: deleteById " + id);
        partRepo.deleteById(id);
    }

}
