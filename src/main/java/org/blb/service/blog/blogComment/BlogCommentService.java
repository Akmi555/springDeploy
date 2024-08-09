package org.blb.service.blog.blogComment;

import lombok.AllArgsConstructor;
import org.blb.DTO.appDTO.StandardDelRequest;
import org.blb.DTO.blog.blogs.BlogCommentRequestDTO;
import org.blb.exeption.NotFoundException;
import org.blb.exeption.RestException;
import org.blb.models.blog.Blog;
import org.blb.models.blog.BlogComment;
import org.blb.models.user.Role;
import org.blb.models.user.User;
import org.blb.repository.user.RoleRepository;
import org.blb.repository.blog.BlogCommentRepository;
import org.blb.repository.blog.BlogRepository;
import org.blb.service.blog.BlogDataService;
import org.blb.service.blog.BlogFindService;
import org.blb.service.user.UserFindService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BlogCommentService {
    private final BlogFindService blogFindService;
    private final BlogCommentRepository blogCommentRepository;
    private final UserFindService userFindService;
    private final RoleRepository roleRepository;
    private final BlogDataService blogDataService;
    private final BlogRepository blogRepository;

    public void addComment(BlogCommentRequestDTO dto) {
        User user = userFindService.getUserFromContext();
        Blog blog = blogFindService.findById(dto.getBlogId());
        blog.setComments(blog.getComments()+1);
        blogRepository.save(blog);
        blogCommentRepository.save(new BlogComment(dto.getComment(), user, blog));
    }
    public void remove(StandardDelRequest dto) {
        User user = userFindService.getUserFromContext();
        BlogComment comment = blogCommentRepository.findById(dto.getId())
                .orElseThrow(()-> new NotFoundException("Comment not found"));
        Role role = roleRepository.findByRole("ADMIN");
        if (user != comment.getUser() && user.getRole() != role){
            throw new RestException(HttpStatus.CONFLICT, "You dont have permission to dell this message");
        }
        Blog blog = comment.getBlog();
        blog.setComments(blog.getComments()-1);
        blogRepository.save(blog);
        blogCommentRepository.delete(comment);
    }
    public void removeByBlog(Blog blog){
        blogCommentRepository.deleteAll(blogCommentRepository.findAllByBlog(blog));
    }



}
