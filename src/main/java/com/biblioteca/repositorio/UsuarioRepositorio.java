package com.biblioteca.repositorio;

import com.biblioteca.model.Usuario;
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
 * Repositório para acesso aos dados de Usuário
 *
 * Implementa operações CRUD (Create, Read, Update, Delete)
 * na tabela 'usuario' do banco de dados
 */
@Repository
public class UsuarioRepositorio {

    private final JdbcTemplate jdbcTemplate;

    // RowMapper: converte ResultSet (linha do banco) em objeto Usuario
    private final RowMapper<Usuario> usuarioRowMapper = (rs, rowNum) -> {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(rs.getInt("id_usuario"));
        usuario.setMatricula(rs.getString("matricula"));
        usuario.setNome(rs.getString("nome"));
        usuario.setEmail(rs.getString("email"));
        usuario.setTelefone(rs.getString("telefone"));
        usuario.setCpf(rs.getString("cpf"));
        return usuario;
    };

    public UsuarioRepositorio(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * CREATE - Insere um novo usuário no banco
     *
     * @param usuario dados do usuário a ser inserido
     * @return usuário com ID gerado pelo banco
     */
    public Usuario inserir(Usuario usuario) {
        String sql = "INSERT INTO usuario (matricula, nome, email, telefone, cpf) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, usuario.getMatricula());
            ps.setString(2, usuario.getNome());
            ps.setString(3, usuario.getEmail());
            ps.setString(4, usuario.getTelefone());
            ps.setString(5, usuario.getCpf());
            return ps;
        }, keyHolder);

        usuario.setIdUsuario(keyHolder.getKey().intValue());
        return usuario;
    }

    /**
     * READ - Busca todos os usuários
     *
     * @return lista com todos os usuários cadastrados
     */
    public List<Usuario> buscarTodos() {
        String sql = "SELECT * FROM usuario ORDER BY nome";
        return jdbcTemplate.query(sql, usuarioRowMapper);
    }

    /**
     * READ - Busca um usuário por ID
     *
     * @param id ID do usuário
     * @return Optional contendo o usuário ou vazio se não encontrado
     */
    public Optional<Usuario> buscarPorId(Integer id) {
        String sql = "SELECT * FROM usuario WHERE id_usuario = ?";
        List<Usuario> usuarios = jdbcTemplate.query(sql, usuarioRowMapper, id);
        return usuarios.isEmpty() ? Optional.empty() : Optional.of(usuarios.get(0));
    }

    /**
     * READ - Busca um usuário por matrícula
     *
     * @param matricula matrícula do usuário
     * @return Optional contendo o usuário ou vazio se não encontrado
     */
    public Optional<Usuario> buscarPorMatricula(String matricula) {
        String sql = "SELECT * FROM usuario WHERE matricula = ?";
        List<Usuario> usuarios = jdbcTemplate.query(sql, usuarioRowMapper, matricula);
        return usuarios.isEmpty() ? Optional.empty() : Optional.of(usuarios.get(0));
    }

    /**
     * UPDATE - Atualiza os dados de um usuário
     *
     * @param id ID do usuário a ser atualizado
     * @param usuario novos dados do usuário
     * @return número de linhas afetadas (1 se sucesso, 0 se não encontrado)
     */
    public int atualizar(Integer id, Usuario usuario) {
        String sql = "UPDATE usuario SET matricula = ?, nome = ?, email = ?, telefone = ?, cpf = ? WHERE id_usuario = ?";
        return jdbcTemplate.update(sql,
                usuario.getMatricula(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone(),
                usuario.getCpf(),
                id
        );
    }

    /**
     * DELETE - Remove um usuário do banco
     *
     * @param id ID do usuário a ser removido
     * @return número de linhas afetadas (1 se sucesso, 0 se não encontrado)
     */
    public int deletar(Integer id) {
        String sql = "DELETE FROM usuario WHERE id_usuario = ?";
        return jdbcTemplate.update(sql, id);
    }

    /**
     * Chama a função do banco que conta empréstimos ativos do usuário
     *
     * @param idUsuario ID do usuário
     * @return quantidade de empréstimos ativos
     */
    public Integer contarEmprestimosAtivos(Integer idUsuario) {
        String sql = "SELECT fn_obter_total_emprestimos_ativos(?)";
        return jdbcTemplate.queryForObject(sql, Integer.class, idUsuario);
    }
}
