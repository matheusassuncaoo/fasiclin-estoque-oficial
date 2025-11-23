package com.br.fasiclin.estoque.estoque.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO padrão para respostas da API.
 * Garante consistência nas respostas para os 3 sistemas clientes.
 * 
 * @param <T> Tipo de dado retornado
 * @author Sistema Fasiclin - Módulo Estoque
 * @version 1.0
 * @since 2025
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Não inclui campos nulos no JSON
public class ApiResponseDTO<T> {
    
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    
    private boolean success;
    private String message;
    private T data;
    
    // Informações de paginação (quando aplicável)
    private PaginationInfo pagination;
    
    /**
     * Cria resposta de sucesso com dados
     */
    public static <T> ApiResponseDTO<T> success(T data) {
        return ApiResponseDTO.<T>builder()
                .timestamp(LocalDateTime.now())
                .success(true)
                .data(data)
                .build();
    }
    
    /**
     * Cria resposta de sucesso com mensagem e dados
     */
    public static <T> ApiResponseDTO<T> success(String message, T data) {
        return ApiResponseDTO.<T>builder()
                .timestamp(LocalDateTime.now())
                .success(true)
                .message(message)
                .data(data)
                .build();
    }
    
    /**
     * Cria resposta de erro
     */
    public static <T> ApiResponseDTO<T> error(String message) {
        return ApiResponseDTO.<T>builder()
                .timestamp(LocalDateTime.now())
                .success(false)
                .message(message)
                .build();
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaginationInfo {
        private int page;
        private int size;
        private long totalElements;
        private int totalPages;
        private boolean first;
        private boolean last;
    }
}
