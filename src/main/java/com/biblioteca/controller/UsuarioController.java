package com.biblioteca.controller;

import com.biblioteca.model.Usuario;
import com.biblioteca.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller REST para gerenciar Usuários
 *
 * Endpoints disponíveis:
 * - POST   /usuarios          - Criar usuário
 * - GET    /usuarios          - Listar todos
 * - GET    /usuarios/{id}     - Buscar por ID
 * - PUT    /usuarios/{id}     - Atualizar
 * - DELETE /usuarios/{id}     - Deletar
 * - GET    /usuarios/{id}/emprestimos-ativos - Total de empréstimos ativos
 */
@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*") // Permite requisições de qualquer origem (para desenvolvimento)
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * CREATE - Criar um novo usuário
     *
     * POST /api/usuarios
     * Body: { "matricula": "2023001", "nome": "João Silva", ... }
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> criar(@Valid @RequestBody Usuario usuario) {
        try {
            Usuario usuarioCriado = usuarioService.criar(usuario);

            Map<String, Object> resposta = new HashMap<>();
            resposta.put("sucesso", true);
            resposta.put("mensagem", "Usuário criado com sucesso!");
            resposta.put("usuario", usuarioCriado);

            return ResponseEntity.status(HttpStatus.CREATED).body(resposta);

        } catch (IllegalArgumentException e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("sucesso", false);
            erro.put("mensagem", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
        }
    }

    /**
     * READ - Listar todos os usuários
     *
     * GET /api/usuarios
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> listarTodos() {
        List<Usuario> usuarios = usuarioService.listarTodos();

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("sucesso", true);
        resposta.put("total", usuarios.size());
        resposta.put("usuarios", usuarios);

        return ResponseEntity.ok(resposta);
    }

    /**
     * READ - Buscar usuário por ID
     *
     * GET /api/usuarios/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> buscarPorId(@PathVariable Integer id) {
        try {
            Usuario usuario = usuarioService.buscarPorId(id);

            Map<String, Object> resposta = new HashMap<>();
            resposta.put("sucesso", true);
            resposta.put("usuario", usuario);

            return ResponseEntity.ok(resposta);

        } catch (IllegalArgumentException e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("sucesso", false);
            erro.put("mensagem", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
        }
    }

    /**
     * UPDATE - Atualizar usuário
     *
     * PUT /api/usuarios/{id}
     * Body: { "matricula": "2023001", "nome": "João Silva Atualizado", ... }
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> atualizar(
            @PathVariable Integer id,
            @Valid @RequestBody Usuario usuario) {
        try {
            Usuario usuarioAtualizado = usuarioService.atualizar(id, usuario);

            Map<String, Object> resposta = new HashMap<>();
            resposta.put("sucesso", true);
            resposta.put("mensagem", "Usuário atualizado com sucesso!");
            resposta.put("usuario", usuarioAtualizado);

            return ResponseEntity.ok(resposta);

        } catch (IllegalArgumentException e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("sucesso", false);
            erro.put("mensagem", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
        }
    }

    /**
     * DELETE - Deletar usuário
     *
     * DELETE /api/usuarios/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletar(@PathVariable Integer id) {
        try {
            usuarioService.deletar(id);

            Map<String, Object> resposta = new HashMap<>();
            resposta.put("sucesso", true);
            resposta.put("mensagem", "Usuário deletado com sucesso!");

            return ResponseEntity.ok(resposta);

        } catch (IllegalArgumentException e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("sucesso", false);
            erro.put("mensagem", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
        }
    }

    /**
     * Obtém total de empréstimos ativos do usuário
     * Chama a função fn_obter_total_emprestimos_ativos do banco
     *
     * GET /api/usuarios/{id}/emprestimos-ativos
     */
    @GetMapping("/{id}/emprestimos-ativos")
    public ResponseEntity<Map<String, Object>> obterEmprestimosAtivos(@PathVariable Integer id) {
        try {
            Integer total = usuarioService.obterTotalEmprestimosAtivos(id);

            Map<String, Object> resposta = new HashMap<>();
            resposta.put("sucesso", true);
            resposta.put("idUsuario", id);
            resposta.put("totalEmprestimosAtivos", total);

            return ResponseEntity.ok(resposta);

        } catch (IllegalArgumentException e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("sucesso", false);
            erro.put("mensagem", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
        }
    }
}
