package com.br.fasiclin.estoque.estoque.controller;

import com.br.fasiclin.estoque.estoque.dto.ApiResponseDTO;
import com.br.fasiclin.estoque.estoque.dto.FornecedorDTO;
import com.br.fasiclin.estoque.estoque.service.FornecedorService;
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
 * Controller REST para gerenciamento de Fornecedores.
 * 
 * @author Sistema Fasiclin - Módulo Estoque
 * @version 1.0
 * @since 2025
 */
@Slf4j
@RestController
@RequestMapping("/api/fornecedores")
@RequiredArgsConstructor
@Tag(name = "Fornecedores", description = "Gerenciamento de fornecedores")
public class FornecedorController {
    
    private final FornecedorService fornecedorService;
    
    /**
     * Lista todos os fornecedores com paginação
     * GET /api/fornecedores?page=0&size=20
     */
    @GetMapping
    @Operation(summary = "Listar fornecedores", description = "Lista todos os fornecedores com paginação")
    public ResponseEntity<ApiResponseDTO<Page<FornecedorDTO>>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("GET /api/fornecedores - page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<FornecedorDTO> fornecedores = fornecedorService.findAll(pageable);
        
        return ResponseEntity.ok(ApiResponseDTO.success(fornecedores));
    }
    
    /**
     * Busca fornecedor por ID
     * GET /api/fornecedores/1
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID", description = "Retorna um fornecedor específico")
    public ResponseEntity<ApiResponseDTO<FornecedorDTO>> findById(@PathVariable Integer id) {
        log.debug("GET /api/fornecedores/{}", id);
        FornecedorDTO fornecedor = fornecedorService.findById(id);
        return ResponseEntity.ok(ApiResponseDTO.success(fornecedor));
    }
    
    /**
     * Busca fornecedores por representante
     * GET /api/fornecedores/buscar?representante=João
     */
    @GetMapping("/buscar")
    @Operation(summary = "Buscar por representante", 
               description = "Busca fornecedores que contenham o nome do representante")
    public ResponseEntity<ApiResponseDTO<List<FornecedorDTO>>> findByRepresentante(
            @RequestParam String representante) {
        
        log.debug("GET /api/fornecedores/buscar?representante={}", representante);
        List<FornecedorDTO> fornecedores = fornecedorService.findByRepresentante(representante);
        return ResponseEntity.ok(ApiResponseDTO.success(fornecedores));
    }
    
    /**
     * Cria novo fornecedor
     * POST /api/fornecedores
     */
    @PostMapping
    @Operation(summary = "Criar fornecedor", description = "Cadastra um novo fornecedor")
    public ResponseEntity<ApiResponseDTO<FornecedorDTO>> create(
            @Valid @RequestBody FornecedorDTO dto) {
        
        log.info("POST /api/fornecedores - Criando fornecedor");
        FornecedorDTO created = fornecedorService.create(dto);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success("Fornecedor criado com sucesso", created));
    }
    
    /**
     * Atualiza fornecedor
     * PUT /api/fornecedores/1
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar fornecedor", description = "Atualiza dados de um fornecedor")
    public ResponseEntity<ApiResponseDTO<FornecedorDTO>> update(
            @PathVariable Integer id,
            @Valid @RequestBody FornecedorDTO dto) {
        
        log.info("PUT /api/fornecedores/{}", id);
        FornecedorDTO updated = fornecedorService.update(id, dto);
        
        return ResponseEntity.ok(
                ApiResponseDTO.success("Fornecedor atualizado com sucesso", updated));
    }
    
    /**
     * Remove fornecedor
     * DELETE /api/fornecedores/1
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Remover fornecedor", description = "Remove um fornecedor do sistema")
    public ResponseEntity<ApiResponseDTO<Void>> delete(@PathVariable Integer id) {
        log.info("DELETE /api/fornecedores/{}", id);
        fornecedorService.delete(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Fornecedor removido com sucesso", null));
    }
}
