package com.br.fasiclin.estoque.estoque.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller para servir as páginas HTML via Thymeleaf.
 * Responsável por rotear as requisições para os 3 sistemas:
 * - Ordem de Compra
 * - Movimentação de Estoque
 * - Validação de Almoxarifado
 * 
 * @author Sistema Fasiclin - Módulo Estoque
 * @version 1.0
 * @since 2025
 */
@Controller
public class ViewController {
    
    /**
     * Página de Ordem de Compra
     * URL: /ordemcompra
     */
    @GetMapping("/ordemcompra")
    public String ordemCompra() {
        return "ordemcompra";
    }
    
    /**
     * Página de Movimentação de Estoque
     * URL: /movimentacaoestoque
     */
    @GetMapping("/movimentacaoestoque")
    public String movimentacaoEstoque() {
        return "movimentacaoestoque";
    }
    
    /**
     * Página de Validação de Almoxarifado
     * URL: /validacaoalmoxarifado
     */
    @GetMapping("/validacaoalmoxarifado")
    public String validacaoAlmoxarifado() {
        return "validacaoalmoxarifado";
    }
    
    /**
     * Redireciona raiz para ordem de compra (padrão)
     */
    @GetMapping("/")
    public String index() {
        return "redirect:/ordemcompra";
    }
}
