package com.br.fasiclin.estoque.estoque.controller;

import com.br.fasiclin.estoque.estoque.dto.ApiResponseDTO;
import com.br.fasiclin.estoque.estoque.dto.OrdemCompraDTO;
import com.br.fasiclin.estoque.estoque.model.StatusOrdemCompra;
import com.br.fasiclin.estoque.estoque.service.OrdemCompraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para gerenciamento de Ordens de Compra.
 * Endpoints REST para controle do ciclo de vida das ordens.
 * 
 * @author Sistema Fasiclin - Módulo Estoque
 * @version 1.0
 * @since 2025
 */
@Slf4j
@RestController
@RequestMapping("/api/ordens-compra")
@RequiredArgsConstructor
@Tag(name = "Ordens de Compra", description = "Gerenciamento de ordens de compra")
public class OrdemCompraController {
    
    private final OrdemCompraService ordemCompraService;
    
    /**
     * Lista todas as ordens com paginação
     */
    @GetMapping
    @Operation(summary = "Listar ordens", description = "Lista todas as ordens de compra")
    public ResponseEntity<ApiResponseDTO<Page<OrdemCompraDTO>>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("GET /api/ordens-compra - page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<OrdemCompraDTO> ordens = ordemCompraService.findAll(pageable);
        
        return ResponseEntity.ok(ApiResponseDTO.success(ordens));
    }
    
    /**
     * Busca ordem por ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar ordem por ID", description = "Retorna uma ordem de compra específica")
    public ResponseEntity<ApiResponseDTO<OrdemCompraDTO>> findById(@PathVariable Integer id) {
        log.debug("GET /api/ordens-compra/{}", id);
        OrdemCompraDTO ordem = ordemCompraService.findById(id);
        return ResponseEntity.ok(ApiResponseDTO.success(ordem));
    }
    
    /**
     * Busca ordens por status
     * GET /api/ordens-compra/status/PEND
     */
    @GetMapping("/status/{status}")
    @Operation(summary = "Buscar por status", 
               description = "Lista ordens de compra por status (PEND, ANDA, CONC)")
    public ResponseEntity<ApiResponseDTO<List<OrdemCompraDTO>>> findByStatus(
            @PathVariable StatusOrdemCompra status) {
        
        log.debug("GET /api/ordens-compra/status/{}", status);
        List<OrdemCompraDTO> ordens = ordemCompraService.findByStatus(status);
        return ResponseEntity.ok(ApiResponseDTO.success(ordens));
    }
    
    /**
     * Cria nova ordem de compra
     */
    @PostMapping
    @Operation(summary = "Criar ordem", description = "Cadastra uma nova ordem de compra")
    public ResponseEntity<ApiResponseDTO<OrdemCompraDTO>> create(
            @Valid @RequestBody OrdemCompraDTO dto) {
        
        log.info("POST /api/ordens-compra - Criando ordem");
        OrdemCompraDTO created = ordemCompraService.create(dto);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success("Ordem criada com sucesso", created));
    }
    
    /**
     * Atualiza ordem de compra
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar ordem", description = "Atualiza dados de uma ordem de compra")
    public ResponseEntity<ApiResponseDTO<OrdemCompraDTO>> update(
            @PathVariable Integer id,
            @Valid @RequestBody OrdemCompraDTO dto) {
        
        log.info("PUT /api/ordens-compra/{}", id);
        OrdemCompraDTO updated = ordemCompraService.update(id, dto);
        
        return ResponseEntity.ok(
                ApiResponseDTO.success("Ordem atualizada com sucesso", updated));
    }
    
    /**
     * Atualiza status da ordem
     * PATCH /api/ordens-compra/{id}/status?novoStatus=ANDA
     */
    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status", 
               description = "Atualiza o status da ordem (PEND -> ANDA -> CONC)")
    public ResponseEntity<ApiResponseDTO<OrdemCompraDTO>> atualizarStatus(
            @PathVariable Integer id,
            @RequestParam StatusOrdemCompra novoStatus) {
        
        log.info("PATCH /api/ordens-compra/{}/status - Novo status: {}", id, novoStatus);
        OrdemCompraDTO updated = ordemCompraService.atualizarStatus(id, novoStatus);
        
        return ResponseEntity.ok(
                ApiResponseDTO.success("Status atualizado com sucesso", updated));
    }
    
    /**
     * Remove ordem de compra
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Remover ordem", description = "Remove uma ordem de compra (apenas pendentes)")
    public ResponseEntity<ApiResponseDTO<Void>> delete(@PathVariable Integer id) {
        log.info("DELETE /api/ordens-compra/{}", id);
        ordemCompraService.delete(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Ordem removida com sucesso", null));
    }
}
