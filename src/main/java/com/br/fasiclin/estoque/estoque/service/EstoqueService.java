package com.br.fasiclin.estoque.estoque.service;

import com.br.fasiclin.estoque.estoque.dto.EstoqueDTO;
import com.br.fasiclin.estoque.estoque.exception.BusinessException;
import com.br.fasiclin.estoque.estoque.exception.ResourceNotFoundException;
import com.br.fasiclin.estoque.estoque.model.Estoque;
import com.br.fasiclin.estoque.estoque.model.Lote;
import com.br.fasiclin.estoque.estoque.model.Produto;
import com.br.fasiclin.estoque.estoque.repository.EstoqueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;

/**
 * Service para gerenciamento de Estoque.
 * Controla entradas, saídas e consultas de estoque.
 * 
 * @author Sistema Fasiclin - Módulo Estoque
 * @version 1.0
 * @since 2025
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EstoqueService {
    
    private final EstoqueRepository estoqueRepository;
    private final EntityManager entityManager;
    
    /**
     * Busca estoque por ID
     */
    public EstoqueDTO findById(Integer id) {
        log.debug("Buscando estoque com ID: {}", id);
        Estoque estoque = estoqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque", "id", id));
        return convertToDTO(estoque);
    }
    
    /**
     * Lista todos os estoques com paginação
     */
    public Page<EstoqueDTO> findAll(Pageable pageable) {
        log.debug("Listando estoques com paginação: {}", pageable);
        return estoqueRepository.findAll(pageable)
                .map(this::convertToDTO);
    }
    
    /**
     * Registra entrada de estoque (adiciona quantidade)
     */
    @Transactional
    public EstoqueDTO registrarEntrada(Integer idProduto, Integer idLote, Integer quantidade) {
        log.info("Registrando entrada - Produto: {}, Lote: {}, Quantidade: {}", 
                idProduto, idLote, quantidade);
        
        if (quantidade <= 0) {
            throw new BusinessException("Quantidade deve ser positiva");
        }
        
        // Buscar estoque existente ou criar novo
        Estoque estoque = estoqueRepository.findByProdutoIdAndLoteId(idProduto, idLote)
                .orElseGet(() -> {
                    Estoque novo = new Estoque();
                    novo.setProduto(entityManager.getReference(Produto.class, idProduto));
                    novo.setLote(entityManager.getReference(Lote.class, idLote));
                    novo.setQuantidadeEstoque(0);
                    return novo;
                });
        
        estoque.setQuantidadeEstoque(estoque.getQuantidadeEstoque() + quantidade);
        Estoque saved = estoqueRepository.save(estoque);
        
        log.info("Entrada registrada com sucesso. Novo estoque: {}", saved.getQuantidadeEstoque());
        return convertToDTO(saved);
    }
    
    /**
     * Registra saída de estoque (remove quantidade)
     */
    @Transactional
    public EstoqueDTO registrarSaida(Integer idProduto, Integer idLote, Integer quantidade) {
        log.info("Registrando saída - Produto: {}, Lote: {}, Quantidade: {}", 
                idProduto, idLote, quantidade);
        
        if (quantidade <= 0) {
            throw new BusinessException("Quantidade deve ser positiva");
        }
        
        Estoque estoque = estoqueRepository.findByProdutoIdAndLoteId(idProduto, idLote)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Estoque não encontrado para produto/lote especificado"));
        
        if (estoque.getQuantidadeEstoque() < quantidade) {
            throw new BusinessException(
                    String.format("Estoque insuficiente. Disponível: %d, Solicitado: %d",
                            estoque.getQuantidadeEstoque(), quantidade));
        }
        
        estoque.setQuantidadeEstoque(estoque.getQuantidadeEstoque() - quantidade);
        Estoque saved = estoqueRepository.save(estoque);
        
        log.info("Saída registrada com sucesso. Novo estoque: {}", saved.getQuantidadeEstoque());
        return convertToDTO(saved);
    }
    
    /**
     * Atualiza quantidade de estoque (substitui o valor)
     */
    @Transactional
    public EstoqueDTO atualizarQuantidade(Integer id, Integer novaQuantidade) {
        log.info("Atualizando quantidade do estoque ID: {} para {}", id, novaQuantidade);
        
        if (novaQuantidade < 0) {
            throw new BusinessException("Quantidade não pode ser negativa");
        }
        
        Estoque estoque = estoqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque", "id", id));
        
        estoque.setQuantidadeEstoque(novaQuantidade);
        Estoque updated = estoqueRepository.save(estoque);
        
        log.info("Quantidade atualizada com sucesso");
        return convertToDTO(updated);
    }
    
    /**
     * Remove registro de estoque
     */
    @Transactional
    public void delete(Integer id) {
        log.info("Removendo estoque ID: {}", id);
        
        Estoque estoque = estoqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque", "id", id));
        
        estoqueRepository.delete(estoque);
        log.info("Estoque removido com sucesso");
    }
    
    // ========== CONVERSÃO DTO <-> ENTITY ==========
    
    private EstoqueDTO convertToDTO(Estoque entity) {
        return EstoqueDTO.builder()
                .id(entity.getId())
                .idProduto(entity.getProduto() != null ? entity.getProduto().getId() : null)
                .idLote(entity.getLote() != null ? entity.getLote().getId() : null)
                .quantidadeEstoque(entity.getQuantidadeEstoque())
                .nomeProduto(entity.getProduto() != null ? entity.getProduto().getNome() : null)
                .codBarrasProduto(entity.getProduto() != null ? entity.getProduto().getCodBarras() : null)
                .build();
    }
}
