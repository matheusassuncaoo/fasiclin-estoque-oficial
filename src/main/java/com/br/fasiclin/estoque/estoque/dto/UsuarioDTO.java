package com.br.fasiclin.estoque.estoque.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para transferência de dados de Usuário.
 * A senha nunca é retornada nas consultas (segurança).
 * 
 * @author Sistema Fasiclin - Módulo Estoque
 * @version 1.0
 * @since 2025
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    
    private Integer id;
    
    @NotBlank(message = "O nome de usuário é obrigatório")
    @Size(max = 50, message = "O nome de usuário não pode exceder 50 caracteres")
    private String nomeUsuario;
    
    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, max = 255, message = "A senha deve ter entre 6 e 255 caracteres")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Apenas escrita, nunca retorna
    private String senha;
    
    @NotBlank(message = "O nome completo é obrigatório")
    @Size(max = 100, message = "O nome completo não pode exceder 100 caracteres")
    private String nomeCompleto;
    
    @Email(message = "Email inválido")
    @Size(max = 100, message = "O email não pode exceder 100 caracteres")
    private String email;
    
    private Boolean ativo;
}
