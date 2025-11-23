package com.br.fasiclin.estoque.estoque.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.fasiclin.estoque.estoque.model.Estoque;

import java.util.Optional;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Integer> {
    
    /**
     * Busca estoque por produto e lote
     */
    Optional<Estoque> findByProdutoIdAndLoteId(Integer produtoId, Integer loteId);
}
