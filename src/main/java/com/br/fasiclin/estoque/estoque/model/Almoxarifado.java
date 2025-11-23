package com.br.fasiclin.estoque.estoque.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
import jakarta.persistence.FetchType;

@Entity
@Table(name = "ALMOXARIFADO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"setor"})
public class Almoxarifado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDALMOX")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SETOR", nullable = false)
    private Setor setor;

    @Column(name = "NOMEALMO", nullable = false, unique = true, length = 100)
    private String nome;

}