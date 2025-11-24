package com.br.fasiclin.estoque.estoque.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.br.fasiclin.estoque.estoque.model.MovContabil;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Repository para operações de banco de dados da entidade MovContabil.
 * 
 * <p>
 * Este repository fornece métodos otimizados para consultas na tabela
 * MOVCONTABIL
 * do banco de dados MySQL. Todas as consultas utilizam QueryHints para melhor
 * performance:
 * </p>
 * 
 * <ul>
 * <li><strong>readOnly:</strong> Otimiza consultas somente leitura</li>
 * <li><strong>fetchSize:</strong> Define tamanho do lote para busca (50
 * registros)</li>
 * <li><strong>cacheable:</strong> Habilita cache de segundo nível do
 * Hibernate</li>
 * <li><strong>timeout:</strong> Define timeout de 2 segundos para
 * consultas</li>
 * </ul>
 * 
 * @author Sistema Fasiclin - Módulo Estoque
 * @version 1.0
 * @since 2025
 */
@Repository
public interface MovContabilRepository extends JpaRepository<MovContabil, Integer> {

    /**
     * Busca uma movimentação contábil pelo ID.
     * 
     * <p>
     * <strong>Coluna do banco:</strong> IDMOVCONTAB (INT, PRIMARY KEY,
     * AUTO_INCREMENT)
     * </p>
     * 
     * @param id ID da movimentação contábil
     * @return Optional contendo a movimentação encontrada ou vazio se não existir
     */
    // Métodos anteriores removidos: entidade atual não possui campos antigos usados nas queries.
    // Utilizar findById padrão do JpaRepository.

    /**
     * Busca movimentação contábil pelo número do lançamento.
     * 
     * <p>
     * <strong>Coluna do banco:</strong> NUMELANCAM (INT, NOT NULL, UNIQUE)
     * </p>
     * 
     * @param numeroLancamento Número único do lançamento
     * @return Optional contendo a movimentação encontrada ou vazio se não existir
     */
    // Removido: campo numeLancam não existe na entidade atual.

    /**
     * Busca movimentações contábeis por ID da ordem de compra.
     * 
     * <p>
     * <strong>Coluna do banco:</strong> ID_ORDCOMP (INT, FK ->
     * ORDEMCOMPRA(IDORDCOMP))
     * </p>
     * 
     * @param idOrdComp ID da ordem de compra
     * @return Lista de movimentações da ordem de compra especificada
     */
    // Novos métodos baseados nos campos reais da entidade
    List<MovContabil> findByProduto_Id(Integer produtoId);

    /**
     * Busca movimentações contábeis por ID do item de venda.
     * 
     * <p>
     * <strong>Coluna do banco:</strong> ID_ITEMVENDA (INT, FK ->
     * ITEMVENDA(IDITEMVENDA))
     * </p>
     * 
     * @param idItemVenda ID do item de venda
     * @return Lista de movimentações do item de venda especificado
     */
    List<MovContabil> findByTipoMovimentacao(String tipoMovimentacao);

    /**
     * Busca movimentações contábeis por ID do plano de contas.
     * 
     * <p>
     * <strong>Coluna do banco:</strong> ID_PLANOCONTA (INT, NOT NULL, FK ->
     * PLANOCONTA(IDPLANOCONTA))
     * </p>
     * 
     * @param idPlanoConta ID do plano de contas
     * @return Lista de movimentações do plano de contas especificado
     */
    List<MovContabil> findByQuantidade(Integer quantidade);

    /**
     * Busca movimentações contábeis por data de lançamento.
     * 
     * <p>
     * <strong>Coluna do banco:</strong> DATALANCAME (DATE)
     * </p>
     * 
     * @param dataLancamento Data do lançamento
     * @return Lista de movimentações na data especificada
     */
    List<MovContabil> findByDataMovimentacao(LocalDate dataMovimentacao);

    /**
     * Busca movimentações contábeis em uma faixa de datas.
     * 
     * <p>
     * Útil para relatórios contábeis por período.
     * </p>
     * 
     * @param dataInicio Data inicial
     * @param dataFim    Data final
     * @return Lista de movimentações na faixa de datas especificada
     */
    List<MovContabil> findByDataMovimentacaoBetween(LocalDate dataInicio, LocalDate dataFim);

    /**
     * Busca movimentações contábeis por faixa de valor de débito.
     * 
     * <p>
     * <strong>Coluna do banco:</strong> VALDBTO (DECIMAL(10,2), NOT NULL)
     * </p>
     * 
     * @param valorMinimo Valor mínimo de débito
     * @param valorMaximo Valor máximo de débito
     * @return Lista de movimentações na faixa de débito especificada
     */
    List<MovContabil> findByValorUnitarioBetween(BigDecimal valorMinimo, BigDecimal valorMaximo);

