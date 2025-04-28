package main.java.controller;

import main.java.model.Emprestimo;
import main.java.model.DAO.EmprestimoDAO;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class EmprestimoController {
    private EmprestimoDAO emprestimoDAO;

    public EmprestimoController() {
        this.emprestimoDAO = new EmprestimoDAO();
    }

    public void realizarEmprestimo(Long idUsuario, Long idObra, Date dataEmprestimo, Date dataPrevDevolucao) throws SQLException {
        Emprestimo emp = new Emprestimo(null, idUsuario, idObra, dataEmprestimo, dataPrevDevolucao, null);
        emprestimoDAO.create(emp);
    }

    public void registrarDevolucao(Long idEmprestimo, Date dataRealDevolucao) throws SQLException {
        Emprestimo emp = emprestimoDAO.readById(idEmprestimo);
        if (emp != null) {
            emp.setDataRealDevolucao(dataRealDevolucao);
            emprestimoDAO.update(emp);
        }
    }

    public List<Emprestimo> listarTodosEmprestimos() throws SQLException {
        return emprestimoDAO.readAll();
    }

    public List<Emprestimo> listarEmprestimosAtivosPorUsuario(Long idUsuario) throws SQLException {
        return emprestimoDAO.findActiveByUsuario(idUsuario);
    }

    public Emprestimo buscarEmprestimoPorId(Long id) throws SQLException {
        return emprestimoDAO.readById(id);
    }

    public void atualizarEmprestimo(Emprestimo emprestimo) throws SQLException {
        emprestimoDAO.update(emprestimo);
    }

    public void removerEmprestimo(Long id) throws SQLException {
        emprestimoDAO.delete(id);
    }
}