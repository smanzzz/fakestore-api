package org.example.dto;

import java.math.BigDecimal;
import java.math.BigInteger;

public record FakeStoreProductResponseDTO(Long id,
                                 String title,
                                 BigDecimal price,
                                 String description,
                                 String category,
                                 String image
                                 ) {
}
