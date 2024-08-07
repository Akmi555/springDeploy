package org.blb.service.rent.productServise;

import lombok.AllArgsConstructor;
import org.blb.DTO.rent.ProductSearchResponse;
import org.blb.DTO.rent.productDto.ProductResponseDto;
import org.blb.exeption.NotFoundException;
import org.blb.models.rent.Product;
import org.blb.models.user.User;
import org.blb.repository.rent.ProductRepository;
import org.blb.service.user.UserFindService;
import org.blb.service.util.rentMapping.ProductConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AllProductsByUserService {

    private final ProductRepository productRepository;
    private final UserFindService userFindService;
    public final ProductConverter productConverter;
    private final int defaultPageSize = 10;

    public ProductSearchResponse findUserProducts(int page) {
        User currentUser = userFindService.getUserFromContext();
        Pageable pageable = PageRequest.of(page, defaultPageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page<Product> productPage = productRepository.findAllByUser(currentUser, pageable);

        if (productPage.isEmpty()) {
            throw new NotFoundException("You have not products yet.");
        }

        List<ProductResponseDto> products = productPage.getContent().stream()
                .map(productConverter::toDto)
                .collect(Collectors.toList());

        return new ProductSearchResponse(products, null, page, productPage.getTotalPages());
    }
}
