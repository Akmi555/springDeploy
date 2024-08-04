package org.blb.service.startTest;

import lombok.AllArgsConstructor;
import org.blb.DTO.blog.BlogAddRequestDTO;
import org.blb.models.blog.Blog;
import org.blb.models.blog.BlogComment;
import org.blb.models.region.Region;
import org.blb.models.rent.Category;
import org.blb.models.rent.Product;
import org.blb.models.user.User;
import org.blb.repository.blog.BlogCommentRepository;
import org.blb.repository.blog.BlogRepository;
import org.blb.repository.rent.CategoryRepository;
import org.blb.repository.rent.ProductRepository;
import org.blb.service.region.FindRegionService;
import org.blb.service.user.UserFindService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StartTestService {
    private final UserFindService userFindService;
    private final BlogRepository blogRepository;
    private final FindRegionService findRegionService;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BlogCommentRepository blogCommentRepository;
    public void startBlog() {
        for (int i = 0; i < 20; i++) {
            int reg = 2;
            if (i < 17) {
                reg += i;
            } else {
                reg = i - 15;
            }
            BlogAddRequestDTO dto = new BlogAddRequestDTO("some title N" + i, "Lorem ipsum dolor sit amet. Et deleniti dolor qui quisquam galisum quo aspernatur consequatur ut vero minima et commodi pariatur. Sed repellendus voluptatem et voluptatem vero et galisum praesentium ut voluptate nostrum quo pariatur accusantium non nesciunt omnis.\n" +
                    "\n" +
                    "Id placeat deleniti quo vitae dolorem vel illum quia aut sunt vero! Eos cupiditate maxime et earum impedit nam voluptas neque nam perspiciatis iusto At internos accusantium. Id odio voluptates a quam dolorem et impedit voluptate qui sapiente aliquid id voluptatem sequi! Ut illum iure sed nisi distinctio a quos autem non minima voluptas rem inventore neque.\n" +
                    "\n" +
                    "Qui vitae iste eos internos dolor ea ipsa temporibus hic rerum omnis et quos quasi eos quia natus sed dolores voluptas. Ut architecto galisum aut commodi laboriosam et voluptates molestias sed quis fugit. Qui deleniti eligendi est exercitationem repudiandae At sunt quibusdam sed optio ipsum et tempore animi.",
                     (long) reg);
            User user = userFindService.findUserById((long) 1);
            Region region = findRegionService.getRegionById((long) reg);
            Blog blog = dto.dtoToBlog(user, region);
            blogRepository.save(blog);
            Category category = categoryRepository.findById((long)1).get();
            Product product = new Product("Verkaufe Tish", category, (double)i*10, true);
            product.setUser(user);
            product.setRegion(region);
            blogCommentRepository.save(new BlogComment("some commen", user, blog));
        }
    }

}
