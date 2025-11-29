package com.uni.vendas.item.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.uni.vendas.item.model.enums.ItemCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.UUID;

@JsonPropertyOrder({"id", "name", "description", "amount", "price"})
public record RegisterItemDTO(
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
        UUID soldById,
        @NotNull(message = "Category is required")
        ItemCategory category,
        MultipartFile image

) {
}
