package com.googe.ssadm.sc.repository;

import com.googe.ssadm.sc.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepo extends JpaRepository<Client, Long> {
    Optional<Client> findClientByEmail(String email);
    Optional<Client> findClientByMobile(String mobile);
}
