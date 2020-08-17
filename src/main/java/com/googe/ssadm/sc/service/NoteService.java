package com.googe.ssadm.sc.service;

import com.googe.ssadm.sc.entity.Note;
import com.googe.ssadm.sc.repository.NoteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private final NoteRepo noteRepo;

    @Autowired
    public NoteService(NoteRepo noteRepo) {
        this.noteRepo = noteRepo;
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(NoteService.class);


    public List<Note> findAll(){
        log.info("NoteService: findAll");
        return noteRepo.findAll();
    }

    public List<Note> findByOrderId(Long id){
        log.info("NoteService: findByOrderId" + id);
        return  noteRepo.findAllByOrder_Id(id);
    }

    public Note save(Note note){
        log.info("NoteService: save" + note.getMessage());
        return noteRepo.save(note);
    }

    public void deleteById(Long id){
        log.info("NoteService: deleteById" + id);
        noteRepo.deleteById(id);
    }
}
