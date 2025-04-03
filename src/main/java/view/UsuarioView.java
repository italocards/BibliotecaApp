package main.java.view;

import main.java.controller.UsuarioController;
import main.java.model.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UsuarioView extends JFrame {
    private UsuarioController controller;
    private JTable tabelaUsuarios;
    private DefaultTableModel tableModel;

    public UsuarioView() {
        this.controller = new UsuarioController();
        initComponents();
        carregarDados();
    }

    private void initComponents() {
        setTitle("Gerenciamento de Usuários");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Painel principal
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Painel de tabela
        tableModel = new DefaultTableModel(new Object[]{"ID", "Tipo", "Nome", "Data Nasc.", "Curso"}, 0);
        tabelaUsuarios = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabelaUsuarios);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnEditar = new JButton("Editar");
        JButton btnRemover = new JButton("Remover");
        JButton btnAtualizar = new JButton("Atualizar");

        buttonPanel.add(btnAdicionar);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnRemover);
        buttonPanel.add(btnAtualizar);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Listeners
        btnAdicionar.addActionListener(e -> adicionarUsuario());
        btnEditar.addActionListener(e -> editarUsuario());
        btnRemover.addActionListener(e -> removerUsuario());
        btnAtualizar.addActionListener(e -> carregarDados());

        add(mainPanel);
    }

    private void carregarDados() {
        tableModel.setRowCount(0);
        try {
            List<Usuario> usuarios = controller.listarUsuarios();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            for (Usuario usuario : usuarios) {
                tableModel.addRow(new Object[]{
                        usuario.getIdUsuario(),
                        usuario.getTipoUsuario(),
                        usuario.getNome(),
                        sdf.format(usuario.getDataNascimento()),
                        usuario.getCurso()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar usuários: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void adicionarUsuario() {
        JPanel panel = new JPanel(new GridLayout(5, 2));
        JTextField txtTipo = new JTextField();
        JTextField txtNome = new JTextField();
        JTextField txtDataNasc = new JTextField();
        JTextField txtCurso = new JTextField();

        panel.add(new JLabel("Tipo (Aluno/Professor):"));
        panel.add(txtTipo);
        panel.add(new JLabel("Nome:"));
        panel.add(txtNome);
        panel.add(new JLabel("Data Nasc. (dd/MM/yyyy):"));
        panel.add(txtDataNasc);
        panel.add(new JLabel("Curso (opcional para professores):"));
        panel.add(txtCurso);

        int result = JOptionPane.showConfirmDialog(this, panel, "Adicionar Usuário",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date dataNasc = sdf.parse(txtDataNasc.getText());

                controller.cadastrarUsuario(
                        txtTipo.getText(),
                        txtNome.getText(),
                        dataNasc,
                        txtCurso.getText()
                );

                carregarDados();
                JOptionPane.showMessageDialog(this, "Usuário adicionado com sucesso!");
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Formato de data inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao adicionar usuário: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarUsuario() {
        int selectedRow = tabelaUsuarios.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para editar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Long id = (Long) tableModel.getValueAt(selectedRow, 0);

        try {
            Usuario usuario = controller.buscarUsuarioPorId(id);
            if (usuario == null) {
                JOptionPane.showMessageDialog(this, "Usuário não encontrado!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JPanel panel = new JPanel(new GridLayout(5, 2));
            JTextField txtTipo = new JTextField(usuario.getTipoUsuario());
            JTextField txtNome = new JTextField(usuario.getNome());

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            JTextField txtDataNasc = new JTextField(sdf.format(usuario.getDataNascimento()));
            JTextField txtCurso = new JTextField(usuario.getCurso());

            panel.add(new JLabel("Tipo (Aluno/Professor):"));
            panel.add(txtTipo);
            panel.add(new JLabel("Nome:"));
            panel.add(txtNome);
            panel.add(new JLabel("Data Nasc. (dd/MM/yyyy):"));
            panel.add(txtDataNasc);
            panel.add(new JLabel("Curso (opcional para professores):"));
            panel.add(txtCurso);

            int result = JOptionPane.showConfirmDialog(this, panel, "Editar Usuário",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    Date dataNasc = sdf.parse(txtDataNasc.getText());

                    controller.atualizarUsuario(
                            usuario.getIdUsuario(),
                            txtTipo.getText(),
                            txtNome.getText(),
                            dataNasc,
                            txtCurso.getText()
                    );

                    carregarDados();
                    JOptionPane.showMessageDialog(this, "Usuário atualizado com sucesso!");
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(this, "Formato de data inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao atualizar usuário: " + ex.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar usuário: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerUsuario() {
        int selectedRow = tabelaUsuarios.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para remover!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Long id = (Long) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja remover este usuário?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                controller.removerUsuario(id);
                carregarDados();
                JOptionPane.showMessageDialog(this, "Usuário removido com sucesso!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao remover usuário: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UsuarioView view = new UsuarioView();
            view.setVisible(true);
        });
    }
}