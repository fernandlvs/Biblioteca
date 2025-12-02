package com.biblioteca.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Classe que representa um Livro da biblioteca
 *
 * Mapeia a tabela 'livro' do banco de dados
 */
public class Livro {

    private Integer idLivro;

    @NotBlank(message = "ISBN é obrigatório")
    @Size(max = 30, message = "ISBN deve ter no máximo 30 caracteres")
    private String isbn;

    @NotBlank(message = "Título é obrigatório")
    @Size(max = 255, message = "Título deve ter no máximo 255 caracteres")
    private String titulo;

    @NotNull(message = "Ano de publicação é obrigatório")
    @Positive(message = "Ano de publicação deve ser positivo")
    private Integer anoPublicacao;

    // Construtores
    public Livro() {
    }

    public Livro(String isbn, String titulo, Integer anoPublicacao) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.anoPublicacao = anoPublicacao;
    }

    // Getters e Setters
    public Integer getIdLivro() {
        return idLivro;
    }

    public void setIdLivro(Integer idLivro) {
        this.idLivro = idLivro;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(Integer anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    @Override
    public String toString() {
        return "Livro{" +
                "idLivro=" + idLivro +
                ", isbn='" + isbn + '\'' +
                ", titulo='" + titulo + '\'' +
                ", anoPublicacao=" + anoPublicacao +
                '}';
    }
}
