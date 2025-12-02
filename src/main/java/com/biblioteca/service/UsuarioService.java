package com.biblioteca.service;

import com.biblioteca.model.Usuario;
import com.biblioteca.repositorio.UsuarioRepositorio;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Serviço de Usuário - contém a lógica de negócio
 *
 * Faz a ponte entre o Controller (requisições HTTP)
 * e o Repository (acesso ao banco)
 */
@Service
public class UsuarioService {

    private final UsuarioRepositorio usuarioRepositorio;

    public UsuarioService(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    /**
     * Cria um novo usuário
     *
     * @param usuario dados do usuário
     * @return usuário criado com ID
     * @throws IllegalArgumentException se matrícula já existe
     */
    public Usuario criar(Usuario usuario) {
        // Validação: verifica se matrícula já existe
        Optional<Usuario> usuarioExistente = usuarioRepositorio.buscarPorMatricula(usuario.getMatricula());
        if (usuarioExistente.isPresent()) {
            throw new IllegalArgumentException("Matrícula já cadastrada: " + usuario.getMatricula());
        }

        return usuarioRepositorio.inserir(usuario);
    }

    /**
     * Lista todos os usuários
     *
     * @return lista de usuários
     */
    public List<Usuario> listarTodos() {
        return usuarioRepositorio.buscarTodos();
    }

    /**
     * Busca um usuário por ID
     *
     * @param id ID do usuário
     * @return usuário encontrado
     * @throws IllegalArgumentException se usuário não existe
     */
    public Usuario buscarPorId(Integer id) {
        return usuarioRepositorio.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com ID: " + id));
    }

    /**
     * Atualiza os dados de um usuário
     *
     * @param id ID do usuário
     * @param usuario novos dados
     * @return usuário atualizado
     * @throws IllegalArgumentException se usuário não existe
     */
    public Usuario atualizar(Integer id, Usuario usuario) {
        // Verifica se o usuário existe
        buscarPorId(id);

        // Verifica se a nova matrícula já está em uso por outro usuário
        Optional<Usuario> usuarioComMesmaMatricula = usuarioRepositorio.buscarPorMatricula(usuario.getMatricula());
        if (usuarioComMesmaMatricula.isPresent() && !usuarioComMesmaMatricula.get().getIdUsuario().equals(id)) {
            throw new IllegalArgumentException("Matrícula já em uso por outro usuário: " + usuario.getMatricula());
        }

        usuarioRepositorio.atualizar(id, usuario);
        usuario.setIdUsuario(id);
        return usuario;
    }

    /**
     * Deleta um usuário
     *
     * @param id ID do usuário
     * @throws IllegalArgumentException se usuário não existe
     */
    public void deletar(Integer id) {
        buscarPorId(id); // Verifica se existe
        usuarioRepositorio.deletar(id);
    }

    /**
     * Obtém o total de empréstimos ativos de um usuário
     * Chama a função fn_obter_total_emprestimos_ativos do banco
     *
     * @param id ID do usuário
     * @return quantidade de empréstimos ativos
     */
    public Integer obterTotalEmprestimosAtivos(Integer id) {
        buscarPorId(id); // Verifica se usuário existe
        return usuarioRepositorio.contarEmprestimosAtivos(id);
    }
}
