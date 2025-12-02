package com.biblioteca.controller;

import com.biblioteca.service.EmprestimoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller REST para gerenciar Empréstimos
 *
 * Endpoints disponíveis:
 * - POST /emprestimos/{id}/devolver  - Registrar devolução (chama procedure)
 * - GET  /emprestimos/ativos         - Listar empréstimos ativos
 * - GET  /emprestimos/{id}           - Buscar empréstimo por ID
 */
@RestController
@RequestMapping("/emprestimos")
@CrossOrigin(origins = "*")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    public EmprestimoController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    /**
     * Registra a devolução de um empréstimo
     * Chama a procedure prc_registrar_devolucao do banco
     *
     * POST /api/emprestimos/{id}/devolver
     * Body (opcional): { "dataDevolucao": "2025-11-29" }
     *
     * Se não informar data, usa a data atual
     */
    @PostMapping("/{id}/devolver")
    public ResponseEntity<Map<String, Object>> registrarDevolucao(
            @PathVariable Integer id,
            @RequestBody(required = false) Map<String, String> body) {
        try {
            LocalDate dataDevolucao = null;

            // Se informou data no body, converte
            if (body != null && body.containsKey("dataDevolucao")) {
                dataDevolucao = LocalDate.parse(body.get("dataDevolucao"));
            }

            // Chama a procedure através do service
            Map<String, Object> resultado = emprestimoService.registrarDevolucao(id, dataDevolucao);

            return ResponseEntity.ok(resultado);

        } catch (RuntimeException e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("sucesso", false);
            erro.put("mensagem", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
        }
    }

    /**
     * Lista todos os empréstimos ativos
     * Usa a view vw_emprestimos_ativos
     *
     * GET /api/emprestimos/ativos
     */
    @GetMapping("/ativos")
    public ResponseEntity<Map<String, Object>> listarEmprestimosAtivos() {
        try {
            Map<String, Object> resultado = emprestimoService.listarEmprestimosAtivos();
            resultado.put("sucesso", true);
            return ResponseEntity.ok(resultado);

        } catch (Exception e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("sucesso", false);
            erro.put("mensagem", "Erro ao buscar empréstimos ativos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
        }
    }

    /**
     * Busca informações detalhadas de um empréstimo
     *
     * GET /api/emprestimos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> buscarEmprestimo(@PathVariable Integer id) {
        try {
            Map<String, Object> emprestimo = emprestimoService.buscarEmprestimo(id);

            Map<String, Object> resposta = new HashMap<>();
            resposta.put("sucesso", true);
            resposta.put("emprestimo", emprestimo);

            return ResponseEntity.ok(resposta);

        } catch (Exception e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("sucesso", false);
            erro.put("mensagem", "Empréstimo não encontrado: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
        }
    }
}
