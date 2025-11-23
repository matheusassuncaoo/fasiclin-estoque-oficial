package com.br.fasiclin.estoque.estoque.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entidade representando um Usuário do sistema.
 * 
 * @author Sistema Fasiclin - Módulo Estoque
 * @version 1.0
 * @since 2025
 */
@Entity
@Table(name = "USUARIO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDUSUARIO")
    private Integer id;

    @NotBlank(message = "O nome de usuário é obrigatório.")
    @Size(max = 50, message = "O nome de usuário deve ter no máximo 50 caracteres.")
    @Column(name = "NOME_USUARIO", nullable = false, unique = true, length = 50)
    private String nomeUsuario;

    @NotBlank(message = "A senha é obrigatória.")
    @Size(max = 255, message = "A senha deve ter no máximo 255 caracteres.")
    @Column(name = "SENHA", nullable = false, length = 255)
    private String senha;

    @NotBlank(message = "O nome completo é obrigatório.")
    @Size(max = 100, message = "O nome completo deve ter no máximo 100 caracteres.")
    @Column(name = "NOME_COMPLETO", nullable = false, length = 100)
    private String nomeCompleto;

    @Size(max = 100, message = "O email deve ter no máximo 100 caracteres.")
    @Column(name = "EMAIL", length = 100)
    private String email;

    @Column(name = "ATIVO")
    private Boolean ativo = true;
}
