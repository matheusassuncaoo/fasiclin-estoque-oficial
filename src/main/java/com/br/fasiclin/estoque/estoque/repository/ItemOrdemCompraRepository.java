package com.br.fasiclin.estoque.estoque.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.br.fasiclin.estoque.estoque.model.ItemOrdemCompra;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ItemOrdemCompraRepository extends JpaRepository<ItemOrdemCompra, Integer> {

    @Query("SELECT i FROM ItemOrdemCompra i WHERE i.ordemCompra.id = :idOrdemCompra ORDER BY i.id ASC")
    List<ItemOrdemCompra> findByOrdemCompraId(@Param("idOrdemCompra") Integer idOrdemCompra);

    @Query("SELECT i FROM ItemOrdemCompra i WHERE i.produto.id = :idProduto ORDER BY i.dataVencimento ASC")
    List<ItemOrdemCompra> findByProdutoId(@Param("idProduto") Integer idProduto);

    @Query("SELECT i FROM ItemOrdemCompra i WHERE i.dataVencimento = :dataVencimento ORDER BY i.id ASC")
    List<ItemOrdemCompra> findByDataVencimento(@Param("dataVencimento") LocalDate dataVencimento);

    @Query("SELECT i FROM ItemOrdemCompra i WHERE i.dataVencimento BETWEEN :dataInicio AND :dataFim ORDER BY i.dataVencimento ASC")
    List<ItemOrdemCompra> findByDataVencimentoBetween(@Param("dataInicio") LocalDate dataInicio,
                                                     @Param("dataFim") LocalDate dataFim);

    @Query("SELECT i FROM ItemOrdemCompra i WHERE i.dataVencimento < CURRENT_DATE ORDER BY i.dataVencimento ASC")
    List<ItemOrdemCompra> findItensVencidos();

    @Query("SELECT i FROM ItemOrdemCompra i WHERE i.dataVencimento BETWEEN CURRENT_DATE AND :dataLimite ORDER BY i.dataVencimento ASC")
    List<ItemOrdemCompra> findItensProximosVencimento(@Param("dataLimite") LocalDate dataLimite);
}