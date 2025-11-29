package com.uni.vendas.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record DefaultUserDTO(
        UUID id,
        @NotBlank(message = "Name cannot be blank")
        @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
        String name,
        @NotBlank(message = "Email cannot be blank")
        @Size(min = 3, max = 100, message = "Email deve ter entre 3 e 100 caracteres")
        @Email(regexp = "^[a-zA-Z0-9._%+-]+@dcx.ufpb.br$", message = "Invalid email format")
        String email,
        @NotBlank(message = "Phone Number cannot be blank")
        @Size(min = 3, max = 15, message = "Número de celular deve ter entre 3 e 15 caracteres")
        String phoneNumber,
        @NotBlank(message = "City cannot be blank")
        @Size(min = 3, max = 50, message = "Cidade deve ter entre 3 e 50 caracteres")
        String city,
        String image) {}
