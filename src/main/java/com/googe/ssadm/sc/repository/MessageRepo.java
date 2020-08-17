package com.googe.ssadm.sc.repository;

import com.googe.ssadm.sc.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepo extends JpaRepository<Message, Long> {
    List<Message> findAllMessagesByUnreadIsTrueAndToUser_Email(String email);
    Page<Message> findByToUser_Id(Pageable pageable, Long id);
    Page<Message> findByFromUser_Id(Pageable pageable, Long id);
}
