package org.blb.service.blog;

import lombok.AllArgsConstructor;
import org.blb.DTO.appDTO.StandardDelRequest;
import org.blb.DTO.blog.BlogUpdateDTO;
import org.blb.DTO.blog.blogs.IdRequestDTO;
import org.blb.exeption.RestException;
import org.blb.models.blog.Blog;
import org.blb.models.region.Region;
import org.blb.models.user.Role;
import org.blb.models.user.User;
import org.blb.repository.user.RoleRepository;
import org.blb.repository.blog.BlogRepository;
import org.blb.service.blog.blogComment.BlogCommentService;
import org.blb.service.region.FindRegionService;
import org.blb.service.user.UserFindService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BlogUpdateService {
    private final BlogFindService blogFindService;
    private final UserFindService userFindService;
    private final RoleRepository roleRepository;
    private final BlogRepository blogRepository;
    private final FindRegionService findRegionService;
    private final BlogCommentService blogCommentService;


    public void remove(StandardDelRequest dto) {
        Blog blog = getBlogForUpdate(dto.getId());
        blogCommentService.removeByBlog(blog);
        blogRepository.deleteById(blog.getId());
    }

    public void addViews(IdRequestDTO dto) {
        Blog blog = blogFindService.findById(dto.getId());
        blog.setViews(blog.getViews() + 1);
        blogRepository.save(blog);
    }

    public void update(BlogUpdateDTO dto) {
        Blog blog = getBlogForUpdate(dto.getId());
        Region region = findRegionService.
                getRegionById(dto.getBlogAddRequestDTO().getRegion());
        dto.DtoToBlog(blog, region);
        blogRepository.save(blog);
    }

    private Blog getBlogForUpdate (Long id){
        User user = userFindService.getUserFromContext();
        Blog blog = blogFindService.findById(id);
        Role role = roleRepository.findByRole("ADMIN");
        if (user != blog.getAuthor() && user.getRole() != role){
            throw new RestException(HttpStatus.CONFLICT, "You dont have permission to update this blog");
        }
        return blog;
    }
}
