package com.googe.ssadm.sc.repository;

import com.googe.ssadm.sc.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepo extends JpaRepository<Note, Long> {
    List<Note> findAllByOrder_Id(Long orderId);
 }
