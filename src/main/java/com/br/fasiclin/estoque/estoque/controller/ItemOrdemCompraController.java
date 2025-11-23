package com.br.fasiclin.estoque.estoque.controller;

import com.br.fasiclin.estoque.estoque.dto.ApiResponseDTO;
import com.br.fasiclin.estoque.estoque.dto.ItemOrdemCompraDTO;
import com.br.fasiclin.estoque.estoque.service.ItemOrdemCompraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para gerenciamento de Itens de Ordem de Compra.
 * 
 * @author Sistema Fasiclin - Módulo Estoque
 * @version 1.0
 * @since 2025
 */
@Slf4j
@RestController
@RequestMapping("/api/itens-ordem-compra")
@RequiredArgsConstructor
@Tag(name = "Itens de Ordem de Compra", description = "Gerenciamento de itens de ordem de compra")
public class ItemOrdemCompraController {
    
    private final ItemOrdemCompraService itemService;
    
    /**
     * Busca item por ID
     * GET /api/itens-ordem-compra/1
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID", description = "Retorna um item específico")
    public ResponseEntity<ApiResponseDTO<ItemOrdemCompraDTO>> findById(@PathVariable Integer id) {
        log.debug("GET /api/itens-ordem-compra/{}", id);
        ItemOrdemCompraDTO item = itemService.findById(id);
        return ResponseEntity.ok(ApiResponseDTO.success(item));
    }
    
    /**
     * Lista itens de uma ordem de compra
     * GET /api/itens-ordem-compra/ordem/1
     */
    @GetMapping("/ordem/{idOrdemCompra}")
    @Operation(summary = "Listar por ordem", 
               description = "Lista todos os itens de uma ordem de compra")
    public ResponseEntity<ApiResponseDTO<List<ItemOrdemCompraDTO>>> findByOrdemCompra(
            @PathVariable Integer idOrdemCompra) {
        
        log.debug("GET /api/itens-ordem-compra/ordem/{}", idOrdemCompra);
        List<ItemOrdemCompraDTO> itens = itemService.findByOrdemCompraId(idOrdemCompra);
        return ResponseEntity.ok(ApiResponseDTO.success(itens));
    }
    
    /**
     * Lista itens de um produto
     * GET /api/itens-ordem-compra/produto/1
     */
    @GetMapping("/produto/{idProduto}")
    @Operation(summary = "Listar por produto", 
               description = "Lista todos os itens de um produto específico")
    public ResponseEntity<ApiResponseDTO<List<ItemOrdemCompraDTO>>> findByProduto(
            @PathVariable Integer idProduto) {
        
        log.debug("GET /api/itens-ordem-compra/produto/{}", idProduto);
        List<ItemOrdemCompraDTO> itens = itemService.findByProdutoId(idProduto);
        return ResponseEntity.ok(ApiResponseDTO.success(itens));
    }
    
    /**
     * Lista itens vencidos
     * GET /api/itens-ordem-compra/vencidos
     */
    @GetMapping("/vencidos")
    @Operation(summary = "Listar vencidos", 
               description = "Lista itens com data de vencimento expirada")
    public ResponseEntity<ApiResponseDTO<List<ItemOrdemCompraDTO>>> findVencidos() {
        log.debug("GET /api/itens-ordem-compra/vencidos");
        List<ItemOrdemCompraDTO> itens = itemService.findItensVencidos();
        return ResponseEntity.ok(ApiResponseDTO.success(itens));
    }
    
    /**
     * Lista itens próximos ao vencimento
     * GET /api/itens-ordem-compra/proximos-vencimento?dias=30
     */
    @GetMapping("/proximos-vencimento")
    @Operation(summary = "Listar próximos ao vencimento", 
               description = "Lista itens que vencem nos próximos X dias")
    public ResponseEntity<ApiResponseDTO<List<ItemOrdemCompraDTO>>> findProximosVencimento(
            @RequestParam(defaultValue = "30") int dias) {
        
        log.debug("GET /api/itens-ordem-compra/proximos-vencimento?dias={}", dias);
        List<ItemOrdemCompraDTO> itens = itemService.findItensProximosVencimento(dias);
        return ResponseEntity.ok(ApiResponseDTO.success(itens));
    }
    
    /**
     * Cria novo item
     * POST /api/itens-ordem-compra
     */
    @PostMapping
    @Operation(summary = "Criar item", description = "Adiciona um novo item à ordem de compra")
    public ResponseEntity<ApiResponseDTO<ItemOrdemCompraDTO>> create(
            @Valid @RequestBody ItemOrdemCompraDTO dto) {
        
        log.info("POST /api/itens-ordem-compra - Criando item");
        ItemOrdemCompraDTO created = itemService.create(dto);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success("Item criado com sucesso", created));
    }
    
    /**
     * Atualiza item
     * PUT /api/itens-ordem-compra/1
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar item", description = "Atualiza dados de um item")
    public ResponseEntity<ApiResponseDTO<ItemOrdemCompraDTO>> update(
            @PathVariable Integer id,
            @Valid @RequestBody ItemOrdemCompraDTO dto) {
        
        log.info("PUT /api/itens-ordem-compra/{}", id);
        ItemOrdemCompraDTO updated = itemService.update(id, dto);
        
        return ResponseEntity.ok(
                ApiResponseDTO.success("Item atualizado com sucesso", updated));
    }
    
    /**
     * Remove item
     * DELETE /api/itens-ordem-compra/1
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Remover item", description = "Remove um item da ordem de compra")
    public ResponseEntity<ApiResponseDTO<Void>> delete(@PathVariable Integer id) {
        log.info("DELETE /api/itens-ordem-compra/{}", id);
        itemService.delete(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Item removido com sucesso", null));
    }
}
