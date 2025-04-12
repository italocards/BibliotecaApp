package main.java.model.DAO;

import main.java.model.Obra;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ObraDAO {

    public void create(Obra obra) throws SQLException {
        String sql = "INSERT INTO Obra (tipo_obra, titulo, autor, ISBN, data_publicacao, categoria) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, obra.getTipoObra());
            stmt.setString(2, obra.getTitulo());
            stmt.setString(3, obra.getAutor());
            stmt.setString(4, obra.getISBN());
            stmt.setDate(5, new java.sql.Date(obra.getDataPublicacao().getTime()));
            stmt.setString(6, obra.getCategoria());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    obra.setIdObra(generatedKeys.getLong(1));
                }
            }
        }
    }

    public List<Obra> readAll() throws SQLException {
        List<Obra> obras = new ArrayList<>();
        String sql = "SELECT * FROM Obra";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Obra obra = new Obra(
                        rs.getLong("id_obra"),
                        rs.getString("tipo_obra"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("ISBN"),
                        rs.getDate("data_publicacao"),
                        rs.getString("categoria")
                );
                obras.add(obra);
            }
        }
        return obras;
    }

    public Obra readById(Long id) throws SQLException {
        String sql = "SELECT * FROM Obra WHERE id_obra = ?";
        Obra obra = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    obra = new Obra(
                            rs.getLong("id_obra"),
                            rs.getString("tipo_obra"),
                            rs.getString("titulo"),
                            rs.getString("autor"),
                            rs.getString("ISBN"),
                            rs.getDate("data_publicacao"),
                            rs.getString("categoria")
                    );
                }
            }
        }
        return obra;
    }

    public void update(Obra obra) throws SQLException {
        String sql = "UPDATE Obra SET tipo_obra = ?, titulo = ?, autor = ?, ISBN = ?, data_publicacao = ?, categoria = ? WHERE id_obra = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, obra.getTipoObra());
            stmt.setString(2, obra.getTitulo());
            stmt.setString(3, obra.getAutor());
            stmt.setString(4, obra.getISBN());
            stmt.setDate(5, new java.sql.Date(obra.getDataPublicacao().getTime()));
            stmt.setString(6, obra.getCategoria());
            stmt.setLong(7, obra.getIdObra());

            stmt.executeUpdate();
        }
    }

    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM Obra WHERE id_obra = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}

