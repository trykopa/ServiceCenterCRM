package com.googe.ssadm.sc.service;

import com.googe.ssadm.sc.entity.User;
import com.googe.ssadm.sc.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepo userRepo;

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    public List<User> findAll(){
        log.info("UserService: findAll");
        return userRepo.findAll();
    }

    public Optional<User> findById(Long id){
        log.info("UserService: findById");
        return userRepo.findById(id);
    }

    public Optional<User> findByEmail(String email){
        log.info("UserService: findByEmail {}", email);
        return userRepo.findUserByEmail(email);
    }

    public Optional<User> findByFullName(String name, String surname){
        log.info("UserService: findFirstByNameAndSurname {}", name + ' ' +surname);
        return userRepo.findUserByNameAndSurname(name, surname);
    }

    public User save(User user){
        log.info("UserService: saveOrUpdate, {}", user.getId());
        return userRepo.save(user);
    }

    public void deleteById(Long id){
        log.info("UserService: deleteById, {}", id);
        userRepo.deleteById(id);
    }

}
