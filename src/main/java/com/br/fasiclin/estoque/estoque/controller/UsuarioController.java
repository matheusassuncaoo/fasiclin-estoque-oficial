package com.br.fasiclin.estoque.estoque.controller;

import com.br.fasiclin.estoque.estoque.dto.ApiResponseDTO;
import com.br.fasiclin.estoque.estoque.dto.UsuarioDTO;
import com.br.fasiclin.estoque.estoque.service.UsuarioService;
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

/**
 * Controller REST para gerenciamento de Usuários.
 * Endpoints REST para CRUD e autenticação.
 * 
 * @author Sistema Fasiclin - Módulo Estoque
 * @version 1.0
 * @since 2025
 */
@Slf4j
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Gerenciamento de usuários do sistema")
public class UsuarioController {
    
    private final UsuarioService usuarioService;
    
    /**
     * Lista todos os usuários com paginação
     */
    @GetMapping
    @Operation(summary = "Listar usuários", description = "Lista todos os usuários do sistema")
    public ResponseEntity<ApiResponseDTO<Page<UsuarioDTO>>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("GET /api/usuarios - page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<UsuarioDTO> usuarios = usuarioService.findAll(pageable);
        
        return ResponseEntity.ok(ApiResponseDTO.success(usuarios));
    }
    
    /**
     * Busca usuário por ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar por ID", description = "Retorna um usuário específico")
    public ResponseEntity<ApiResponseDTO<UsuarioDTO>> findById(@PathVariable Integer id) {
        log.debug("GET /api/usuarios/{}", id);
        UsuarioDTO usuario = usuarioService.findById(id);
        return ResponseEntity.ok(ApiResponseDTO.success(usuario));
    }
    
    /**
     * Busca usuário por nome de usuário
     * GET /api/usuarios/buscar?nomeUsuario=admin
     */
    @GetMapping("/buscar")
    @Operation(summary = "Buscar por nome de usuário", 
               description = "Busca usuário pelo nome de usuário (login)")
    public ResponseEntity<ApiResponseDTO<UsuarioDTO>> findByNomeUsuario(
            @RequestParam String nomeUsuario) {
        
        log.debug("GET /api/usuarios/buscar?nomeUsuario={}", nomeUsuario);
        UsuarioDTO usuario = usuarioService.findByNomeUsuario(nomeUsuario);
        return ResponseEntity.ok(ApiResponseDTO.success(usuario));
    }
    
    /**
     * Cria novo usuário
     */
    @PostMapping
    @Operation(summary = "Criar usuário", description = "Cadastra um novo usuário no sistema")
    public ResponseEntity<ApiResponseDTO<UsuarioDTO>> create(
            @Valid @RequestBody UsuarioDTO dto) {
        
        log.info("POST /api/usuarios - Criando usuário: {}", dto.getNomeUsuario());
        UsuarioDTO created = usuarioService.create(dto);
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success("Usuário criado com sucesso", created));
    }
    
    /**
     * Atualiza usuário existente
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuário", description = "Atualiza dados de um usuário")
    public ResponseEntity<ApiResponseDTO<UsuarioDTO>> update(
            @PathVariable Integer id,
            @Valid @RequestBody UsuarioDTO dto) {
        
        log.info("PUT /api/usuarios/{}", id);
        UsuarioDTO updated = usuarioService.update(id, dto);
        
        return ResponseEntity.ok(
                ApiResponseDTO.success("Usuário atualizado com sucesso", updated));
    }
    
    /**
     * Ativa/desativa usuário
     * PATCH /api/usuarios/{id}/toggle-ativo
     */
    @PatchMapping("/{id}/toggle-ativo")
    @Operation(summary = "Ativar/Desativar", description = "Alterna o status ativo do usuário")
    public ResponseEntity<ApiResponseDTO<UsuarioDTO>> toggleAtivo(@PathVariable Integer id) {
        log.info("PATCH /api/usuarios/{}/toggle-ativo", id);
        UsuarioDTO updated = usuarioService.toggleAtivo(id);
        
        return ResponseEntity.ok(
                ApiResponseDTO.success("Status atualizado com sucesso", updated));
    }
    
    /**
     * Remove usuário (soft delete - apenas desativa)
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Remover usuário", 
               description = "Desativa um usuário (soft delete)")
    public ResponseEntity<ApiResponseDTO<Void>> delete(@PathVariable Integer id) {
        log.info("DELETE /api/usuarios/{}", id);
        usuarioService.delete(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Usuário desativado com sucesso", null));
    }
}
