package org.blb.controller.blog;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.blb.DTO.appDTO.StandardResponseDto;
import org.blb.DTO.blog.BlogsRequestDTO;
import org.blb.DTO.blog.blogs.BlogsResponseDTO;
import org.blb.DTO.blog.blogs.ContentResponseDTO;
import org.blb.DTO.blog.blogs.IdRequestDTO;
import org.blb.controller.api.blog.BlogsApi;
import org.blb.service.blog.BlogFindService;
import org.blb.service.blog.BlogUpdateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BlogsController implements BlogsApi {
    private final BlogFindService blogFindService;
    private final BlogUpdateService blogUpdateService;

    @Override
    public ResponseEntity<BlogsResponseDTO> getBlogs(Integer page, Long region) {
        return ResponseEntity.ok(blogFindService.findAll(new BlogsRequestDTO(page,region), 10));
    }

    @Override
    public ResponseEntity<BlogsResponseDTO> getBlogsByUser(Integer page) {
        return ResponseEntity.ok(blogFindService
                .findAllByUser(page,10));
    }

    @Override
    public ResponseEntity<ContentResponseDTO> getBlog(Long id) {
        return ResponseEntity.ok(blogFindService.getContent(id));
    }

    @Override
    public ResponseEntity<StandardResponseDto> addBlogViews(IdRequestDTO dto) {
        blogUpdateService.addViews(dto);
        return ResponseEntity.ok(new StandardResponseDto("Successfully added views"));
    }
}
