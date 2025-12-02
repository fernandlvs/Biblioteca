package com.biblioteca.repositorio;

import com.biblioteca.model.Livro;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

/**
 * Repositório para acesso aos dados de Livro
 *
 * Implementa operações CRUD (Create, Read, Update, Delete)
 * na tabela 'livro' do banco de dados
 */
@Repository
public class LivroRepositorio {

    private final JdbcTemplate jdbcTemplate;

    // RowMapper: converte ResultSet (linha do banco) em objeto Livro
    private final RowMapper<Livro> livroRowMapper = (rs, rowNum) -> {
        Livro livro = new Livro();
        livro.setIdLivro(rs.getInt("id_livro"));
        livro.setIsbn(rs.getString("isbn"));
        livro.setTitulo(rs.getString("titulo"));
        livro.setAnoPublicacao(rs.getInt("ano_publicacao"));
        return livro;
    };

    public LivroRepositorio(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * CREATE - Insere um novo livro no banco
     *
     * @param livro dados do livro a ser inserido
     * @return livro com ID gerado pelo banco
     */
    public Livro inserir(Livro livro) {
        String sql = "INSERT INTO livro (isbn, titulo, ano_publicacao) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, livro.getIsbn());
            ps.setString(2, livro.getTitulo());
            ps.setInt(3, livro.getAnoPublicacao());
            return ps;
        }, keyHolder);

        livro.setIdLivro(keyHolder.getKey().intValue());
        return livro;
    }

    /**
     * READ - Busca todos os livros
     *
     * @return lista com todos os livros cadastrados
     */
    public List<Livro> buscarTodos() {
        String sql = "SELECT * FROM livro ORDER BY titulo";
        return jdbcTemplate.query(sql, livroRowMapper);
    }

    /**
     * READ - Busca um livro por ID
     *
     * @param id ID do livro
     * @return Optional contendo o livro ou vazio se não encontrado
     */
    public Optional<Livro> buscarPorId(Integer id) {
        String sql = "SELECT * FROM livro WHERE id_livro = ?";
        List<Livro> livros = jdbcTemplate.query(sql, livroRowMapper, id);
        return livros.isEmpty() ? Optional.empty() : Optional.of(livros.get(0));
    }

    /**
     * READ - Busca um livro por ISBN
     *
     * @param isbn ISBN do livro
     * @return Optional contendo o livro ou vazio se não encontrado
     */
    public Optional<Livro> buscarPorIsbn(String isbn) {
        String sql = "SELECT * FROM livro WHERE isbn = ?";
        List<Livro> livros = jdbcTemplate.query(sql, livroRowMapper, isbn);
        return livros.isEmpty() ? Optional.empty() : Optional.of(livros.get(0));
    }

    /**
     * READ - Busca livros por título (busca parcial)
     *
     * @param titulo parte do título a ser buscado
     * @return lista de livros que contém o texto no título
     */
    public List<Livro> buscarPorTitulo(String titulo) {
        String sql = "SELECT * FROM livro WHERE titulo LIKE ? ORDER BY titulo";
        return jdbcTemplate.query(sql, livroRowMapper, "%" + titulo + "%");
    }

    /**
     * UPDATE - Atualiza os dados de um livro
     *
     * @param id ID do livro a ser atualizado
     * @param livro novos dados do livro
     * @return número de linhas afetadas (1 se sucesso, 0 se não encontrado)
     */
    public int atualizar(Integer id, Livro livro) {
        String sql = "UPDATE livro SET isbn = ?, titulo = ?, ano_publicacao = ? WHERE id_livro = ?";
        return jdbcTemplate.update(sql,
                livro.getIsbn(),
                livro.getTitulo(),
                livro.getAnoPublicacao(),
                id
        );
    }

    /**
     * DELETE - Remove um livro do banco
     *
     * @param id ID do livro a ser removido
     * @return número de linhas afetadas (1 se sucesso, 0 se não encontrado)
     */
    public int deletar(Integer id) {
        String sql = "DELETE FROM livro WHERE id_livro = ?";
        return jdbcTemplate.update(sql, id);
    }

    /**
     * Chama a função do banco que conta autores de um livro
     *
     * @param idLivro ID do livro
     * @return quantidade de autores associados ao livro
     */
    public Integer contarAutores(Integer idLivro) {
        String sql = "SELECT fn_contar_autores_livro(?)";
        return jdbcTemplate.queryForObject(sql, Integer.class, idLivro);
    }
}
