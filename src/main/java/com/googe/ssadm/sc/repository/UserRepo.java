package com.googe.ssadm.sc.repository;

import com.googe.ssadm.sc.entity.Message;
import com.googe.ssadm.sc.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findUserByNameAndSurname(String name, String surname);
    Optional<User> findUserByEmail(String email);

    @Override
    @EntityGraph(attributePaths = "roles")
    List<User> findAll();
}
