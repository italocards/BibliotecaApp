package main.java.controller;

import main.java.model.Usuario;
import main.java.model.Endereco;
import main.java.model.Telefone;
import main.java.model.DAO.UsuarioDAO;
import java.util.List;
import java.sql.SQLException;
import java.util.Date;

public class UsuarioController {
    private UsuarioDAO usuarioDAO;

    public UsuarioController() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public void cadastrarUsuario(String tipoUsuario, String nome, Date dataNascimento, String curso,
                                 List<Telefone> telefones, List<Endereco> enderecos) throws SQLException {
        Usuario usuario = new Usuario(null, tipoUsuario, nome, dataNascimento, curso);
        usuario.setTelefones(telefones);
        usuario.setEnderecos(enderecos);
        usuarioDAO.create(usuario);
    }

    public List<Usuario> listarUsuarios() throws SQLException {
        return usuarioDAO.readAll();
    }

    public Usuario buscarUsuarioPorId(Long id) throws SQLException {
        return usuarioDAO.readById(id);
    }

    public void atualizarUsuario(Long id, String tipoUsuario, String nome, Date dataNascimento, String curso,
                                 List<Telefone> telefones, List<Endereco> enderecos) throws SQLException {
        Usuario usuario = new Usuario(id, tipoUsuario, nome, dataNascimento, curso);
        usuario.setTelefones(telefones);
        usuario.setEnderecos(enderecos);
        usuarioDAO.update(usuario);
    }

    public void removerUsuario(Long id) throws SQLException {
        usuarioDAO.delete(id);
    }
}