package org.blb.service.startTest;

import lombok.AllArgsConstructor;
import org.blb.DTO.blog.BlogAddRequestDTO;
import org.blb.models.blog.Blog;
import org.blb.models.region.Region;
import org.blb.models.rent.Category;
import org.blb.models.rent.Product;
import org.blb.models.user.Role;
import org.blb.models.user.State;
import org.blb.models.user.User;
import org.blb.repository.blog.BlogRepository;
import org.blb.repository.rent.CategoryRepository;
import org.blb.repository.rent.ProductRepository;
import org.blb.repository.user.RoleRepository;
import org.blb.repository.user.UserRepository;
import org.blb.service.region.FindRegionService;
import org.blb.service.user.UserFindService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StartTestService {
    private final UserFindService userFindService;
    private final BlogRepository blogRepository;
    private final FindRegionService findRegionService;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public void startBlog() {

        Role roleUser = roleRepository.findByRole("USER");

        User user2 = new User();
        user2.setName("jams007");
        user2.setEmail("bond007@gmail.com");
        user2.setPassword(passwordEncoder.encode("Cochort40!"));
        user2.setRole(roleUser);
        user2.setState(State.CONFIRMED);
        user2.setCode("839be75f-4746-4816-ba29-628c1cc53beb");
        userRepository.save(user2);

        User user3 = new User();
        user3.setName("jams008");
        user3.setEmail("bond008@gmail.com");
        user3.setPassword(passwordEncoder.encode("Cochort40!"));
        user3.setRole(roleUser);
        user3.setState(State.CONFIRMED);
        user3.setCode("839be75f-4746-4816-ba29-628c1cc53beb");
        userRepository.save(user3);

        List<Category> categories = categoryRepository.findAll();

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

            Category category = categories.get(i % categories.size());
            System.out.println(category);
            Product product = new Product("Verkaufe Tisch", category, (double)i*10, true);
            System.out.println(product);
            product.setUser(user);
            product.setRegion(region);
            product.setLink("https://ifiwjdganyiodnmwtdlr.supabase.co/storage/v1/object/public/blb_rent/62f5a3c4-0f73-47ec-ab1e-dd2f0f397476_dinner-1433494_640.jpg");
            System.out.println(product);

            if (i < 10) {
                product.setUser(user);
            }else {
                product.setUser(user2);
                product.setDescription("A high quality Table for a good time.");
            }

            System.out.println(productRepository.save(product));
        }
    }

}
