package org.example.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record OrderRequestDTO(
                              @NotEmpty(message = "Ordern måste innehålla minst en produkt.")
                              @Valid
                              List<OrderItemDTO> orderItemList
                              ) {
}
