package org.blb.service.startTest;

import lombok.AllArgsConstructor;
import org.blb.DTO.blog.BlogAddRequestDTO;
import org.blb.models.blog.Blog;
import org.blb.models.blog.BlogComment;
import org.blb.models.region.Region;
import org.blb.models.rent.Category;
import org.blb.models.rent.Product;
import org.blb.models.user.Role;
import org.blb.models.user.State;
import org.blb.models.user.User;
import org.blb.repository.blog.BlogCommentRepository;
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

    private final BlogCommentRepository blogCommentRepository;

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public void startBlog() {

        productRepository.deleteAll();

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
        //    BlogAddRequestDTO dto = new BlogAddRequestDTO("some title N" + i, "Lorem ipsum dolor sit amet. Et deleniti dolor qui quisquam galisum quo aspernatur consequatur ut vero minima et commodi pariatur. Sed repellendus voluptatem et voluptatem vero et galisum praesentium ut voluptate nostrum quo pariatur accusantium non nesciunt omnis.\n" +
        //            "\n" +
        //            "Id placeat deleniti quo vitae dolorem vel illum quia aut sunt vero! Eos cupiditate maxime et earum impedit nam voluptas neque nam perspiciatis iusto At internos accusantium. Id odio voluptates a quam dolorem et impedit voluptate qui sapiente aliquid id voluptatem sequi! Ut illum iure sed nisi distinctio a quos autem non minima voluptas rem inventore neque.\n" +
        //            "\n" +
        //            "Qui vitae iste eos internos dolor ea ipsa temporibus hic rerum omnis et quos quasi eos quia natus sed dolores voluptas. Ut architecto galisum aut commodi laboriosam et voluptates molestias sed quis fugit. Qui deleniti eligendi est exercitationem repudiandae At sunt quibusdam sed optio ipsum et tempore animi.",
        //             (long) reg);
            User user = userFindService.findUserById((long) 1);
            Region region = findRegionService.getRegionById((long) reg);
        //    Blog blog = dto.dtoToBlog(user, region);
        //    blogRepository.save(blog);


            Category category = categories.get(i % categories.size());
            System.out.println(category);
            double price = Math.ceil(i * 1.2);
            Product product = new Product("Auto", category, price, true);
            System.out.println(product);
            product.setUser(user);
            product.setRegion(region);
            product.setDescription("Fast neu.");
            System.out.println(product);

            if (i < 7) {
                product.setName("SUP");
                product.setUser(user);
                product.setDescription("Ein hochwertiger SUP für eine gute Zeit.");
                product.setLink("https://ifiwjdganyiodnmwtdlr.supabase.co/storage/v1/object/public/blb_rent/f7f21230-b28f-4031-84f5-39c8f4fe02f8_photo-1590131026302-846cff2287b1.jpg");
            }else if (i < 14) {
                product.setName("Fahrrad");
                product.setUser(user2);
                product.setDescription("Ein hochwertiger Fahrrad für eine gute Zeit.");
                product.setLink("https://ifiwjdganyiodnmwtdlr.supabase.co/storage/v1/object/public/blb_rent/dff32705-57fc-46bc-9f45-5ec299c5aaba_photo-1643537052325-1b28b742fba5 (1).jpg");
            }else {
                product.setName("Auto");
                product.setUser(user);
                product.setLink("https://ifiwjdganyiodnmwtdlr.supabase.co/storage/v1/object/public/blb_rent/10e6327f-3844-47e0-8c93-3a7a4a25d24d_photo-1676288176903-a68732722cce.jpg");
            }

            System.out.println(productRepository.save(product));

        }
    }

}
