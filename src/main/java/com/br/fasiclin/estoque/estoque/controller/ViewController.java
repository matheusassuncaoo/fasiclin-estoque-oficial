package com.br.fasiclin.estoque.estoque.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/ordemcompra")
    public String ordemCompra() {
        return "ordemcompra";
    }

    @GetMapping("/movimentacaoestoque")
    public String movimentacaoEstoque() {
        return "movimentacaoestoque";
    }

    @GetMapping("/validacaoalmoxarifado")
    public String validacaoAlmoxarifado() {
        return "validacaoalmoxarifado";
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/ordemcompra";
    }
}
