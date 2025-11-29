package com.uni.vendas.item.dto;

import com.uni.vendas.item.model.enums.ItemCategory;
import com.uni.vendas.user.dto.DefaultUserDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record DefaultItemDTO(
        UUID id,
        @NotBlank(message = "Name cannot be blank")
        @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
        String name,
        @NotBlank(message = "Description cannot be blank")
        @Size(min = 20, max = 2000, message = "Descrição deve ter entre 20 e 2000 caracteres")
        String description,
        @NotNull(message = "Amount is required")
        Long amount,
        @NotNull(message = "Price is required")
        BigDecimal price,
        @NotNull(message = "User ID is required")
        DefaultUserDTO soldBy,
        @NotNull(message = "Category is required")
        ItemCategory category,
        String image
) {
}
