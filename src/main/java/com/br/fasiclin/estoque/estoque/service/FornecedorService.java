package com.br.fasiclin.estoque.estoque.service;

import com.br.fasiclin.estoque.estoque.dto.FornecedorDTO;
import com.br.fasiclin.estoque.estoque.exception.BusinessException;
import com.br.fasiclin.estoque.estoque.exception.ResourceNotFoundException;
import com.br.fasiclin.estoque.estoque.model.Fornecedor;
import com.br.fasiclin.estoque.estoque.model.PessoaJuridica;
import com.br.fasiclin.estoque.estoque.repository.FornecedorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para gerenciamento de Fornecedores.
 * 
 * @author Sistema Fasiclin - Módulo Estoque
 * @version 1.0
 * @since 2025
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;

    /**
     * Busca fornecedor por ID
     */
    public FornecedorDTO findById(Integer id) {
        log.debug("Buscando fornecedor com ID: {}", id);
        Fornecedor fornecedor = fornecedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor", "id", id));
        return convertToDTO(fornecedor);
    }

    /**
     * Lista todos os fornecedores com paginação
     */
    public Page<FornecedorDTO> findAll(Pageable pageable) {
        log.debug("Listando fornecedores com paginação: {}", pageable);
        return fornecedorRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    /**
     * Busca fornecedores por representante
     */
    public List<FornecedorDTO> findByRepresentante(String representante) {
        log.debug("Buscando fornecedores por representante: {}", representante);
        return fornecedorRepository.findByRepresentanteContaining(representante).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Cria novo fornecedor
     */
    @Transactional
    public FornecedorDTO create(FornecedorDTO dto) {
        log.info("Criando novo fornecedor");

        validateFornecedor(dto);

        Fornecedor fornecedor = convertToEntity(dto);
        Fornecedor saved = fornecedorRepository.save(fornecedor);
        
        log.info("Fornecedor criado com sucesso. ID: {}", saved.getId());
        return convertToDTO(saved);
    }

    /**
     * Atualiza fornecedor
     */
    @Transactional
    public FornecedorDTO update(Integer id, FornecedorDTO dto) {
        log.info("Atualizando fornecedor ID: {}", id);

        Fornecedor existing = fornecedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor", "id", id));

        validateFornecedor(dto);
        updateEntity(existing, dto);

        Fornecedor updated = fornecedorRepository.save(existing);
        log.info("Fornecedor atualizado com sucesso. ID: {}", id);

        return convertToDTO(updated);
    }

    /**
     * Remove fornecedor
     */
    @Transactional
    public void delete(Integer id) {
        log.info("Removendo fornecedor ID: {}", id);

        Fornecedor fornecedor = fornecedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor", "id", id));

        fornecedorRepository.delete(fornecedor);
        log.info("Fornecedor removido com sucesso");
    }

    // ========== VALIDAÇÕES ==========

    private void validateFornecedor(FornecedorDTO dto) {
        if (dto.getIdPessoaJuridica() == null) {
            throw new BusinessException("Pessoa jurídica é obrigatória");
        }

        if (dto.getRepresentante() != null && dto.getRepresentante().length() > 100) {
            throw new BusinessException("Nome do representante não pode exceder 100 caracteres");
        }

        if (dto.getContatoRepresentante() != null && dto.getContatoRepresentante().length() > 15) {
            throw new BusinessException("Contato não pode exceder 15 caracteres");
        }
    }

    // ========== CONVERSÃO DTO <-> ENTITY ==========

    private FornecedorDTO convertToDTO(Fornecedor entity) {
        return FornecedorDTO.builder()
                .id(entity.getId())
                .idPessoaJuridica(entity.getPessoasJuridica() != null ? entity.getPessoasJuridica().getId() : null)
                .representante(entity.getRepresentante())
                .contatoRepresentante(entity.getContatoRepresentante())
                .condicoesPagamento(entity.getDescricao())
                .razaoSocial(entity.getPessoasJuridica() != null ? entity.getPessoasJuridica().getRazaoSocial() : null)
                .cnpj(entity.getPessoasJuridica() != null ? entity.getPessoasJuridica().getCnpj() : null)
                .build();
    }

    private Fornecedor convertToEntity(FornecedorDTO dto) {
        Fornecedor fornecedor = new Fornecedor();
        updateEntity(fornecedor, dto);
        return fornecedor;
    }

    private void updateEntity(Fornecedor entity, FornecedorDTO dto) {
        // Criar PessoaJuridica com ID
        if (dto.getIdPessoaJuridica() != null) {
            PessoaJuridica pj = new PessoaJuridica();
            pj.setId(dto.getIdPessoaJuridica());
            entity.setPessoasJuridica(pj);
        }
        
        entity.setRepresentante(dto.getRepresentante());
        entity.setContatoRepresentante(dto.getContatoRepresentante());
        entity.setDescricao(dto.getCondicoesPagamento());
    }
}
