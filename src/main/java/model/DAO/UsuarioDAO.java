package main.java.model.DAO;

import main.java.model.Usuario;
import main.java.model.Endereco;
import main.java.model.Telefone;
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

                    // Salvar telefones e endereços
                    saveTelefones(usuario, conn);
                    saveEnderecos(usuario, conn);
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

                // Carregar telefones e endereços
                usuario.setTelefones(loadTelefones(usuario.getIdUsuario(), conn));
                usuario.setEnderecos(loadEnderecos(usuario.getIdUsuario(), conn));

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

                    // Carregar telefones e endereços
                    usuario.setTelefones(loadTelefones(id, conn));
                    usuario.setEnderecos(loadEnderecos(id, conn));
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

            // Atualizar telefones e endereços
            updateTelefones(usuario, conn);
            updateEnderecos(usuario, conn);
        }
    }

    public void delete(Long id) throws SQLException {
        // Primeiro deletar telefones e endereços associados
        deleteTelefones(id);
        deleteEnderecos(id);

        // Depois deletar o usuário
        String sql = "DELETE FROM Usuario WHERE id_usuario = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    // Métodos auxiliares para telefones
    private List<Telefone> loadTelefones(Long idUsuario, Connection conn) throws SQLException {
        List<Telefone> telefones = new ArrayList<>();
        String sql = "SELECT * FROM Telefone WHERE id_usuario = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    telefones.add(new Telefone(
                            rs.getLong("id_telefone"),
                            rs.getLong("id_usuario"),
                            rs.getString("tipo_telefone"),
                            rs.getString("numero")
                    ));
                }
            }
        }
        return telefones;
    }

    private void saveTelefones(Usuario usuario, Connection conn) throws SQLException {
        String sql = "INSERT INTO Telefone (id_usuario, tipo_telefone, numero) VALUES (?, ?, ?)";

        for (Telefone telefone : usuario.getTelefones()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setLong(1, usuario.getIdUsuario());
                stmt.setString(2, telefone.getTipoTelefone());
                stmt.setString(3, telefone.getNumero());

                stmt.executeUpdate();

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        telefone.setIdTelefone(generatedKeys.getLong(1));
                    }
                }
            }
        }
    }

    private void updateTelefones(Usuario usuario, Connection conn) throws SQLException {
        // Primeiro deletar todos os telefones existentes
        deleteTelefones(usuario.getIdUsuario(), conn);

        // Depois inserir os novos
        saveTelefones(usuario, conn);
    }

    private void deleteTelefones(Long idUsuario) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            deleteTelefones(idUsuario, conn);
        }
    }

    private void deleteTelefones(Long idUsuario, Connection conn) throws SQLException {
        String sql = "DELETE FROM Telefone WHERE id_usuario = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idUsuario);
            stmt.executeUpdate();
        }
    }

    // Métodos auxiliares para endereços (similares aos de telefones)
    private List<Endereco> loadEnderecos(Long idUsuario, Connection conn) throws SQLException {
        List<Endereco> enderecos = new ArrayList<>();
        String sql = "SELECT * FROM Endereco WHERE id_usuario = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    enderecos.add(new Endereco(
                            rs.getLong("id_endereco"),
                            rs.getLong("id_usuario"),
                            rs.getString("rua"),
                            rs.getString("numero"),
                            rs.getString("bairro"),
                            rs.getString("cidade"),
                            rs.getString("estado"),
                            rs.getString("cep"),
                            rs.getString("complemento")
                    ));
                }
            }
        }
        return enderecos;
    }

    private void saveEnderecos(Usuario usuario, Connection conn) throws SQLException {
        String sql = "INSERT INTO Endereco (id_usuario, rua, numero, bairro, cidade, estado, cep, complemento) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        for (Endereco endereco : usuario.getEnderecos()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setLong(1, usuario.getIdUsuario());
                stmt.setString(2, endereco.getRua());
                stmt.setString(3, endereco.getNumero());
                stmt.setString(4, endereco.getBairro());
                stmt.setString(5, endereco.getCidade());
                stmt.setString(6, endereco.getEstado());
                stmt.setString(7, endereco.getCep());
                stmt.setString(8, endereco.getComplemento());

                stmt.executeUpdate();

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        endereco.setIdEndereco(generatedKeys.getLong(1));
                    }
                }
            }
        }
    }

    private void updateEnderecos(Usuario usuario, Connection conn) throws SQLException {
        // Primeiro deletar todos os endereços existentes
        deleteEnderecos(usuario.getIdUsuario(), conn);

        // Depois inserir os novos
        saveEnderecos(usuario, conn);
    }

    private void deleteEnderecos(Long idUsuario) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            deleteEnderecos(idUsuario, conn);
        }
    }

    private void deleteEnderecos(Long idUsuario, Connection conn) throws SQLException {
        String sql = "DELETE FROM Endereco WHERE id_usuario = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, idUsuario);
            stmt.executeUpdate();
        }
    }
}