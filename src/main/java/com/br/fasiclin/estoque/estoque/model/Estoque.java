package com.br.fasiclin.estoque.estoque.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Table(name = "ESTOQUE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"produto", "lote"})
public class Estoque {

    @Id
    @GeneratedValue
    @Column(name = "IDESTOQUE")
    private Integer id;

    @NotNull(message = "O produto deve ser informado.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PRODUTO", nullable = false)
    private Produto produto;

    @NotNull(message = "O lote deve ser informado.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_LOTE", nullable = false)
    private Lote lote;

    @NotNull(message = "A quantidade deve ser informada.")
    @Column(name = "QTDESTOQUE", nullable = false)
    private Integer quantidadeEstoque;
  
}