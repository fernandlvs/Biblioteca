package com.biblioteca.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Serviço de Empréstimo - gerencia operações de empréstimo
 *
 * Contém a lógica para registrar devoluções chamando
 * a procedure do banco de dados
 */
@Service
public class EmprestimoService {

    private final JdbcTemplate jdbcTemplate;

    public EmprestimoService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Registra a devolução de um empréstimo
     *
     * Chama a procedure prc_registrar_devolucao do banco que:
     * - Atualiza a data de devolução
     * - Atualiza o status do exemplar para 'disponível'
     * - Calcula e registra multa se houver atraso
     * - Registra auditoria da devolução
     *
     * @param idEmprestimo ID do empréstimo a ser devolvido
     * @param dataDevolucao data da devolução (opcional, usa hoje se null)
     * @return mapa com informações do resultado
     * @throws RuntimeException se houver erro na devolução
     */
    public Map<String, Object> registrarDevolucao(Integer idEmprestimo, LocalDate dataDevolucao) {
        try {
            // Se data não informada, usa data atual
            if (dataDevolucao == null) {
                dataDevolucao = LocalDate.now();
            }

            // Chama a procedure do banco
            String sql = "CALL prc_registrar_devolucao(?, ?)";
            jdbcTemplate.update(sql, idEmprestimo, Date.valueOf(dataDevolucao));

            // Verifica se foi gerada multa
            String sqlMulta = "SELECT valor FROM multa WHERE id_emprestimo = ? ORDER BY id_multa DESC LIMIT 1";
            Double valorMulta = null;

            try {
                valorMulta = jdbcTemplate.queryForObject(sqlMulta, Double.class, idEmprestimo);
            } catch (Exception e) {
                // Não tem multa (devolução sem atraso)
            }

            // Monta resposta
            Map<String, Object> resultado = new HashMap<>();
            resultado.put("sucesso", true);
            resultado.put("mensagem", "Devolução registrada com sucesso!");
            resultado.put("idEmprestimo", idEmprestimo);
            resultado.put("dataDevolucao", dataDevolucao);
            resultado.put("multaGerada", valorMulta != null);

            if (valorMulta != null) {
                resultado.put("valorMulta", valorMulta);
                resultado.put("mensagemMulta", "Multa de R$ " + String.format("%.2f", valorMulta) + " gerada por atraso.");
            } else {
                resultado.put("valorMulta", 0.0);
                resultado.put("mensagemMulta", "Devolução sem atraso. Nenhuma multa gerada.");
            }

            return resultado;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao registrar devolução: " + e.getMessage(), e);
        }
    }

    /**
     * Lista todos os empréstimos ativos
     *
     * @return lista de empréstimos que ainda não foram devolvidos
     */
    public Map<String, Object> listarEmprestimosAtivos() {
        String sql = "SELECT * FROM vw_emprestimos_ativos WHERE status = 'pendente'";

        var emprestimos = jdbcTemplate.queryForList(sql);

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("total", emprestimos.size());
        resultado.put("emprestimos", emprestimos);

        return resultado;
    }

    /**
     * Busca informações detalhadas de um empréstimo
     *
     * @param idEmprestimo ID do empréstimo
     * @return mapa com dados do empréstimo
     */
    public Map<String, Object> buscarEmprestimo(Integer idEmprestimo) {
        String sql = """
            SELECT 
                e.id_emprestimo,
                u.nome as usuario,
                l.titulo as livro,
                e.data_emprestimo,
                e.data_prevista_devolucao,
                e.data_devolucao_real,
                CASE 
                    WHEN e.data_devolucao_real IS NULL THEN 'ATIVO'
                    ELSE 'DEVOLVIDO'
                END as status
            FROM emprestimo e
            JOIN usuario u ON e.id_usuario = u.id_usuario
            JOIN exemplar ex ON e.id_exemplar = ex.id_exemplar
            JOIN livro l ON ex.id_livro = l.id_livro
            WHERE e.id_emprestimo = ?
        """;

        var emprestimo = jdbcTemplate.queryForMap(sql, idEmprestimo);

        // Verifica se tem multa
        String sqlMulta = "SELECT valor, data_pagamento FROM multa WHERE id_emprestimo = ?";
        try {
            var multa = jdbcTemplate.queryForMap(sqlMulta, idEmprestimo);
            emprestimo.put("multa", multa);
        } catch (Exception e) {
            emprestimo.put("multa", null);
        }

        return emprestimo;
    }
}
