package com.biblioteca.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * Classe de configuração do banco de dados
 *
 * Configura a conexão com o MySQL usando as propriedades
 * definidas no application.properties
 *
 * @author Fernanda Alves, Ana Gusmão, Amanda Gabrielly
 */
@Configuration
public class DataBaseConfig {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    /**
     * Cria o DataSource (fonte de dados) para conexão com o banco
     *
     * @return DataSource configurado
     */
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        System.out.println("   Conexão com banco de dados configurada:");
        System.out.println("   URL: " + url);
        System.out.println("   Usuário: " + username);

        return dataSource;
    }

    /**
     * Cria o JdbcTemplate para executar queries SQL
     *
     * JdbcTemplate facilita a execução de comandos SQL
     * e o mapeamento de resultados para objetos Java
     *
     * @param dataSource fonte de dados configurada
     * @return JdbcTemplate pronto para uso
     */
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
