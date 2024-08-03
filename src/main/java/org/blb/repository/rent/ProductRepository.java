package org.blb.repository.rent;

import org.blb.models.region.Region;
import org.blb.models.rent.Category;
import org.blb.models.rent.Product;
import org.blb.models.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findById(Long id);
    Page<Product> findAllByRegionAndCategoryAndNameStartingWithIgnoreCase(Region region, Category category, String name, Pageable pageable);
    Page<Product> findAllByRegionAndCategory(Region region, Category category, Pageable pageable);
    Page<Product> findAllByRegionAndNameStartingWithIgnoreCase(Region region, String name, Pageable pageable);
    Page<Product> findAllByCategoryAndNameStartingWithIgnoreCase(Category category, String name, Pageable pageable);
    Page<Product> findAllByRegion(Region region, Pageable pageable);
    Page<Product> findAllByCategory(Category category, Pageable pageable);
    Page<Product> findByNameStartingWithIgnoreCase(String name, Pageable pageable);

    Page<Product> findAllByUser(User user, Pageable pageable);
}
