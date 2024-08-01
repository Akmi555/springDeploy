package org.blb.DTO.blog;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BlogsRequestDTO {
    Integer pageNumber;
    Long region_ID;
}
