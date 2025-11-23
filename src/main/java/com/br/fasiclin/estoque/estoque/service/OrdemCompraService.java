package com.br.fasiclin.estoque.estoque.service;

import com.br.fasiclin.estoque.estoque.dto.OrdemCompraDTO;
import com.br.fasiclin.estoque.estoque.exception.BusinessException;
import com.br.fasiclin.estoque.estoque.exception.ResourceNotFoundException;
import com.br.fasiclin.estoque.estoque.model.OrdemCompra;
import com.br.fasiclin.estoque.estoque.model.StatusOrdemCompra;
import com.br.fasiclin.estoque.estoque.repository.OrdemCompraRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para gerenciamento de Ordens de Compra.
 * Gerencia ciclo de vida completo das ordens.
 * 
 * @author Sistema Fasiclin - Módulo Estoque
 * @version 1.0
 * @since 2025
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrdemCompraService {

    private final OrdemCompraRepository ordemCompraRepository;

    /**
     * Busca ordem por ID
     */
    public OrdemCompraDTO findById(Integer id) {
        log.debug("Buscando ordem de compra com ID: {}", id);
        OrdemCompra ordem = ordemCompraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de Compra", "id", id));
        return convertToDTO(ordem);
    }

    /**
     * Lista todas as ordens com paginação
     */
    public Page<OrdemCompraDTO> findAll(Pageable pageable) {
        log.debug("Listando ordens de compra com paginação: {}", pageable);
        return ordemCompraRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    /**
     * Busca ordens por status
     */
    public List<OrdemCompraDTO> findByStatus(StatusOrdemCompra status) {
        log.debug("Buscando ordens por status: {}", status);
        return ordemCompraRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Cria nova ordem de compra
     */
    @Transactional
    public OrdemCompraDTO create(OrdemCompraDTO dto) {
        log.info("Criando nova ordem de compra");

        // Validações de negócio
        validateOrdemCompra(dto);

        OrdemCompra ordem = convertToEntity(dto);
        ordem.setDataOrdem(LocalDate.now()); // Data atual
        ordem.setStatus(StatusOrdemCompra.PEND); // Status inicial

        OrdemCompra saved = ordemCompraRepository.save(ordem);
        log.info("Ordem de compra criada com sucesso. ID: {}", saved.getId());

        return convertToDTO(saved);
    }

    /**
     * Atualiza ordem de compra
     */
    @Transactional
    public OrdemCompraDTO update(Integer id, OrdemCompraDTO dto) {
        log.info("Atualizando ordem de compra ID: {}", id);

        OrdemCompra existing = ordemCompraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de Compra", "id", id));

        // Não permitir atualização de ordens concluídas
        if (existing.getStatus() == StatusOrdemCompra.CONC) {
            throw new BusinessException("Não é possível atualizar ordem de compra concluída");
        }

        validateOrdemCompra(dto);
        updateEntity(existing, dto);

        OrdemCompra updated = ordemCompraRepository.save(existing);
        log.info("Ordem de compra atualizada com sucesso. ID: {}", id);

        return convertToDTO(updated);
    }

    /**
     * Atualiza status da ordem
     */
    @Transactional
    public OrdemCompraDTO atualizarStatus(Integer id, StatusOrdemCompra novoStatus) {
        log.info("Atualizando status da ordem {} para {}", id, novoStatus);

        OrdemCompra ordem = ordemCompraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de Compra", "id", id));

        // Validar transição de status
        validateStatusTransition(ordem.getStatus(), novoStatus);

        ordem.setStatus(novoStatus);

        // Se concluída, atualizar data de entrega
        if (novoStatus == StatusOrdemCompra.CONC) {
            ordem.setDataEntrega(LocalDate.now());
        }

        OrdemCompra updated = ordemCompraRepository.save(ordem);
        log.info("Status atualizado com sucesso");

        return convertToDTO(updated);
    }

    /**
     * Remove ordem de compra
     */
    @Transactional
    public void delete(Integer id) {
        log.info("Removendo ordem de compra ID: {}", id);

        OrdemCompra ordem = ordemCompraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ordem de Compra", "id", id));

        // Só permite remoção de ordens pendentes
        if (ordem.getStatus() != StatusOrdemCompra.PEND) {
            throw new BusinessException("Só é possível remover ordens pendentes");
        }

        ordemCompraRepository.delete(ordem);
        log.info("Ordem de compra removida com sucesso");
    }

    // ========== VALIDAÇÕES ==========

    private void validateOrdemCompra(OrdemCompraDTO dto) {
        if (dto.getDataPrevisao().isBefore(LocalDate.now())) {
            throw new BusinessException("Data de previsão não pode ser no passado");
        }

        if (dto.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Valor da ordem deve ser positivo");
        }
    }

    private void validateStatusTransition(StatusOrdemCompra atual, StatusOrdemCompra novo) {
        // PEND -> ANDA -> CONC (fluxo normal)
        if (atual == StatusOrdemCompra.CONC) {
            throw new BusinessException("Ordem concluída não pode ter status alterado");
        }

        if (atual == StatusOrdemCompra.PEND && novo == StatusOrdemCompra.CONC) {
            throw new BusinessException("Ordem pendente deve passar por andamento antes de ser concluída");
        }
    }

    // ========== CONVERSÃO DTO <-> ENTITY ==========

    private OrdemCompraDTO convertToDTO(OrdemCompra entity) {
        return OrdemCompraDTO.builder()
                .id(entity.getId())
                .status(entity.getStatus())
                .valor(entity.getValor())
                .dataPrevisao(entity.getDataPrevisao())
                .dataOrdem(entity.getDataOrdem())
                .dataEntrega(entity.getDataEntrega())
                .build();
    }

    private OrdemCompra convertToEntity(OrdemCompraDTO dto) {
        OrdemCompra ordem = new OrdemCompra();
        updateEntity(ordem, dto);
        return ordem;
    }

    private void updateEntity(OrdemCompra entity, OrdemCompraDTO dto) {
        entity.setStatus(dto.getStatus());
        entity.setValor(dto.getValor());
        entity.setDataPrevisao(dto.getDataPrevisao());
        if (dto.getDataOrdem() != null) {
            entity.setDataOrdem(dto.getDataOrdem());
        }
        if (dto.getDataEntrega() != null) {
            entity.setDataEntrega(dto.getDataEntrega());
        }
    }
}
