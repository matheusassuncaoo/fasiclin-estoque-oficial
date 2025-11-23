package com.br.fasiclin.estoque.estoque.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para transferência de dados de Estoque.
 * 
 * @author Sistema Fasiclin - Módulo Estoque
 * @version 1.0
 * @since 2025
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstoqueDTO {
    
    private Integer id;
    
    @NotNull(message = "O produto é obrigatório")
    private Integer idProduto;
    
    @NotNull(message = "O lote é obrigatório")
    private Integer idLote;
    
    @NotNull(message = "A quantidade é obrigatória")
    @Positive(message = "A quantidade deve ser positiva")
    private Integer quantidadeEstoque;
    
    // Campos de leitura
    private String nomeProduto;
    private String numeroLote;
    private String codBarrasProduto;
}
