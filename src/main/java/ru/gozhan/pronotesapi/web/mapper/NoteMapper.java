package ru.gozhan.pronotesapi.web.mapper;

import org.mapstruct.Mapper;
import ru.gozhan.pronotesapi.domain.note.Note;
import ru.gozhan.pronotesapi.web.dto.NoteDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NoteMapper extends Mappable<Note, NoteDto> {

    @Override
    NoteDto toDto(Note entity);

    @Override
    List<NoteDto> toDto(List<Note> entity);

    @Override
    Note toEntity(NoteDto dto);

}
