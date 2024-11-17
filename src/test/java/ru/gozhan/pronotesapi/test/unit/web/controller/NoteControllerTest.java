package ru.gozhan.pronotesapi.test.unit.web.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.gozhan.pronotesapi.config.SecurityConfig;
import ru.gozhan.pronotesapi.domain.note.Note;
import ru.gozhan.pronotesapi.service.NoteService;
import ru.gozhan.pronotesapi.test.config.security.WithMockJwtEntity;
import ru.gozhan.pronotesapi.test.constant.ApiEndpoint;
import ru.gozhan.pronotesapi.test.data.builder.NoteBuilder;
import ru.gozhan.pronotesapi.test.data.builder.NoteDtoBuilder;
import ru.gozhan.pronotesapi.test.unit.AbstractUnitTest;
import ru.gozhan.pronotesapi.test.util.JsonUtil;
import ru.gozhan.pronotesapi.test.util.MockMvcUtil;
import ru.gozhan.pronotesapi.web.controller.NoteController;
import ru.gozhan.pronotesapi.web.dto.NoteDto;
import ru.gozhan.pronotesapi.web.mapper.NoteMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NoteController.class)
@Import(SecurityConfig.class)
@ActiveProfiles("test")
public class NoteControllerTest extends AbstractUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteService noteService;

    @MockBean
    private NoteMapper noteMapper;

    @Test
    @DisplayName("""
            API Endpoint GET /notes.
            Given authenticated user.
            Then returns notes without content.
            """)
    @WithMockJwtEntity
    @SneakyThrows
    void givenUser_whenGetAllNotes_thenReturnsNotesWithoutContent() {
        // given
        Note note1 = new NoteBuilder().build();
        Note note2 = new NoteBuilder()
                .id(2L)
                .title("Title 2 of my note")
                .content("Content 2 of my note")
                .build();

        NoteDto noteDto1 = new NoteDtoBuilder().build();
        NoteDto noteDto2 = new NoteDtoBuilder()
                .id(2L)
                .title("Title 2 of my note")
                .content("Content 2 of my note")
                .build();

        when(noteService.getAllByUserIdWithoutContent(1L))
                .thenReturn(List.of(note1, note2));

        when(noteMapper.toDto(anyList()))
                .thenReturn(List.of(noteDto1, noteDto2));

        var request = get(ApiEndpoint.GET_NOTES.getPath());

        // when
        var response = mockMvc.perform(request)
                .andDo(MockMvcUtil.prettyPrintRequestAndResponse());

        // then
        response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].userId").value(1L))
                .andExpect(jsonPath("$[0].title").value("Title of my note"))
                .andExpect(jsonPath("$[0].content").doesNotExist())
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].userId").value(1L))
                .andExpect(jsonPath("$[1].title").value("Title 2 of my note"))
                .andExpect(jsonPath("$[1].content").doesNotExist())
                .andExpect(jsonPath("$[2]").doesNotExist());
    }

    @Test
    @DisplayName("""
            API Endpoint GET /notes/{userId}.
            Given authenticated user.
            Then returns notes without content.
            """)
    @WithMockJwtEntity
    @SneakyThrows
    void givenUser_whenNoteById_thenReturnsNote() {
        // given
        Note note = new NoteBuilder().build();

        NoteDto noteDto = new NoteDtoBuilder().build();

        when(noteService.getById(1L))
                .thenReturn(note);

        when(noteMapper.toDto(any(Note.class)))
                .thenReturn(noteDto);

        var request = get(
                String.format(ApiEndpoint.GET_NOTES_BY_ID.getPath(), 1L)
        );

        // when
        var response = mockMvc.perform(request)
                .andDo(MockMvcUtil.prettyPrintRequestAndResponse());

        // then
        response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.title").value("Title of my note"))
                .andExpect(jsonPath("$.content").value("Content of my note"));
    }

    @Test
    @DisplayName("""
            API Endpoint POST /notes.
            Given note.
            Then returns created note.
            """)
    @WithMockJwtEntity
    @SneakyThrows
    void givenNote_whenCreateNote_thenReturnsNote() {
        // given
        NoteDto inputNoteDto = new NoteDtoBuilder()
                .id(null)
                .userId(null)
                .build();

        Note inputNote = new NoteBuilder()
                .id(null)
                .userId(1L)
                .build();

        Note createdNote = new NoteBuilder()
                .id(1L)
                .userId(1L)
                .build();

        NoteDto createdNoteDto = new NoteDtoBuilder()
                .id(1L)
                .userId(1L)
                .build();

        when(noteMapper.toEntity(any(NoteDto.class)))
                .thenReturn(inputNote);

        when(noteService.save(inputNote))
                .thenReturn(createdNote);

        when(noteMapper.toDto(any(Note.class)))
                .thenReturn(createdNoteDto);

        var request = post(ApiEndpoint.POST_NOTE.getPath())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(inputNoteDto));

        // when
        var response = mockMvc.perform(request)
                .andDo(MockMvcUtil.prettyPrintRequestAndResponse());

        // then
        response
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.title").value("Title of my note"))
                .andExpect(jsonPath("$.content").value("Content of my note"));
    }

    @Test
    @DisplayName("""
            API Endpoint PUT /notes/{noteId}.
            Given note.
            Then returns update note.
            """)
    @WithMockJwtEntity
    @SneakyThrows
    void givenNote_whenUpdateNote_thenReturnsNote() {
        // given
        NoteDto inputNoteDto = new NoteDtoBuilder()
                .id(null)
                .userId(null)
                .title("New title")
                .content("New content")
                .build();

        Note inputNote = new NoteBuilder()
                .id(1L)
                .userId(1L)
                .title("New title")
                .content("New content")
                .build();

        Note updatedNote = new NoteBuilder()
                .id(1L)
                .userId(1L)
                .title("New title")
                .content("New content")
                .build();

        NoteDto updatedNoteDto = new NoteDtoBuilder()
                .id(1L)
                .userId(1L)
                .title("New title")
                .content("New content")
                .build();

        when(noteMapper.toEntity(any(NoteDto.class)))
                .thenReturn(inputNote);

        when(noteService.save(inputNote))
                .thenReturn(updatedNote);

        when(noteMapper.toDto(any(Note.class)))
                .thenReturn(updatedNoteDto);

        var request = put(String.format(ApiEndpoint.PUT_NOTE_BY_ID.getPath(), 1L))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(inputNoteDto));

        // when
        var response = mockMvc.perform(request)
                .andDo(MockMvcUtil.prettyPrintRequestAndResponse());

        // then
        response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*").doesNotExist());
    }

    @Test
    @DisplayName("""
            API Endpoint DELETE /notes/{noteId}.
            Given noteId.
            Then delete note with NO CONTENT.
            """)
    @WithMockJwtEntity
    @SneakyThrows
    void givenNoteId_whenDeleteNote_thenNoContent() {
        // given
        Long noteId = 1L;

        var request = delete(
                String.format(ApiEndpoint.DELETE_NOTE_BY_ID.getPath(), noteId)
        );

        // when
        var response = mockMvc.perform(request)
                .andDo(MockMvcUtil.prettyPrintRequestAndResponse());

        // then
        response
                .andExpect(status().isNoContent());
        verify(noteService).delete(noteId);
    }

}
