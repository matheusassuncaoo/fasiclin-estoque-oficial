package com.br.fasiclin.estoque.estoque.model;

/**
 * Enum representando os possíveis status de uma Ordem de Compra.
 * 
 * @author Sistema Fasiclin - Módulo Estoque
 * @version 1.0
 * @since 2025
 */
public enum StatusOrdemCompra {
    
    /**
     * Ordem de compra pendente (aguardando processamento).
     */
    PEND("Pendente"),
    
    /**
     * Ordem de compra em andamento (sendo processada).
     */
    ANDA("Em Andamento"),
    
    /**
     * Ordem de compra concluída.
     */
    CONC("Concluída");
    
    private final String descricao;
    
    StatusOrdemCompra(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
}
