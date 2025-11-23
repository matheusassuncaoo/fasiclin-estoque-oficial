package com.br.fasiclin.estoque.estoque.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para transferência de dados de Fornecedor.
 * 
 * @author Sistema Fasiclin - Módulo Estoque
 * @version 1.0
 * @since 2025
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FornecedorDTO {
    
    private Integer id;
    
    @NotNull(message = "A pessoa jurídica é obrigatória")
    private Integer idPessoaJuridica;
    
    @Size(max = 100, message = "O nome do representante não pode exceder 100 caracteres")
    private String representante;
    
    @Size(max = 15, message = "O contato não pode exceder 15 caracteres")
    private String contatoRepresentante;
    
    @Size(max = 250, message = "As condições não podem exceder 250 caracteres")
    private String condicoesPagamento;
    
    // Campos de leitura
    private String razaoSocial;
    private String cnpj;
}
