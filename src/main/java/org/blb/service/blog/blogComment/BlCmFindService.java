package org.blb.service.blog.blogComment;

import lombok.AllArgsConstructor;
import org.blb.DTO.blog.blogs.BlogCommentResponseDTO;
import org.blb.models.blog.Blog;
import org.blb.repository.blog.BlogCommentRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BlCmFindService {
    private final BlogCommentRepository blogCommentRepository;
    public List<BlogCommentResponseDTO> getCommentsOfBlog(Blog blog) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return blogCommentRepository.findAllByBlog(blog).stream()
                .map(comment -> new BlogCommentResponseDTO(comment.getId(), comment.getComment(),
                        comment.getCommentDate(), comment.getUser().getName()
                        ,email.equals(comment.getUser().getEmail())
                )).toList();
    }
}
