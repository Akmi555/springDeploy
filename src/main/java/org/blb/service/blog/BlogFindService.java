package org.blb.service.blog;

import lombok.AllArgsConstructor;
import org.blb.DTO.blog.BlogsRequestDTO;
import org.blb.DTO.blog.blogs.BlogResponseDTO;
import org.blb.DTO.blog.blogs.BlogsResponseDTO;
import org.blb.DTO.blog.blogs.ContentResponseDTO;
import org.blb.exeption.NotFoundException;
import org.blb.models.blog.Blog;
import org.blb.models.region.Region;
import org.blb.models.user.User;
import org.blb.repository.blog.BlogFindRepository;
import org.blb.repository.blog.BlogRepository;
import org.blb.service.blog.blogComment.BlCmFindService;
import org.blb.service.region.FindRegionService;
import org.blb.service.user.UserFindService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BlogFindService {


    private final BlogFindRepository blogFindRepository;
    private final FindRegionService findRegionService;
    private final BlogRepository blogRepository;
    private final BlCmFindService blCmFindService;
    private final UserFindService userFindService;

    public BlogsResponseDTO findAll(BlogsRequestDTO dto, Integer itemByPage) {
        Pageable page = PageRequest.of(dto.getPageNumber(), itemByPage);
        Page<BlogResponseDTO> blogs;
        if (dto.getRegion_ID() != 0) {
            Region region = findRegionService.getRegionById(dto.getRegion_ID());
            blogs = blogFindRepository.findDTOByRegionOrderByIdDesc(region, page);
        } else {
            blogs = blogFindRepository.findAllByOrderByIdDesc(page);
        }
        BlogsResponseDTO response = new BlogsResponseDTO(blogs.getTotalPages(), dto.getPageNumber(),
                blogs.getContent());

        return response;
    }

    public BlogsResponseDTO findAllByUser(Integer pageNumber, Integer itemByPage) {
        Pageable page = PageRequest.of(pageNumber, itemByPage);
        User user = userFindService.getUserFromContext();
        Page<BlogResponseDTO> blogs = blogFindRepository.findAllByAuthorOrderByIdDesc(page, user);

        return new BlogsResponseDTO(blogs.getTotalPages(), pageNumber,
                blogs.getContent());
    }

    public Blog findById(long id) {
        return blogRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Blog not found")
        );
    }
    public ContentResponseDTO getContent(Long id) {
       Blog blog = blogRepository.findById(id).orElseThrow(
                () ->new NotFoundException("Blog with id " + id +" not found" ));
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return new ContentResponseDTO(blog.getTitle(), blog.getPublishedDate(),
                blog.getViews(), blog.getAuthor().getName(),
                blog.getRegion().getRegionName(), blog.getContent(),
                blog.getAuthor().getEmail().equals(email),blCmFindService.getCommentsOfBlog(blog));
    }
}
