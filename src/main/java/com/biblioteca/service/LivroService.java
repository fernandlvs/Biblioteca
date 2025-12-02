package com.biblioteca.service;

import com.biblioteca.model.Livro;
import com.biblioteca.repositorio.LivroRepositorio;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Serviço de Livro - contém a lógica de negócio
 *
 * Faz a ponte entre o Controller (requisições HTTP)
 * e o Repository (acesso ao banco)
 */
@Service
public class LivroService {

    private final LivroRepositorio livroRepositorio;

    public LivroService(LivroRepositorio livroRepositorio) {
        this.livroRepositorio = livroRepositorio;
    }

    /**
     * Cria um novo livro
     *
     * @param livro dados do livro
     * @return livro criado com ID
     * @throws IllegalArgumentException se ISBN já existe
     */
    public Livro criar(Livro livro) {
        // Validação: verifica se ISBN já existe
        Optional<Livro> livroExistente = livroRepositorio.buscarPorIsbn(livro.getIsbn());
        if (livroExistente.isPresent()) {
            throw new IllegalArgumentException("ISBN já cadastrado: " + livro.getIsbn());
        }

        return livroRepositorio.inserir(livro);
    }

    /**
     * Lista todos os livros
     *
     * @return lista de livros
     */
    public List<Livro> listarTodos() {
        return livroRepositorio.buscarTodos();
    }

    /**
     * Busca um livro por ID
     *
     * @param id ID do livro
     * @return livro encontrado
     * @throws IllegalArgumentException se livro não existe
     */
    public Livro buscarPorId(Integer id) {
        return livroRepositorio.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado com ID: " + id));
    }

    /**
     * Busca livros por título (busca parcial)
     *
     * @param titulo parte do título
     * @return lista de livros encontrados
     */
    public List<Livro> buscarPorTitulo(String titulo) {
        return livroRepositorio.buscarPorTitulo(titulo);
    }

    /**
     * Atualiza os dados de um livro
     *
     * @param id ID do livro
     * @param livro novos dados
     * @return livro atualizado
     * @throws IllegalArgumentException se livro não existe
     */
    public Livro atualizar(Integer id, Livro livro) {
        // Verifica se o livro existe
        buscarPorId(id);

        // Verifica se o novo ISBN já está em uso por outro livro
        Optional<Livro> livroComMesmoIsbn = livroRepositorio.buscarPorIsbn(livro.getIsbn());
        if (livroComMesmoIsbn.isPresent() && !livroComMesmoIsbn.get().getIdLivro().equals(id)) {
            throw new IllegalArgumentException("ISBN já em uso por outro livro: " + livro.getIsbn());
        }

        livroRepositorio.atualizar(id, livro);
        livro.setIdLivro(id);
        return livro;
    }

    /**
     * Deleta um livro
     *
     * @param id ID do livro
     * @throws IllegalArgumentException se livro não existe
     */
    public void deletar(Integer id) {
        buscarPorId(id); // Verifica se existe
        livroRepositorio.deletar(id);
    }

    /**
     * Obtém o total de autores de um livro
     * Chama a função fn_contar_autores_livro do banco
     *
     * @param id ID do livro
     * @return quantidade de autores associados
     */
    public Integer obterTotalAutores(Integer id) {
        buscarPorId(id); // Verifica se livro existe
        return livroRepositorio.contarAutores(id);
    }
}
