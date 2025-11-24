package com.br.fasiclin.estoque.estoque.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeactivationRequestDTO {
    @NotBlank(message = "Login é obrigatório")
    private String login;

    @NotBlank(message = "Senha é obrigatória")
    private String senha;

    @NotBlank(message = "Motivo é obrigatório")
    private String motivo;
}
