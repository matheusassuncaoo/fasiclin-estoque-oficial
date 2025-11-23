package com.br.fasiclin.estoque.estoque.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "UNIMEDIDA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnidadeMedida {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDUNMEDI")
    private Integer id;

    @Column(name = "DESCRICAO", nullable = false, length = 50)
    private String descricao;

    @Column(name = "UNIABREV", nullable = false, unique = true, length = 3)
    private String abreviacao;
}