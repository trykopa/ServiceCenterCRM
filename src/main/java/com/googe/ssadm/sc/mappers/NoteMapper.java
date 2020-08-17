package com.googe.ssadm.sc.mappers;

import com.googe.ssadm.sc.dto.NoteDTO;
import com.googe.ssadm.sc.entity.Note;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface NoteMapper {
    @Mapping(source = "createdAt", target = "createdAt")
    NoteDTO toNoteDTO(Note note);

    List<NoteDTO> toNoteDTOs(List<Note> notes);

    Note toNote(NoteDTO noteDTO);
}
