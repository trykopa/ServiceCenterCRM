package com.googe.ssadm.sc.service;

import com.googe.ssadm.sc.entity.Client;
import com.googe.ssadm.sc.repository.ClientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepo clientRepo;

    @Autowired
    public ClientService(ClientRepo clientRepo) {
        this.clientRepo = clientRepo;
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ClientService.class);

    public List<Client> findAll(){
        log.info("ClientService: findAll");
        return clientRepo.findAll();
    }

    public Optional<Client> findById(Long id){
        log.info("ClientService: findById");
        return clientRepo.findById(id);
    }

    public Optional<Client> findByEmail(String email){
        log.info("ClientService: findByEmail, {}", email);
        return clientRepo.findClientByEmail(email);
    }

    public Optional<Client> findByMobile(String mobile){
        log.info("ClientService: findClientByMobile, {}", mobile);
        return clientRepo.findClientByMobile(mobile);
    }

    public Client save(Client client){
        log.info("ClientService: saveOrUpdate, {}", client.getId());
        return clientRepo.save(client);
    }

    public void deleteById(Long id){
        log.info("ClientService: deleteById, {}", id);
        clientRepo.deleteById(id);
    }

}
