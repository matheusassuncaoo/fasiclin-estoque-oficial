package com.br.fasiclin.estoque.estoque.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Entidade representando uma Movimentação Contábil.
 * 
 * @author Sistema Fasiclin - Módulo Estoque
 * @version 1.0
 * @since 2025
 */
@Entity
@Table(name = "MOVCONTABIL")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"produto"})
public class MovContabil {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDMOVCONTABIL")
    private Integer id;

    @NotNull(message = "O produto deve ser informado.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PRODUTO", nullable = false)
    private Produto produto;

    @NotNull(message = "A data da movimentação é obrigatória.")
    @Column(name = "DATA_MOVIMENTACAO", nullable = false)
    private LocalDate dataMovimentacao;

    @NotNull(message = "O tipo de movimentação é obrigatório.")
    @Column(name = "TIPO_MOVIMENTACAO", nullable = false, length = 10)
    private String tipoMovimentacao; // ENTRADA ou SAIDA

    @NotNull(message = "A quantidade é obrigatória.")
    @Column(name = "QUANTIDADE", nullable = false)
    private Integer quantidade;

    @Column(name = "VALOR_UNITARIO", precision = 10, scale = 2)
    private BigDecimal valorUnitario;

    @Column(name = "VALOR_TOTAL", precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "OBSERVACAO", length = 500)
    private String observacao;
}
