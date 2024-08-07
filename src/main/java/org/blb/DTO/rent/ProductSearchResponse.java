package org.blb.DTO.rent;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.blb.DTO.errorDto.ErrorResponseDto;
import org.blb.DTO.rent.productDto.ProductResponseDto;


import java.util.List;

@Data
@AllArgsConstructor
public class ProductSearchResponse {
    private List<ProductResponseDto> products;
    private ErrorResponseDto error;
    private long currentPage;
    private int totalPages;
}
