package com.br.fasiclin.estoque.estoque.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

/**
 * Configuração de DataSources para múltiplos usuários.
 * 
 * Cada módulo usa um datasource específico:
 * - Matheus: Ordem de Compra
 * - Yuri: Movimentação de Estoque
 * - Erasmo: Validação de Almoxarifado
 * 
 * @author Sistema Fasiclin - Módulo Estoque
 * @version 1.0
 * @since 2025
 */
@Configuration
public class DataSourceConfig {

    private final Environment env;

    public DataSourceConfig(Environment env) {
        this.env = env;
    }

    /**
     * DataSource primário - Matheus (Ordem de Compra)
     * Este é o datasource padrão usado por todos os repositories
     */
    @Primary
    @Bean(name = "matheusDataSource")
    public DataSource matheusDataSource() {
        return DataSourceBuilder.create()
                .url(env.getProperty("datasource.matheus.url"))
                .username(env.getProperty("datasource.matheus.username"))
                .password(env.getProperty("datasource.matheus.password"))
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
    }

    /**
     * DataSource - Yuri (Movimentação de Estoque)
     */
    @Bean(name = "yuriDataSource")
    public DataSource yuriDataSource() {
        return DataSourceBuilder.create()
                .url(env.getProperty("datasource.yuri.url"))
                .username(env.getProperty("datasource.yuri.username"))
                .password(env.getProperty("datasource.yuri.password"))
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
    }

    /**
     * DataSource - Erasmo (Validação de Almoxarifado)
     */
    @Bean(name = "erasmoDataSource")
    public DataSource erasmoDataSource() {
        return DataSourceBuilder.create()
                .url(env.getProperty("datasource.erasmo.url"))
                .username(env.getProperty("datasource.erasmo.username"))
                .password(env.getProperty("datasource.erasmo.password"))
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .build();
    }
}
