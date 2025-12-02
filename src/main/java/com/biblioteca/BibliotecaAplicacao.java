package com.biblioteca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal da aplicação Biblioteca API
 *
 * Esta é a classe que inicia o servidor Spring Boot
 * e configura todos os componentes automaticamente.
 *
 * @author Fernanda Alves, Ana Gusmão, Amanda Gabrielly
 * @version 1.0
 */
@SpringBootApplication
public class BibliotecaAplicacao {

    public static void main(String[] args) {
        SpringApplication.run(BibliotecaAplicacao.class, args);
        System.out.println("===========================================");
        System.out.println("Biblioteca API iniciada com sucesso!");
        System.out.println("Acesse: http://localhost:8080/api");
        System.out.println("===========================================");
    }
}
