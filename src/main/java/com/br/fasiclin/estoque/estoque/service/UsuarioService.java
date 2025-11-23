package com.br.fasiclin.estoque.estoque.service;

import com.br.fasiclin.estoque.estoque.dto.UsuarioDTO;
import com.br.fasiclin.estoque.estoque.exception.BusinessException;
import com.br.fasiclin.estoque.estoque.exception.ResourceNotFoundException;
import com.br.fasiclin.estoque.estoque.model.Usuario;
import com.br.fasiclin.estoque.estoque.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service para gerenciamento de Usuários.
 * Gerencia autenticação, autorização e CRUD de usuários.
 * 
 * @author Sistema Fasiclin - Módulo Estoque
 * @version 1.0
 * @since 2025
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsuarioService {
    
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    
    /**
     * Busca usuário por ID
     */
    public UsuarioDTO findById(Integer id) {
        log.debug("Buscando usuário com ID: {}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", "id", id));
        return convertToDTO(usuario);
    }
    
    /**
     * Lista todos os usuários com paginação
     */
    public Page<UsuarioDTO> findAll(Pageable pageable) {
        log.debug("Listando usuários com paginação: {}", pageable);
        return usuarioRepository.findAll(pageable)
                .map(this::convertToDTO);
    }
    
    /**
     * Busca usuário por nome de usuário
     */
    public UsuarioDTO findByNomeUsuario(String nomeUsuario) {
        log.debug("Buscando usuário por nome: {}", nomeUsuario);
        Usuario usuario = usuarioRepository.findByNomeUsuario(nomeUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", "nomeUsuario", nomeUsuario));
        return convertToDTO(usuario);
    }
    
    /**
     * Cria novo usuário
     */
    @Transactional
    public UsuarioDTO create(UsuarioDTO dto) {
        log.info("Criando novo usuário: {}", dto.getNomeUsuario());
        
        // Validar se nome de usuário já existe
        if (usuarioRepository.findByNomeUsuario(dto.getNomeUsuario()).isPresent()) {
            throw new BusinessException("Nome de usuário já existe");
        }
        
        Usuario usuario = convertToEntity(dto);
        // Criptografar senha
        usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        usuario.setAtivo(true);
        
        Usuario saved = usuarioRepository.save(usuario);
        log.info("Usuário criado com sucesso. ID: {}", saved.getId());
        
        return convertToDTO(saved);
    }
    
    /**
     * Atualiza usuário existente
     */
    @Transactional
    public UsuarioDTO update(Integer id, UsuarioDTO dto) {
        log.info("Atualizando usuário ID: {}", id);
        
        Usuario existing = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", "id", id));
        
        // Validar se mudança de nome de usuário conflita com existente
        if (!existing.getNomeUsuario().equals(dto.getNomeUsuario())) {
            usuarioRepository.findByNomeUsuario(dto.getNomeUsuario())
                    .ifPresent(u -> {
                        throw new BusinessException("Nome de usuário já existe");
                    });
        }
        
        updateEntity(existing, dto);
        
        // Só atualiza senha se foi fornecida nova
        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            existing.setSenha(passwordEncoder.encode(dto.getSenha()));
        }
        
        Usuario updated = usuarioRepository.save(existing);
        log.info("Usuário atualizado com sucesso. ID: {}", id);
        
        return convertToDTO(updated);
    }
    
    /**
     * Ativa/desativa usuário
     */
    @Transactional
    public UsuarioDTO toggleAtivo(Integer id) {
        log.info("Alternando status ativo do usuário ID: {}", id);
        
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", "id", id));
        
        usuario.setAtivo(!usuario.getAtivo());
        Usuario updated = usuarioRepository.save(usuario);
        
        log.info("Status atualizado para: {}", updated.getAtivo());
        return convertToDTO(updated);
    }
    
    /**
     * Remove usuário (soft delete recomendado)
     */
    @Transactional
    public void delete(Integer id) {
        log.info("Removendo usuário ID: {}", id);
        
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", "id", id));
        
        // Em produção, prefira desativar ao invés de remover
        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
        
        log.info("Usuário desativado com sucesso");
    }
    
    // ========== CONVERSÃO DTO <-> ENTITY ==========
    
    private UsuarioDTO convertToDTO(Usuario entity) {
        return UsuarioDTO.builder()
                .id(entity.getId())
                .nomeUsuario(entity.getNomeUsuario())
                // NUNCA retornar senha
                .nomeCompleto(entity.getNomeCompleto())
                .email(entity.getEmail())
                .ativo(entity.getAtivo())
                .build();
    }
    
    private Usuario convertToEntity(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        updateEntity(usuario, dto);
        return usuario;
    }
    
    private void updateEntity(Usuario entity, UsuarioDTO dto) {
        entity.setNomeUsuario(dto.getNomeUsuario());
        entity.setNomeCompleto(dto.getNomeCompleto());
        entity.setEmail(dto.getEmail());
        if (dto.getAtivo() != null) {
            entity.setAtivo(dto.getAtivo());
        }
    }
}
