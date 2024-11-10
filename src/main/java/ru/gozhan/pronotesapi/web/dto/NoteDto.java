package ru.gozhan.pronotesapi.web.dto;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import ru.gozhan.pronotesapi.config.openapi.NoteView;

@Getter
@Setter
@Schema(description = "Note DTO")
public class NoteDto {

    @Schema(
            description = "Note ID",
            example = "1"
    )
    @JsonView({
            NoteView.Summary.class,
            NoteView.Detail.class,
    })
    private Long id;

    @Schema(
            description = "User ID",
            example = "1"
    )
    @JsonView({
            NoteView.Summary.class,
            NoteView.Detail.class,
    })
    private Long userId;

    @Schema(
            description = "Title of the note",
            example = "My first note"
    )
    @NotNull(message = "Title must be not null.")
    @Length(
            max = 255,
            message = "Title length must be smaller than 255 symbols."
    )
    @JsonView({
            NoteView.Summary.class,
            NoteView.Detail.class,
            NoteView.Create.class,
            NoteView.Update.class,
    })
    private String title;

    @Schema(
            description = "Content of the note",
            example = "This is the content of my note."
    )
    @JsonView({
            NoteView.Detail.class,
            NoteView.Create.class,
            NoteView.Update.class,
    })
    private String content;

}
