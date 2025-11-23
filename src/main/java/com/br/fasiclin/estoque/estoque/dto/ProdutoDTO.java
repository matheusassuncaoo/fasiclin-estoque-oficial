package com.br.fasiclin.estoque.estoque.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para transferência de dados de Produto.
 * Utilizado para desacoplar a camada de apresentação da entidade JPA.
 * 
 * @author Sistema Fasiclin - Módulo Estoque
 * @version 1.0
 * @since 2025
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoDTO {
    
    private Integer id;
    
    @NotBlank(message = "O nome do produto é obrigatório")
    @Size(max = 50, message = "O nome não pode exceder 50 caracteres")
    private String nome;
    
    @NotBlank(message = "A descrição é obrigatória")
    @Size(max = 250, message = "A descrição não pode exceder 250 caracteres")
    private String descricao;
    
    @NotNull(message = "O almoxarifado é obrigatório")
    private Integer idAlmoxarifado;
    
    @NotNull(message = "A unidade de medida é obrigatória")
    private Integer idUnidadeMedida;
    
    @NotBlank(message = "O código de barras é obrigatório")
    @Size(max = 50, message = "O código de barras não pode exceder 50 caracteres")
    private String codBarras;
    
    private BigDecimal tempIdeal;
    
    @NotNull(message = "O estoque máximo é obrigatório")
    @Positive(message = "O estoque máximo deve ser positivo")
    private Integer stqMax;
    
    @NotNull(message = "O estoque mínimo é obrigatório")
    @Positive(message = "O estoque mínimo deve ser positivo")
    private Integer stqMin;
    
    @NotNull(message = "O ponto de pedido é obrigatório")
    @Positive(message = "O ponto de pedido deve ser positivo")
    private Integer ptnPedido;
    
    // Campos de leitura apenas (não enviados na criação/atualização)
    private String nomeAlmoxarifado;
    private String siglaUnidadeMedida;
    private Integer quantidadeAtualEstoque;
}
