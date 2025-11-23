package com.br.fasiclin.estoque.estoque.service;

import com.br.fasiclin.estoque.estoque.dto.ItemOrdemCompraDTO;
import com.br.fasiclin.estoque.estoque.exception.BusinessException;
import com.br.fasiclin.estoque.estoque.exception.ResourceNotFoundException;
import com.br.fasiclin.estoque.estoque.model.ItemOrdemCompra;
import com.br.fasiclin.estoque.estoque.model.OrdemCompra;
import com.br.fasiclin.estoque.estoque.model.Produto;
import com.br.fasiclin.estoque.estoque.repository.ItemOrdemCompraRepository;
import com.br.fasiclin.estoque.estoque.repository.OrdemCompraRepository;
import com.br.fasiclin.estoque.estoque.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para gerenciamento de Itens de Ordem de Compra.
 * 
 * @author Sistema Fasiclin - Módulo Estoque
 * @version 1.0
 * @since 2025
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemOrdemCompraService {

    private final ItemOrdemCompraRepository itemRepository;
    private final OrdemCompraRepository ordemCompraRepository;
    private final ProdutoRepository produtoRepository;

    /**
     * Busca item por ID
     */
    public ItemOrdemCompraDTO findById(Integer id) {
        log.debug("Buscando item de ordem de compra com ID: {}", id);
        ItemOrdemCompra item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item de Ordem de Compra", "id", id));
        return convertToDTO(item);
    }

    /**
     * Busca itens por ordem de compra
     */
    public List<ItemOrdemCompraDTO> findByOrdemCompraId(Integer idOrdemCompra) {
        log.debug("Buscando itens da ordem de compra ID: {}", idOrdemCompra);
        return itemRepository.findByOrdemCompraId(idOrdemCompra).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca itens por produto
     */
    public List<ItemOrdemCompraDTO> findByProdutoId(Integer idProduto) {
        log.debug("Buscando itens do produto ID: {}", idProduto);
        return itemRepository.findByProdutoId(idProduto).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca itens vencidos
     */
    public List<ItemOrdemCompraDTO> findItensVencidos() {
        log.debug("Buscando itens vencidos");
        return itemRepository.findItensVencidos().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca itens próximos ao vencimento
     */
    public List<ItemOrdemCompraDTO> findItensProximosVencimento(int diasAntecedencia) {
        log.debug("Buscando itens com vencimento nos próximos {} dias", diasAntecedencia);
        LocalDate dataLimite = LocalDate.now().plusDays(diasAntecedencia);
        return itemRepository.findItensProximosVencimento(dataLimite).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Cria novo item de ordem de compra
     */
    @Transactional
    public ItemOrdemCompraDTO create(ItemOrdemCompraDTO dto) {
        log.info("Criando novo item de ordem de compra");

        validateItem(dto);

        ItemOrdemCompra item = convertToEntity(dto);
        ItemOrdemCompra saved = itemRepository.save(item);
        
        log.info("Item criado com sucesso. ID: {}", saved.getId());
        return convertToDTO(saved);
    }

    /**
     * Atualiza item de ordem de compra
     */
    @Transactional
    public ItemOrdemCompraDTO update(Integer id, ItemOrdemCompraDTO dto) {
        log.info("Atualizando item de ordem de compra ID: {}", id);

        ItemOrdemCompra existing = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item de Ordem de Compra", "id", id));

        validateItem(dto);
        updateEntity(existing, dto);

        ItemOrdemCompra updated = itemRepository.save(existing);
        log.info("Item atualizado com sucesso. ID: {}", id);

        return convertToDTO(updated);
    }

    /**
     * Remove item de ordem de compra
     */
    @Transactional
    public void delete(Integer id) {
        log.info("Removendo item de ordem de compra ID: {}", id);

        ItemOrdemCompra item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item de Ordem de Compra", "id", id));

        itemRepository.delete(item);
        log.info("Item removido com sucesso");
    }

    // ========== VALIDAÇÕES ==========

    private void validateItem(ItemOrdemCompraDTO dto) {
        if (dto.getIdOrdemCompra() == null) {
            throw new BusinessException("Ordem de compra é obrigatória");
        }

        if (dto.getIdProduto() == null) {
            throw new BusinessException("Produto é obrigatório");
        }

        if (dto.getQuantidade() == null || dto.getQuantidade() <= 0) {
            throw new BusinessException("Quantidade deve ser maior que zero");
        }

        if (dto.getValorUnitario() == null || dto.getValorUnitario().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Valor unitário deve ser positivo");
        }

        // Verificar se ordem de compra existe
        ordemCompraRepository.findById(dto.getIdOrdemCompra())
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de Compra", "id", dto.getIdOrdemCompra()));

        // Verificar se produto existe
        produtoRepository.findById(dto.getIdProduto())
                .orElseThrow(() -> new ResourceNotFoundException("Produto", "id", dto.getIdProduto()));
    }

    // ========== CONVERSÃO DTO <-> ENTITY ==========

    private ItemOrdemCompraDTO convertToDTO(ItemOrdemCompra entity) {
        BigDecimal valorTotal = entity.getValor().multiply(BigDecimal.valueOf(entity.getQuantidade()));
        
        return ItemOrdemCompraDTO.builder()
                .id(entity.getId())
                .idOrdemCompra(entity.getOrdemCompra() != null ? entity.getOrdemCompra().getId() : null)
                .idProduto(entity.getProduto() != null ? entity.getProduto().getId() : null)
                .quantidade(entity.getQuantidade())
                .valorUnitario(entity.getValor())
                .valorTotal(valorTotal)
                .nomeProduto(entity.getProduto() != null ? entity.getProduto().getNome() : null)
                .build();
    }

    private ItemOrdemCompra convertToEntity(ItemOrdemCompraDTO dto) {
        ItemOrdemCompra item = new ItemOrdemCompra();
        updateEntity(item, dto);
        return item;
    }

    private void updateEntity(ItemOrdemCompra entity, ItemOrdemCompraDTO dto) {
        // Associar OrdemCompra
        if (dto.getIdOrdemCompra() != null) {
            OrdemCompra ordem = new OrdemCompra();
            ordem.setId(dto.getIdOrdemCompra());
            entity.setOrdemCompra(ordem);
        }
        
        // Associar Produto
        if (dto.getIdProduto() != null) {
            Produto produto = new Produto();
            produto.setId(dto.getIdProduto());
            entity.setProduto(produto);
        }
        
        entity.setQuantidade(dto.getQuantidade());
        entity.setValor(dto.getValorUnitario());
        
        // Data de vencimento padrão: 30 dias a partir de hoje
        if (entity.getDataVencimento() == null) {
            entity.setDataVencimento(LocalDate.now().plusDays(30));
        }
    }
}
