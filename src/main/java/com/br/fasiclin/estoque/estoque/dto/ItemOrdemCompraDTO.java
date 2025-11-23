package com.br.fasiclin.estoque.estoque.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para transferência de dados de Item de Ordem de Compra.
 * 
 * @author Sistema Fasiclin - Módulo Estoque
 * @version 1.0
 * @since 2025
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemOrdemCompraDTO {
    
    private Integer id;
    
    @NotNull(message = "A ordem de compra é obrigatória")
    private Integer idOrdemCompra;
    
    @NotNull(message = "O produto é obrigatório")
    private Integer idProduto;
    
    @NotNull(message = "A quantidade é obrigatória")
    @Positive(message = "A quantidade deve ser positiva")
    private Integer quantidade;
    
    @NotNull(message = "O valor unitário é obrigatório")
    @Positive(message = "O valor unitário deve ser positivo")
    private BigDecimal valorUnitario;
    
    // Campos calculados
    private BigDecimal valorTotal;
    private String nomeProduto;
}
