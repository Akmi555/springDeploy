package org.blb.service.blog;

import lombok.AllArgsConstructor;
import org.blb.DTO.appDTO.StandardResponseDto;
import org.blb.DTO.blog.BlogAddRequestDTO;
import org.blb.models.blog.Blog;
import org.blb.models.region.Region;
import org.blb.models.user.User;
import org.blb.repository.blog.BlogRepository;
import org.blb.repository.region.RegionRepository;
import org.blb.service.region.FindRegionService;
import org.blb.service.user.UserFindService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BlogDataService {
    private final RegionRepository regionRepository;
    private final FindRegionService findRegionService;
    BlogRepository blogRepository;
    UserFindService userFindService;


    public StandardResponseDto addBlog(BlogAddRequestDTO blog) {
        User user = userFindService.getUserFromContext();
        Region region = findRegionService.getRegionById(blog.getRegion());
        Blog newBlog = blog.dtoToBlog(user, region);
        blogRepository.save(newBlog);
        return new StandardResponseDto("Blog added successfully");
    }
}
