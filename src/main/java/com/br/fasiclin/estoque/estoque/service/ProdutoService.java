package com.br.fasiclin.estoque.estoque.service;

import com.br.fasiclin.estoque.estoque.dto.ProdutoDTO;
import com.br.fasiclin.estoque.estoque.exception.BusinessException;
import com.br.fasiclin.estoque.estoque.exception.ResourceNotFoundException;
import com.br.fasiclin.estoque.estoque.model.Almoxarifado;
import com.br.fasiclin.estoque.estoque.model.Produto;
import com.br.fasiclin.estoque.estoque.model.UnidadeMedida;
import com.br.fasiclin.estoque.estoque.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para gerenciamento de Produtos.
 * Implementa lógica de negócio, validações e conversões DTO <-> Entity.
 * 
 * @author Sistema Fasiclin - Módulo Estoque
 * @version 1.0
 * @since 2025
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // Todas operações de leitura são read-only por padrão
public class ProdutoService {
    
    private final ProdutoRepository produtoRepository;
    private final EntityManager entityManager;
    
    /**
     * Busca produto por ID
     */
    public ProdutoDTO findById(Integer id) {
        log.debug("Buscando produto com ID: {}", id);
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto", "id", id));
        return convertToDTO(produto);
    }
    
    /**
     * Lista todos os produtos com paginação
     */
    public Page<ProdutoDTO> findAll(Pageable pageable) {
        log.debug("Listando produtos com paginação: {}", pageable);
        return produtoRepository.findAll(pageable)
                .map(this::convertToDTO);
    }
    
    /**
     * Busca produtos por nome (parcial)
     */
    public List<ProdutoDTO> findByNome(String nome) {
        log.debug("Buscando produtos por nome: {}", nome);
        return produtoRepository.findByNomeContaining(nome).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Busca produtos com estoque baixo (abaixo do ponto de pedido)
     */
    public List<ProdutoDTO> findProdutosParaReposicao() {
        log.debug("Buscando produtos para reposição");
        return produtoRepository.findProdutosParaReposicao().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Busca produtos com estoque baixo
     */
    public List<ProdutoDTO> findProdutosEstoqueBaixo() {
        log.debug("Buscando produtos com estoque baixo");
        return produtoRepository.findProdutosEstoqueBaixo().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Cria novo produto
     */
    @Transactional // Operação de escrita
    public ProdutoDTO create(ProdutoDTO dto) {
        log.info("Criando novo produto: {}", dto.getNome());
        
        // Validações de negócio
        validateProduto(dto);
        
        Produto produto = convertToEntity(dto);
        Produto saved = produtoRepository.save(produto);
        
        log.info("Produto criado com sucesso. ID: {}", saved.getId());
        return convertToDTO(saved);
    }
    
    /**
     * Atualiza produto existente
     */
    @Transactional
    public ProdutoDTO update(Integer id, ProdutoDTO dto) {
        log.info("Atualizando produto ID: {}", id);
        
        Produto existing = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto", "id", id));
        
        // Validações de negócio
        validateProduto(dto);
        
        // Atualiza campos
        updateEntity(existing, dto);
        Produto updated = produtoRepository.save(existing);
        
        log.info("Produto atualizado com sucesso. ID: {}", id);
        return convertToDTO(updated);
    }
    
    /**
     * Remove produto (soft delete recomendado em produção)
     */
    @Transactional
    public void delete(Integer id) {
        log.info("Removendo produto ID: {}", id);
        
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto", "id", id));
        
        // Verificar se produto tem estoque ou ordens de compra associadas
        // TODO: Implementar verificações de integridade referencial se necessário
        
        produtoRepository.delete(produto);
        log.info("Produto removido com sucesso. ID: {}", id);
    }
    
    // ========== MÉTODOS PRIVADOS DE VALIDAÇÃO E CONVERSÃO ==========
    
    private void validateProduto(ProdutoDTO dto) {
        // Validação: estoque mínimo deve ser menor que máximo
        if (dto.getStqMin() >= dto.getStqMax()) {
            throw new BusinessException("Estoque mínimo deve ser menor que o estoque máximo");
        }
        
        // Validação: ponto de pedido deve estar entre mínimo e máximo
        if (dto.getPtnPedido() < dto.getStqMin() || dto.getPtnPedido() > dto.getStqMax()) {
            throw new BusinessException("Ponto de pedido deve estar entre estoque mínimo e máximo");
        }
        
        // Validação: código de barras único (em criação)
        if (dto.getId() == null) {
            produtoRepository.findAll().stream()
                    .filter(p -> p.getCodBarras().equals(dto.getCodBarras()))
                    .findFirst()
                    .ifPresent(p -> {
                        throw new BusinessException("Código de barras já cadastrado");
                    });
        }
    }
    
    private ProdutoDTO convertToDTO(Produto entity) {
        return ProdutoDTO.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .descricao(entity.getDescricao())
                .idAlmoxarifado(entity.getAlmoxarifado() != null ? entity.getAlmoxarifado().getId() : null)
                .idUnidadeMedida(entity.getUnidadeMedida() != null ? entity.getUnidadeMedida().getId() : null)
                .codBarras(entity.getCodBarras())
                .tempIdeal(entity.getTempIdeal())
                .stqMax(entity.getStqMax())
                .stqMin(entity.getStqMin())
                .ptnPedido(entity.getPtnPedido())
                .build();
    }
    
    private Produto convertToEntity(ProdutoDTO dto) {
        Produto produto = new Produto();
        updateEntity(produto, dto);
        return produto;
    }
    
    private void updateEntity(Produto entity, ProdutoDTO dto) {
        entity.setNome(dto.getNome());
        entity.setDescricao(dto.getDescricao());
        entity.setCodBarras(dto.getCodBarras());
        entity.setTempIdeal(dto.getTempIdeal());
        entity.setStqMax(dto.getStqMax());
        entity.setStqMin(dto.getStqMin());
        entity.setPtnPedido(dto.getPtnPedido());
        
        // Buscar e associar entidades relacionadas
        if (dto.getIdAlmoxarifado() != null) {
            Almoxarifado almox = entityManager.getReference(Almoxarifado.class, dto.getIdAlmoxarifado());
            entity.setAlmoxarifado(almox);
        }
        
        if (dto.getIdUnidadeMedida() != null) {
            UnidadeMedida unidade = entityManager.getReference(UnidadeMedida.class, dto.getIdUnidadeMedida());
            entity.setUnidadeMedida(unidade);
        }
    }
}
