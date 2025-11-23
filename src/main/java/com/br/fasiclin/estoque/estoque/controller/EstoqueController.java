package com.br.fasiclin.estoque.estoque.controller;

import com.br.fasiclin.estoque.estoque.dto.EstoqueDTO;
import com.br.fasiclin.estoque.estoque.dto.ApiResponseDTO;
import com.br.fasiclin.estoque.estoque.service.EstoqueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST para gerenciamento de Estoque.
 * Endpoints REST para controle de entradas e saídas.
 * 
 * @author Sistema Fasiclin - Módulo Estoque
 * @version 1.0
 * @since 2025
 */
@Slf4j
@RestController
@RequestMapping("/api/estoque")
@RequiredArgsConstructor
@Tag(name = "Estoque", description = "Controle de entradas, saídas e consultas de estoque")
public class EstoqueController {
    
    private final EstoqueService estoqueService;
    
    /**
     * Lista todos os estoques com paginação
     */
    @GetMapping
    @Operation(summary = "Listar estoques", description = "Lista todos os registros de estoque")
    public ResponseEntity<ApiResponseDTO<Page<EstoqueDTO>>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("GET /api/estoque - page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<EstoqueDTO> estoques = estoqueService.findAll(pageable);
        
        return ResponseEntity.ok(ApiResponseDTO.success(estoques));
    }
    
    /**
     * Busca estoque por ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar estoque por ID", description = "Retorna registro específico de estoque")
    public ResponseEntity<ApiResponseDTO<EstoqueDTO>> findById(@PathVariable Integer id) {
        log.debug("GET /api/estoque/{}", id);
        EstoqueDTO estoque = estoqueService.findById(id);
        return ResponseEntity.ok(ApiResponseDTO.success(estoque));
    }
    
    /**
     * Registra entrada de estoque
     * POST /api/estoque/entrada
     */
    @PostMapping("/entrada")
    @Operation(summary = "Registrar entrada", 
               description = "Adiciona quantidade ao estoque de um produto/lote")
    public ResponseEntity<ApiResponseDTO<EstoqueDTO>> registrarEntrada(
            @RequestParam Integer idProduto,
            @RequestParam Integer idLote,
            @RequestParam Integer quantidade) {
        
        log.info("POST /api/estoque/entrada - Produto: {}, Lote: {}, Qtd: {}", 
                 idProduto, idLote, quantidade);
        
        EstoqueDTO estoque = estoqueService.registrarEntrada(idProduto, idLote, quantidade);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success("Entrada registrada com sucesso", estoque));
    }
    
    /**
     * Registra saída de estoque
     * POST /api/estoque/saida
     */
    @PostMapping("/saida")
    @Operation(summary = "Registrar saída",
               description = "Remove quantidade do estoque de um produto/lote")
    public ResponseEntity<ApiResponseDTO<EstoqueDTO>> registrarSaida(
            @RequestParam Integer idProduto,
            @RequestParam Integer idLote,
            @RequestParam Integer quantidade) {
        
        log.info("POST /api/estoque/saida - Produto: {}, Lote: {}, Qtd: {}", 
                 idProduto, idLote, quantidade);
        
        EstoqueDTO estoque = estoqueService.registrarSaida(idProduto, idLote, quantidade);
        
        return ResponseEntity.ok(
                ApiResponseDTO.success("Saída registrada com sucesso", estoque));
    }
    
    /**
     * Atualiza quantidade de estoque (substitui o valor)
     * PATCH /api/estoque/{id}/quantidade
     */
    @PatchMapping("/{id}/quantidade")
    @Operation(summary = "Atualizar quantidade",
               description = "Atualiza a quantidade de estoque (substitui o valor atual)")
    public ResponseEntity<ApiResponseDTO<EstoqueDTO>> atualizarQuantidade(
            @PathVariable Integer id,
            @RequestParam Integer novaQuantidade) {
        
        log.info("PATCH /api/estoque/{}/quantidade - Nova quantidade: {}", id, novaQuantidade);
        EstoqueDTO estoque = estoqueService.atualizarQuantidade(id, novaQuantidade);
        
        return ResponseEntity.ok(
                ApiResponseDTO.success("Quantidade atualizada com sucesso", estoque));
    }
    
    /**
     * Remove registro de estoque
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Remover estoque", description = "Remove um registro de estoque")
    public ResponseEntity<ApiResponseDTO<Void>> delete(@PathVariable Integer id) {
        log.info("DELETE /api/estoque/{}", id);
        estoqueService.delete(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Registro removido com sucesso", null));
    }
}
