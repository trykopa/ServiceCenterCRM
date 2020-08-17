package com.googe.ssadm.sc.controller;

import com.googe.ssadm.sc.dto.NoteDTO;
import com.googe.ssadm.sc.entity.Note;
import com.googe.ssadm.sc.entity.Order;
import com.googe.ssadm.sc.mappers.NoteMapper;
import com.googe.ssadm.sc.service.NoteService;
import com.googe.ssadm.sc.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@Controller
@RequestMapping("/notes")
public class NoteController {
    private final NoteMapper noteMapper;
    private final NoteService noteService;
    private final OrderService orderService;

    @Autowired
    public NoteController(NoteMapper noteMapper , NoteService noteService , OrderService orderService) {
        this.noteMapper = noteMapper;
        this.noteService = noteService;
        this.orderService = orderService;
    }

    @PostMapping("/addnote")
    public String addNote(@RequestParam("note-text") String text ,
                          @RequestParam("id") Long id) {
        Note note = noteMapper.toNote(new NoteDTO(text));
        Order order = orderService.findById(id).orElse(new Order());
        Set<Note> noteSet = order.getNoteSet();
        noteSet.add(note);
        order.setNoteSet(noteSet);
        note.setOrder(order);
        noteService.save(note);
        return "redirect:/orders/edit/" + id;
    }
}
