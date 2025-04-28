package main.java.model.DAO;

import main.java.model.Emprestimo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmprestimoDAO {
    public void create(Emprestimo emprestimo) throws SQLException {
        String sql = "INSERT INTO Emprestimo (id_usuario, id_obra, data_emprestimo, data_prevista_devolucao, data_real_devolucao) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, emprestimo.getIdUsuario());
            stmt.setLong(2, emprestimo.getIdObra());
            stmt.setDate(3, new java.sql.Date(emprestimo.getDataEmprestimo().getTime()));
            stmt.setDate(4, new java.sql.Date(emprestimo.getDataPrevDevolucao().getTime()));
            stmt.setDate(5, emprestimo.getDataRealDevolucao() != null ?
                    new java.sql.Date(emprestimo.getDataRealDevolucao().getTime()) : null);

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    emprestimo.setIdEmprestimo(generatedKeys.getLong(1));
                }
            }
        }
    }

    public List<Emprestimo> readAll() throws SQLException {
        List<Emprestimo> emprestimos = new ArrayList<>();
        String sql = "SELECT * FROM Emprestimo";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Emprestimo emp = new Emprestimo(
                        rs.getLong("id_emprestimo"),
                        rs.getLong("id_usuario"),
                        rs.getLong("id_obra"),
                        rs.getDate("data_emprestimo"),
                        rs.getDate("data_prevista_devolucao"),
                        rs.getDate("data_real_devolucao")
                );
                emprestimos.add(emp);
            }
        }
        return emprestimos;
    }

    public Emprestimo readById(Long id) throws SQLException {
        String sql = "SELECT * FROM Emprestimo WHERE id_emprestimo = ?";
        Emprestimo emprestimo = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    emprestimo = new Emprestimo(
                            rs.getLong("id_emprestimo"),
                            rs.getLong("id_usuario"),
                            rs.getLong("id_obra"),
                            rs.getDate("data_emprestimo"),
                            rs.getDate("data_prevista_devolucao"),
                            rs.getDate("data_real_devolucao")
                    );
                }
            }
        }
        return emprestimo;
    }

    public void update(Emprestimo emprestimo) throws SQLException {
        String sql = "UPDATE Emprestimo SET id_usuario = ?, id_obra = ?, data_emprestimo = ?, " +
                "data_prevista_devolucao = ?, data_real_devolucao = ? WHERE id_emprestimo = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, emprestimo.getIdUsuario());
            stmt.setLong(2, emprestimo.getIdObra());
            stmt.setDate(3, new java.sql.Date(emprestimo.getDataEmprestimo().getTime()));
            stmt.setDate(4, new java.sql.Date(emprestimo.getDataPrevDevolucao().getTime()));
            stmt.setDate(5, emprestimo.getDataRealDevolucao() != null ?
                    new java.sql.Date(emprestimo.getDataRealDevolucao().getTime()) : null);
            stmt.setLong(6, emprestimo.getIdEmprestimo());

            stmt.executeUpdate();
        }
    }

    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM Emprestimo WHERE id_emprestimo = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Emprestimo> findActiveByUsuario(Long idUsuario) throws SQLException {
        List<Emprestimo> emprestimos = new ArrayList<>();
        String sql = "SELECT * FROM Emprestimo WHERE id_usuario = ? AND data_real_devolucao IS NULL";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Emprestimo emp = new Emprestimo(
                            rs.getLong("id_emprestimo"),
                            rs.getLong("id_usuario"),
                            rs.getLong("id_obra"),
                            rs.getDate("data_emprestimo"),
                            rs.getDate("data_prevista_devolucao"),
                            rs.getDate("data_real_devolucao")
                    );
                    emprestimos.add(emp);
                }
            }
        }
        return emprestimos;
    }
}