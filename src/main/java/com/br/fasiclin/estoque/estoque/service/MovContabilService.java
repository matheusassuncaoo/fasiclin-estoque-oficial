package com.br.fasiclin.estoque.estoque.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.br.fasiclin.estoque.estoque.model.MovContabil;
import com.br.fasiclin.estoque.estoque.repository.MovContabilRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Service
public class MovContabilService {

    @Autowired
    private MovContabilRepository movContabilRepository;

    public MovContabil findById(@NotNull Integer id) {
        if (id == null) throw new IllegalArgumentException("ID não pode ser nulo");
        return movContabilRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Movimentação não encontrada ID: " + id));
    }

    public List<MovContabil> findAll() { return movContabilRepository.findAll(); }

    @Transactional
    public MovContabil create(@Valid @NotNull MovContabil obj) {
        if (obj == null) throw new IllegalArgumentException("Objeto não pode ser nulo");
        if (obj.getId() != null) throw new IllegalArgumentException("ID deve ser nulo na criação");
        validateBusiness(obj);
        try { return movContabilRepository.save(obj); }
        catch (DataIntegrityViolationException e) { throw e; }
    }

    @Transactional
    public MovContabil update(@Valid @NotNull MovContabil obj) {
        if (obj == null || obj.getId() == null) throw new IllegalArgumentException("ID obrigatório para update");
        MovContabil existing = findById(obj.getId());
        existing.setProduto(obj.getProduto());
        existing.setDataMovimentacao(obj.getDataMovimentacao());
        existing.setTipoMovimentacao(obj.getTipoMovimentacao());
        existing.setQuantidade(obj.getQuantidade());
        existing.setValorUnitario(obj.getValorUnitario());
        existing.setValorTotal(calcularValorTotal(existing));
        existing.setObservacao(obj.getObservacao());
        return movContabilRepository.save(existing);
    }

    @Transactional
    public void deleteById(@NotNull Integer id) {
        if (id == null) throw new IllegalArgumentException("ID não pode ser nulo");
        try { movContabilRepository.deleteById(id); }
        catch (EmptyResultDataAccessException e) { throw new EntityNotFoundException("ID não encontrado: " + id); }
    }

    @Transactional
    public void delete(@NotNull MovContabil obj) { if (obj == null) throw new IllegalArgumentException("Objeto nulo"); deleteById(obj.getId()); }

    public List<MovContabil> findByProduto(Integer produtoId) { return movContabilRepository.findByProduto_Id(produtoId); }
    public List<MovContabil> findByData(LocalDate data) { return movContabilRepository.findByDataMovimentacao(data); }
    public List<MovContabil> findByDataPeriodo(LocalDate inicio, LocalDate fim) { return movContabilRepository.findByDataMovimentacaoBetween(inicio, fim); }

    public BigDecimal sumValorTotalPeriodo(LocalDate inicio, LocalDate fim) { return movContabilRepository.sumValorTotalPeriodo(inicio, fim); }

    private void validateBusiness(MovContabil obj) {
        if (obj.getProduto() == null) throw new IllegalArgumentException("Produto é obrigatório");
        if (obj.getDataMovimentacao() == null) obj.setDataMovimentacao(LocalDate.now());
        if (obj.getQuantidade() == null || obj.getQuantidade() <= 0) throw new IllegalArgumentException("Quantidade deve ser > 0");
        obj.setValorTotal(calcularValorTotal(obj));
    }

    private BigDecimal calcularValorTotal(MovContabil m) {
        if (m.getValorUnitario() == null || m.getQuantidade() == null) return BigDecimal.ZERO;
        return m.getValorUnitario().multiply(BigDecimal.valueOf(m.getQuantidade()));
    }
}
