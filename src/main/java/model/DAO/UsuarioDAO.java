package main.java.model.DAO;

import main.java.model.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    // CRUD operations

    public void create(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO Usuario (tipo_usuario, nome, data_nascimento, curso) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, usuario.getTipoUsuario());
            stmt.setString(2, usuario.getNome());
            stmt.setDate(3, new java.sql.Date(usuario.getDataNascimento().getTime()));
            stmt.setString(4, usuario.getCurso());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    usuario.setIdUsuario(generatedKeys.getLong(1));
                }
            }
        }
    }

    public List<Usuario> readAll() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM Usuario";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getLong("id_usuario"),
                        rs.getString("tipo_usuario"),
                        rs.getString("nome"),
                        rs.getDate("data_nascimento"),
                        rs.getString("curso")
                );
                usuarios.add(usuario);
            }
        }
        return usuarios;
    }

    public Usuario readById(Long id) throws SQLException {
        String sql = "SELECT * FROM Usuario WHERE id_usuario = ?";
        Usuario usuario = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario(
                            rs.getLong("id_usuario"),
                            rs.getString("tipo_usuario"),
                            rs.getString("nome"),
                            rs.getDate("data_nascimento"),
                            rs.getString("curso")
                    );
                }
            }
        }
        return usuario;
    }

    public void update(Usuario usuario) throws SQLException {
        String sql = "UPDATE Usuario SET tipo_usuario = ?, nome = ?, data_nascimento = ?, curso = ? WHERE id_usuario = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getTipoUsuario());
            stmt.setString(2, usuario.getNome());
            stmt.setDate(3, new java.sql.Date(usuario.getDataNascimento().getTime()));
            stmt.setString(4, usuario.getCurso());
            stmt.setLong(5, usuario.getIdUsuario());

            stmt.executeUpdate();
        }
    }

    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM Usuario WHERE id_usuario = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}