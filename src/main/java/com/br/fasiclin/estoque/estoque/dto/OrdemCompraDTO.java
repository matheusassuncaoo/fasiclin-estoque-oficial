package com.br.fasiclin.estoque.estoque.dto;

import com.br.fasiclin.estoque.estoque.model.StatusOrdemCompra;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO para transferência de dados de Ordem de Compra.
 * 
 * @author Sistema Fasiclin - Módulo Estoque
 * @version 1.0
 * @since 2025
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdemCompraDTO {
    
    private Integer id;
    
    @NotNull(message = "O status é obrigatório")
    private StatusOrdemCompra status;
    
    @NotNull(message = "O valor é obrigatório")
    @Positive(message = "O valor deve ser positivo")
    private BigDecimal valor;
    
    @NotNull(message = "A data de previsão é obrigatória")
    @FutureOrPresent(message = "A data de previsão não pode ser no passado")
    private LocalDate dataPrevisao;
    
    @NotNull(message = "A data da ordem é obrigatória")
    private LocalDate dataOrdem;
    
    @NotNull(message = "A data de entrega é obrigatória")
    private LocalDate dataEntrega;
    
    // Lista de itens da ordem
    private List<ItemOrdemCompraDTO> itens;
}