    /**
     * Busca movimentações contábeis por faixa de valor de crédito.
     * 
     * <p>
     * <strong>Coluna do banco:</strong> VALCDTO (DECIMAL(10,2), NOT NULL)
     * </p>
     * 
     * @param valorMinimo Valor mínimo de crédito
     * @param valorMaximo Valor máximo de crédito
     * @return Lista de movimentações na faixa de crédito especificada
     */
    // Valores totais entre faixa
    List<MovContabil> findByValorTotalBetween(BigDecimal valorMinimo, BigDecimal valorMaximo);

    /**
     * Busca movimentações contábeis balanceadas (débito = crédito).
     * 
     * <p>
     * Consulta otimizada para auditoria contábil.
     * </p>
     * 
     * @return Lista de movimentações balanceadas
     */
    // Consultas agregadas
    @Query("SELECT COALESCE(SUM(m.valorTotal),0) FROM MovContabil m WHERE m.produto.id = :produtoId")
    BigDecimal sumValorTotalByProduto(@Param("produtoId") Integer produtoId);

    /**
     * Busca movimentações contábeis desbalanceadas (débito ≠ crédito).
     * 
     * <p>
     * Consulta otimizada para auditoria contábil.
     * </p>
     * 
     * @return Lista de movimentações desbalanceadas
     */
    @Query("SELECT COALESCE(SUM(m.quantidade),0) FROM MovContabil m WHERE m.produto.id = :produtoId")
    Long sumQuantidadeByProduto(@Param("produtoId") Integer produtoId);

    /**
     * Conta o total de movimentações por ordem de compra.
     * 
     * <p>
     * Consulta agregada otimizada para dashboards e relatórios.
     * </p>
     * 
     * @param idOrdComp ID da ordem de compra
     * @return Quantidade de movimentações da ordem de compra
     */
    @Query("SELECT COUNT(m) FROM MovContabil m WHERE m.produto.id = :produtoId")
    Long countByProduto(@Param("produtoId") Integer produtoId);

    /**
     * Soma o valor total de débitos por plano de contas em um período.
     * 
     * <p>
     * Consulta agregada para relatórios contábeis.
     * </p>
     * 
     * @param idPlanoConta ID do plano de contas
     * @param dataInicio   Data inicial
     * @param dataFim      Data final
     * @return Valor total de débitos do plano de contas no período
     */
    @Query("SELECT COALESCE(SUM(m.valorUnitario),0) FROM MovContabil m WHERE m.produto.id = :produtoId AND m.dataMovimentacao BETWEEN :dataInicio AND :dataFim")
    BigDecimal sumValorUnitarioByProdutoAndPeriodo(@Param("produtoId") Integer produtoId,
                                                   @Param("dataInicio") LocalDate dataInicio,
                                                   @Param("dataFim") LocalDate dataFim);

    /**
     * Soma o valor total de créditos por plano de contas em um período.
     * 
     * <p>
     * Consulta agregada para relatórios contábeis.
     * </p>
     * 
     * @param idPlanoConta ID do plano de contas
     * @param dataInicio   Data inicial
     * @param dataFim      Data final
     * @return Valor total de créditos do plano de contas no período
     */
    @Query("SELECT COALESCE(SUM(m.valorTotal),0) FROM MovContabil m WHERE m.produto.id = :produtoId AND m.dataMovimentacao BETWEEN :dataInicio AND :dataFim")
    BigDecimal sumValorTotalByProdutoAndPeriodo(@Param("produtoId") Integer produtoId,
                                                @Param("dataInicio") LocalDate dataInicio,
                                                @Param("dataFim") LocalDate dataFim);

    /**
     * Calcula o saldo (débito - crédito) por plano de contas em um período.
     * 
     * <p>
     * Consulta agregada para balanços contábeis.
     * </p>
     * 
     * @param idPlanoConta ID do plano de contas
     * @param dataInicio   Data inicial
     * @param dataFim      Data final
     * @return Saldo do plano de contas no período
     */
    @Query("SELECT COALESCE(SUM(m.valorTotal),0) FROM MovContabil m WHERE m.dataMovimentacao BETWEEN :dataInicio AND :dataFim")
    BigDecimal sumValorTotalPeriodo(@Param("dataInicio") LocalDate dataInicio,
                                    @Param("dataFim") LocalDate dataFim);

}