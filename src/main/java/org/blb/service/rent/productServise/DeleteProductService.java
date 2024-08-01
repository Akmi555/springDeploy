package org.blb.service.rent.productServise;

import lombok.AllArgsConstructor;
import org.blb.DTO.appDTO.OneMessageDTO;
import org.blb.exeption.NotFoundException;
import org.blb.models.rent.Product;
import org.blb.models.user.User;
import org.blb.repository.rent.ProductRepository;
import org.blb.service.user.UserFindService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class DeleteProductService {

    private final ProductRepository productRepository;
    private final UserFindService userFindService;

    public ResponseEntity<OneMessageDTO> deleteProduct(Long id) {

        User currentUser = userFindService.getUserFromContext();

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + id));

        if (product.getUser().getId().equals(currentUser.getId())) {
            productRepository.deleteById(id);
            return new ResponseEntity<>(new OneMessageDTO("Product deleted successfully"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new OneMessageDTO("You do not have permission to delete this product"), HttpStatus.CONFLICT);
        }
    }
}
