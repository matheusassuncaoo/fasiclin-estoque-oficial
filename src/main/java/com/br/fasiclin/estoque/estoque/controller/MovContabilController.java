package com.br.fasiclin.estoque.estoque.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.br.fasiclin.estoque.estoque.model.MovContabil;
import com.br.fasiclin.estoque.estoque.service.MovContabilService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * Controller REST para gerenciamento de movimentações contábeis.
 * 
 * Fornece endpoints para operações CRUD e consultas específicas
 * relacionadas às movimentações contábeis.
 * 
 * @author Sistema Fasiclin
 * @version 1.0
 * @since 2024
 */
@RestController
@RequestMapping("/api/movimentacoes-contabeis")
@Tag(name = "Movimentações Contábeis", description = "Operações relacionadas às movimentações contábeis")
public class MovContabilController {

    @Autowired
    private MovContabilService movContabilService;

    /**
     * Busca uma movimentação contábil por ID.
     * 
     * @param id ID da movimentação
     * @return ResponseEntity com a movimentação encontrada
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar movimentação por ID", description = "Retorna uma movimentação contábil específica pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimentação encontrada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MovContabil.class))),
            @ApiResponse(responseCode = "404", description = "Movimentação não encontrada", content = @Content),
            @ApiResponse(responseCode = "400", description = "ID inválido", content = @Content)
    })
    public ResponseEntity<MovContabil> findById(
            @Parameter(description = "ID da movimentação contábil", required = true) @PathVariable @NotNull Integer id) {
        try {
            MovContabil movContabil = movContabilService.findById(id);
            return ResponseEntity.ok(movContabil);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Lista todas as movimentações contábeis.
     * 
     * @return ResponseEntity com lista de movimentações
     */
    @GetMapping
    @Operation(summary = "Listar todas as movimentações", description = "Retorna uma lista com todas as movimentações contábeis")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MovContabil.class)))
    })
    public ResponseEntity<List<MovContabil>> findAll() {
        List<MovContabil> movimentacoes = movContabilService.findAll();
        return ResponseEntity.ok(movimentacoes);
    }

    /**
     * Cria uma nova movimentação contábil.
     * 
     * @param movContabil Dados da movimentação a ser criada
     * @return ResponseEntity com a movimentação criada
     */
    @PostMapping
    @Operation(summary = "Criar nova movimentação", description = "Cria uma nova movimentação contábil")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Movimentação criada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MovContabil.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito de dados", content = @Content)
    })
    public ResponseEntity<MovContabil> create(
            @Parameter(description = "Dados da movimentação contábil", required = true) @Valid @RequestBody MovContabil movContabil) {
        try {
            MovContabil novaMovimentacao = movContabilService.create(movContabil);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaMovimentacao);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Atualiza uma movimentação contábil existente.
     * 
     * @param movContabil Dados atualizados da movimentação
     * @return ResponseEntity com a movimentação atualizada
     */
    @PutMapping
    @Operation(summary = "Atualizar movimentação", description = "Atualiza uma movimentação contábil existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimentação atualizada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MovContabil.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Movimentação não encontrada", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito de dados", content = @Content)
    })
    public ResponseEntity<MovContabil> update(
            @Parameter(description = "Dados atualizados da movimentação", required = true) @Valid @RequestBody MovContabil movContabil) {
        try {
            MovContabil movimentacaoAtualizada = movContabilService.update(movContabil);
            return ResponseEntity.ok(movimentacaoAtualizada);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /**
     * Remove uma movimentação contábil por ID.
     * 
     * @param id ID da movimentação a ser removida
     * @return ResponseEntity indicando o resultado da operação
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Remover movimentação", description = "Remove uma movimentação contábil pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Movimentação removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Movimentação não encontrada"),
            @ApiResponse(responseCode = "400", description = "ID inválido"),
            @ApiResponse(responseCode = "409", description = "Conflito - movimentação não pode ser removida")
    })
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "ID da movimentação a ser removida", required = true) @PathVariable @NotNull Integer id) {
        try {
            movContabilService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /*
    // Métodos comentados pois não existem no Service atual
    // ... (código original omitido para brevidade) ...
    */
}