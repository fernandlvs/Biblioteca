package com.biblioteca.controller;

import com.biblioteca.model.Livro;
import com.biblioteca.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller REST para gerenciar Livros
 *
 * Endpoints disponíveis:
 * - POST   /livros          - Criar livro
 * - GET    /livros          - Listar todos
 * - GET    /livros/{id}     - Buscar por ID
 * - GET    /livros/buscar?titulo=...  - Buscar por título
 * - PUT    /livros/{id}     - Atualizar
 * - DELETE /livros/{id}     - Deletar
 * - GET    /livros/{id}/autores  - Total de autores do livro
 */
@RestController
@RequestMapping("/livros")
@CrossOrigin(origins = "*")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    /**
     * CREATE - Criar um novo livro
     *
     * POST /api/livros
     * Body: { "isbn": "978-1234567890", "titulo": "Banco de Dados", "anoPublicacao": 2023 }
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> criar(@Valid @RequestBody Livro livro) {
        try {
            Livro livroCriado = livroService.criar(livro);

            Map<String, Object> resposta = new HashMap<>();
            resposta.put("sucesso", true);
            resposta.put("mensagem", "Livro criado com sucesso!");
            resposta.put("livro", livroCriado);

            return ResponseEntity.status(HttpStatus.CREATED).body(resposta);

        } catch (IllegalArgumentException e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("sucesso", false);
            erro.put("mensagem", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
        }
    }

    /**
     * READ - Listar todos os livros
     *
     * GET /api/livros
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> listarTodos() {
        List<Livro> livros = livroService.listarTodos();

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("sucesso", true);
        resposta.put("total", livros.size());
        resposta.put("livros", livros);

        return ResponseEntity.ok(resposta);
    }

    /**
     * READ - Buscar livro por ID
     *
     * GET /api/livros/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> buscarPorId(@PathVariable Integer id) {
        try {
            Livro livro = livroService.buscarPorId(id);

            Map<String, Object> resposta = new HashMap<>();
            resposta.put("sucesso", true);
            resposta.put("livro", livro);

            return ResponseEntity.ok(resposta);

        } catch (IllegalArgumentException e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("sucesso", false);
            erro.put("mensagem", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
        }
    }

    /**
     * READ - Buscar livros por título (busca parcial)
     *
     * GET /api/livros/buscar?titulo=banco
     */
    @GetMapping("/buscar")
    public ResponseEntity<Map<String, Object>> buscarPorTitulo(@RequestParam String titulo) {
        List<Livro> livros = livroService.buscarPorTitulo(titulo);

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("sucesso", true);
        resposta.put("total", livros.size());
        resposta.put("livros", livros);

        return ResponseEntity.ok(resposta);
    }

    /**
     * UPDATE - Atualizar livro
     *
     * PUT /api/livros/{id}
     * Body: { "isbn": "978-1234567890", "titulo": "Banco de Dados Atualizado", ... }
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> atualizar(
            @PathVariable Integer id,
            @Valid @RequestBody Livro livro) {
        try {
            Livro livroAtualizado = livroService.atualizar(id, livro);

            Map<String, Object> resposta = new HashMap<>();
            resposta.put("sucesso", true);
            resposta.put("mensagem", "Livro atualizado com sucesso!");
            resposta.put("livro", livroAtualizado);

            return ResponseEntity.ok(resposta);

        } catch (IllegalArgumentException e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("sucesso", false);
            erro.put("mensagem", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
        }
    }

    /**
     * DELETE - Deletar livro
     *
     * DELETE /api/livros/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletar(@PathVariable Integer id) {
        try {
            livroService.deletar(id);

            Map<String, Object> resposta = new HashMap<>();
            resposta.put("sucesso", true);
            resposta.put("mensagem", "Livro deletado com sucesso!");

            return ResponseEntity.ok(resposta);

        } catch (IllegalArgumentException e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("sucesso", false);
            erro.put("mensagem", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
        }
    }

    /**
     * Obtém total de autores do livro
     * Chama a função fn_contar_autores_livro do banco
     *
     * GET /api/livros/{id}/autores
     */
    @GetMapping("/{id}/autores")
    public ResponseEntity<Map<String, Object>> obterTotalAutores(@PathVariable Integer id) {
        try {
            Integer total = livroService.obterTotalAutores(id);

            Map<String, Object> resposta = new HashMap<>();
            resposta.put("sucesso", true);
            resposta.put("idLivro", id);
            resposta.put("totalAutores", total);

            return ResponseEntity.ok(resposta);

        } catch (IllegalArgumentException e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("sucesso", false);
            erro.put("mensagem", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
        }
    }
}
