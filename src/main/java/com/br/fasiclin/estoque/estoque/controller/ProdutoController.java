package com.br.fasiclin.estoque.estoque.controller;

import com.br.fasiclin.estoque.estoque.dto.ApiResponseDTO;
import com.br.fasiclin.estoque.estoque.dto.ProdutoDTO;
import com.br.fasiclin.estoque.estoque.service.ProdutoService;
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
 * Controller REST para gerenciamento de Produtos.
 * Endpoints REST (/api/produtos) para uso pelos 3 sistemas clientes.
 * 
 * @author Sistema Fasiclin - Módulo Estoque
 * @version 1.0
 * @since 2025
 */
@Slf4j
@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
@Tag(name = "Produtos", description = "Gerenciamento de produtos do estoque")
public class ProdutoController {
    
    private final ProdutoService produtoService;
    
    /**
     * Lista todos os produtos com paginação
     * GET /api/produtos?page=0&size=20
     */
    @GetMapping
    @Operation(summary = "Listar produtos", description = "Lista todos os produtos com suporte a paginação")
    public ResponseEntity<ApiResponseDTO<Page<ProdutoDTO>>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.debug("GET /api/produtos - page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<ProdutoDTO> produtos = produtoService.findAll(pageable);
        
        return ResponseEntity.ok(ApiResponseDTO.success(produtos));
    }
    
    /**
     * Busca produto por ID
     * GET /api/produtos/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID", description = "Retorna um produto específico pelo ID")
    public ResponseEntity<ApiResponseDTO<ProdutoDTO>> findById(@PathVariable Integer id) {
        log.debug("GET /api/produtos/{}", id);
        ProdutoDTO produto = produtoService.findById(id);
        return ResponseEntity.ok(ApiResponseDTO.success(produto));
    }
    
    /**
     * Busca produtos por nome (parcial)
     * GET /api/produtos/buscar?nome=paracetamol
     */
    @GetMapping("/buscar")
    @Operation(summary = "Buscar por nome", description = "Busca produtos que contenham o nome especificado")
    public ResponseEntity<ApiResponseDTO<List<ProdutoDTO>>> findByNome(
            @RequestParam String nome) {
        
        log.debug("GET /api/produtos/buscar?nome={}", nome);
        List<ProdutoDTO> produtos = produtoService.findByNome(nome);
        return ResponseEntity.ok(ApiResponseDTO.success(produtos));
    }
    
    /**
     * Produtos que precisam reposição
     * GET /api/produtos/reposicao
     */
    @GetMapping("/reposicao")
    @Operation(summary = "Produtos para reposição", 
               description = "Lista produtos com estoque abaixo do ponto de pedido")
    public ResponseEntity<ApiResponseDTO<List<ProdutoDTO>>> produtosParaReposicao() {
        log.debug("GET /api/produtos/reposicao");
        List<ProdutoDTO> produtos = produtoService.findProdutosParaReposicao();
        return ResponseEntity.ok(ApiResponseDTO.success(produtos));
    }
    
    /**
     * Produtos com estoque baixo
     * GET /api/produtos/estoque-baixo
     */
    @GetMapping("/estoque-baixo")
    @Operation(summary = "Produtos com estoque baixo",
               description = "Lista produtos com estoque entre ponto de pedido e mínimo")
    public ResponseEntity<ApiResponseDTO<List<ProdutoDTO>>> produtosEstoqueBaixo() {
        log.debug("GET /api/produtos/estoque-baixo");
        List<ProdutoDTO> produtos = produtoService.findProdutosEstoqueBaixo();
        return ResponseEntity.ok(ApiResponseDTO.success(produtos));
    }
    
    /**
     * Cria novo produto
     * POST /api/produtos
     */
    @PostMapping
    @Operation(summary = "Criar produto", description = "Cadastra um novo produto no sistema")
    public ResponseEntity<ApiResponseDTO<ProdutoDTO>> create(@Valid @RequestBody ProdutoDTO dto) {
        log.info("POST /api/produtos - Criando produto: {}", dto.getNome());
        ProdutoDTO created = produtoService.create(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseDTO.success("Produto criado com sucesso", created));
    }
    
    /**
     * Atualiza produto existente
     * PUT /api/produtos/{id}
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar produto", description = "Atualiza dados de um produto existente")
    public ResponseEntity<ApiResponseDTO<ProdutoDTO>> update(
            @PathVariable Integer id,
            @Valid @RequestBody ProdutoDTO dto) {
        
        log.info("PUT /api/produtos/{} - Atualizando produto", id);
        ProdutoDTO updated = produtoService.update(id, dto);
        return ResponseEntity.ok(ApiResponseDTO.success("Produto atualizado com sucesso", updated));
    }
    
    /**
     * Remove produto
     * DELETE /api/produtos/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Remover produto", description = "Remove um produto do sistema")
    public ResponseEntity<ApiResponseDTO<Void>> delete(@PathVariable Integer id) {
        log.info("DELETE /api/produtos/{} - Removendo produto", id);
        produtoService.delete(id);
        return ResponseEntity.ok(ApiResponseDTO.success("Produto removido com sucesso", null));
    }
}
