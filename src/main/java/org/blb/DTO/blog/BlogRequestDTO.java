package org.blb.DTO.blog;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BlogRequestDTO {
    @Min(1)
    private Integer id;
    @NotNull
    @NotBlank
    @Size(min = 4, max = 255)
    private String title;
    @Lob
    @NotNull
    @NotBlank
    @Size(min = 4)
    private String content;
    @Min(1)
    private Integer authorId;
}
