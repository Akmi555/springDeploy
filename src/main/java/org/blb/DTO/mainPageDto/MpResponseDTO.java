package org.blb.DTO.mainPageDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.blb.DTO.blog.blogs.BlogResponseDTO;
import org.blb.DTO.blog.blogs.BlogsResponseDTO;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MpResponseDTO {
    private List<MpNewsResponse> innerNews;
    private List <MpNewsResponse> sport;
    private List <MpNewsResponse> world;
    private List<BlogResponseDTO> blogs;
    private List<MpRentResponse> rent;
}
