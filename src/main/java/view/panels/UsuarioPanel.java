package main.java.view.panels;

import main.java.controller.UsuarioController;
import main.java.model.Usuario;
import main.java.model.Endereco;
import main.java.model.Telefone;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UsuarioPanel extends JPanel {
    private MainPanel mainPanel;
    private UsuarioController controller;
    private JTable tabelaUsuarios;
    private DefaultTableModel tableModel;

    public UsuarioPanel(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
        this.controller = new UsuarioController();
        initComponents();
        carregarDados();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Painel de tabela
        tableModel = new DefaultTableModel(new Object[]{"ID", "Tipo", "Nome", "Data Nasc.", "Curso", "Telefones", "Endereços"}, 0);
        tabelaUsuarios = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabelaUsuarios);
        add(scrollPane, BorderLayout.CENTER);

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnEditar = new JButton("Editar");
        JButton btnRemover = new JButton("Remover");
        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnVoltar = new JButton("Voltar ao Menu");

        buttonPanel.add(btnAdicionar);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnRemover);
        buttonPanel.add(btnAtualizar);
        buttonPanel.add(btnVoltar);

        add(buttonPanel, BorderLayout.SOUTH);

        // Listeners
        btnAdicionar.addActionListener(e -> adicionarUsuario());
        btnEditar.addActionListener(e -> editarUsuario());
        btnRemover.addActionListener(e -> removerUsuario());
        btnAtualizar.addActionListener(e -> carregarDados());
        btnVoltar.addActionListener(e -> mainPanel.showPanel("MENU"));
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
                        usuario.getCurso(),
                        formatTelefones(usuario.getTelefones()),
                        formatEnderecos(usuario.getEnderecos())
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar usuários: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String formatTelefones(List<Telefone> telefones) {
        if (telefones == null || telefones.isEmpty()) return "Nenhum";

        StringBuilder sb = new StringBuilder();
        for (Telefone t : telefones) {
            sb.append(t.getTipoTelefone()).append(": ").append(t.getNumero()).append("; ");
        }
        return sb.toString();
    }

    private String formatEnderecos(List<Endereco> enderecos) {
        if (enderecos == null || enderecos.isEmpty()) return "Nenhum";

        StringBuilder sb = new StringBuilder();
        for (Endereco e : enderecos) {
            sb.append(e.getCidade()).append("/").append(e.getEstado()).append("; ");
        }
        return sb.toString();
    }

    private void adicionarUsuario() {
        JTabbedPane tabbedPane = new JTabbedPane();

        // Painel de informações básicas
        JPanel infoPanel = new JPanel(new GridLayout(5, 2));
        JTextField txtTipo = new JTextField();
        JTextField txtNome = new JTextField();
        JTextField txtDataNasc = new JTextField();
        JTextField txtCurso = new JTextField();

        infoPanel.add(new JLabel("Tipo (Aluno/Professor):"));
        infoPanel.add(txtTipo);
        infoPanel.add(new JLabel("Nome:"));
        infoPanel.add(txtNome);
        infoPanel.add(new JLabel("Data Nasc. (dd/MM/yyyy):"));
        infoPanel.add(txtDataNasc);
        infoPanel.add(new JLabel("Curso (opcional para professores):"));
        infoPanel.add(txtCurso);

        tabbedPane.addTab("Informações Básicas", infoPanel);

        // Painel de telefones
        DefaultTableModel telefoneModel = new DefaultTableModel(new Object[]{"Tipo", "Número"}, 0);
        JTable telefoneTable = new JTable(telefoneModel);
        JPanel telefonePanel = new JPanel(new BorderLayout());
        telefonePanel.add(new JScrollPane(telefoneTable), BorderLayout.CENTER);

        JPanel telefoneButtonPanel = new JPanel();
        JButton btnAddTelefone = new JButton("Adicionar");
        JButton btnRemTelefone = new JButton("Remover");
        telefoneButtonPanel.add(btnAddTelefone);
        telefoneButtonPanel.add(btnRemTelefone);
        telefonePanel.add(telefoneButtonPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Telefones", telefonePanel);

        // Painel de endereços
        DefaultTableModel enderecoModel = new DefaultTableModel(new Object[]{"Rua", "Número", "Cidade", "Estado"}, 0);
        JTable enderecoTable = new JTable(enderecoModel);
        JPanel enderecoPanel = new JPanel(new BorderLayout());
        enderecoPanel.add(new JScrollPane(enderecoTable), BorderLayout.CENTER);

        JPanel enderecoButtonPanel = new JPanel();
        JButton btnAddEndereco = new JButton("Adicionar");
        JButton btnRemEndereco = new JButton("Remover");
        enderecoButtonPanel.add(btnAddEndereco);
        enderecoButtonPanel.add(btnRemEndereco);
        enderecoPanel.add(enderecoButtonPanel, BorderLayout.SOUTH);

        tabbedPane.addTab("Endereços", enderecoPanel);

        // Listeners para telefones
        btnAddTelefone.addActionListener(e -> {
            JPanel panel = new JPanel(new GridLayout(2, 2));
            JTextField txtTipoTel = new JTextField();
            JTextField txtNumero = new JTextField();

            panel.add(new JLabel("Tipo (Celular/Fixo):"));
            panel.add(txtTipoTel);
            panel.add(new JLabel("Número:"));
            panel.add(txtNumero);

            int result = JOptionPane.showConfirmDialog(this, panel, "Adicionar Telefone",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                telefoneModel.addRow(new Object[]{txtTipoTel.getText(), txtNumero.getText()});
            }
        });

        btnRemTelefone.addActionListener(e -> {
            int row = telefoneTable.getSelectedRow();
            if (row >= 0) {
                telefoneModel.removeRow(row);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um telefone para remover!", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Listeners para endereços
        btnAddEndereco.addActionListener(e -> {
            JPanel panel = new JPanel(new GridLayout(8, 2));
            JTextField txtRua = new JTextField();
            JTextField txtNumero = new JTextField();
            JTextField txtBairro = new JTextField();
            JTextField txtCidade = new JTextField();
            JTextField txtEstado = new JTextField();
            JTextField txtCep = new JTextField();
            JTextField txtComplemento = new JTextField();

            panel.add(new JLabel("Rua:"));
            panel.add(txtRua);
            panel.add(new JLabel("Número:"));
            panel.add(txtNumero);
            panel.add(new JLabel("Bairro:"));
            panel.add(txtBairro);
            panel.add(new JLabel("Cidade:"));
            panel.add(txtCidade);
            panel.add(new JLabel("Estado:"));
            panel.add(txtEstado);
            panel.add(new JLabel("CEP:"));
            panel.add(txtCep);
            panel.add(new JLabel("Complemento:"));
            panel.add(txtComplemento);

            int result = JOptionPane.showConfirmDialog(this, panel, "Adicionar Endereço",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                enderecoModel.addRow(new Object[]{
                        txtRua.getText(),
                        txtNumero.getText(),
                        txtCidade.getText(),
                        txtEstado.getText()
                });
            }
        });

        btnRemEndereco.addActionListener(e -> {
            int row = enderecoTable.getSelectedRow();
            if (row >= 0) {
                enderecoModel.removeRow(row);
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um endereço para remover!", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        int result = JOptionPane.showConfirmDialog(this, tabbedPane, "Adicionar Usuário",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date dataNasc = sdf.parse(txtDataNasc.getText());

                // Preparar lista de telefones
                List<Telefone> telefones = new ArrayList<>();
                for (int i = 0; i < telefoneModel.getRowCount(); i++) {
                    telefones.add(new Telefone(
                            null,
                            null,
                            (String) telefoneModel.getValueAt(i, 0),
                            (String) telefoneModel.getValueAt(i, 1)
                    ));
                }

                // Preparar lista de endereços
                List<Endereco> enderecos = new ArrayList<>();
                for (int i = 0; i < enderecoModel.getRowCount(); i++) {
                    enderecos.add(new Endereco(
                            null,
                            null,
                            (String) enderecoModel.getValueAt(i, 0),
                            (String) enderecoModel.getValueAt(i, 1),
                            "", // bairro - ajustar conforme necessário
                            (String) enderecoModel.getValueAt(i, 2),
                            (String) enderecoModel.getValueAt(i, 3),
                            "", // cep - ajustar conforme necessário
                            ""  // complemento - ajustar conforme necessário
                    ));
                }

                controller.cadastrarUsuario(
                        txtTipo.getText(),
                        txtNome.getText(),
                        dataNasc,
                        txtCurso.getText(),
                        telefones,
                        enderecos
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

            JTabbedPane tabbedPane = new JTabbedPane();

            // Painel de informações básicas
            JPanel infoPanel = new JPanel(new GridLayout(5, 2));
            JTextField txtTipo = new JTextField(usuario.getTipoUsuario());
            JTextField txtNome = new JTextField(usuario.getNome());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            JTextField txtDataNasc = new JTextField(sdf.format(usuario.getDataNascimento()));
            JTextField txtCurso = new JTextField(usuario.getCurso());

            infoPanel.add(new JLabel("Tipo (Aluno/Professor):"));
            infoPanel.add(txtTipo);
            infoPanel.add(new JLabel("Nome:"));
            infoPanel.add(txtNome);
            infoPanel.add(new JLabel("Data Nasc. (dd/MM/yyyy):"));
            infoPanel.add(txtDataNasc);
            infoPanel.add(new JLabel("Curso (opcional para professores):"));
            infoPanel.add(txtCurso);

            tabbedPane.addTab("Informações Básicas", infoPanel);

            // Painel de telefones
            DefaultTableModel telefoneModel = new DefaultTableModel(new Object[]{"Tipo", "Número"}, 0);
            JTable telefoneTable = new JTable(telefoneModel);
            JPanel telefonePanel = new JPanel(new BorderLayout());
            telefonePanel.add(new JScrollPane(telefoneTable), BorderLayout.CENTER);

            JPanel telefoneButtonPanel = new JPanel();
            JButton btnAddTelefone = new JButton("Adicionar");
            JButton btnRemTelefone = new JButton("Remover");
            telefoneButtonPanel.add(btnAddTelefone);
            telefoneButtonPanel.add(btnRemTelefone);
            telefonePanel.add(telefoneButtonPanel, BorderLayout.SOUTH);

            tabbedPane.addTab("Telefones", telefonePanel);

            // Carregar telefones existentes
            if (usuario.getTelefones() != null) {
                for (Telefone t : usuario.getTelefones()) {
                    telefoneModel.addRow(new Object[]{t.getTipoTelefone(), t.getNumero()});
                }
            }

            // Painel de endereços
            DefaultTableModel enderecoModel = new DefaultTableModel(new Object[]{"Rua", "Número", "Cidade", "Estado"}, 0);
            JTable enderecoTable = new JTable(enderecoModel);
            JPanel enderecoPanel = new JPanel(new BorderLayout());
            enderecoPanel.add(new JScrollPane(enderecoTable), BorderLayout.CENTER);

            JPanel enderecoButtonPanel = new JPanel();
            JButton btnAddEndereco = new JButton("Adicionar");
            JButton btnRemEndereco = new JButton("Remover");
            enderecoButtonPanel.add(btnAddEndereco);
            enderecoButtonPanel.add(btnRemEndereco);
            enderecoPanel.add(enderecoButtonPanel, BorderLayout.SOUTH);

            tabbedPane.addTab("Endereços", enderecoPanel);

            // Carregar endereços existentes
            if (usuario.getEnderecos() != null) {
                for (Endereco e : usuario.getEnderecos()) {
                    enderecoModel.addRow(new Object[]{e.getRua(), e.getNumero(), e.getCidade(), e.getEstado()});
                }
            }

            // Listeners para telefones
            btnAddTelefone.addActionListener(e -> {
                JPanel panel = new JPanel(new GridLayout(2, 2));
                JTextField txtTipoTel = new JTextField();
                JTextField txtNumero = new JTextField();

                panel.add(new JLabel("Tipo (Celular/Fixo):"));
                panel.add(txtTipoTel);
                panel.add(new JLabel("Número:"));
                panel.add(txtNumero);

                int result = JOptionPane.showConfirmDialog(this, panel, "Adicionar Telefone",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    telefoneModel.addRow(new Object[]{txtTipoTel.getText(), txtNumero.getText()});
                }
            });

            btnRemTelefone.addActionListener(e -> {
                int row = telefoneTable.getSelectedRow();
                if (row >= 0) {
                    telefoneModel.removeRow(row);
                } else {
                    JOptionPane.showMessageDialog(this, "Selecione um telefone para remover!", "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            });

            // Listeners para endereços
            btnAddEndereco.addActionListener(e -> {
                JPanel panel = new JPanel(new GridLayout(8, 2));
                JTextField txtRua = new JTextField();
                JTextField txtNumero = new JTextField();
                JTextField txtBairro = new JTextField();
                JTextField txtCidade = new JTextField();
                JTextField txtEstado = new JTextField();
                JTextField txtCep = new JTextField();
                JTextField txtComplemento = new JTextField();

                panel.add(new JLabel("Rua:"));
                panel.add(txtRua);
                panel.add(new JLabel("Número:"));
                panel.add(txtNumero);
                panel.add(new JLabel("Bairro:"));
                panel.add(txtBairro);
                panel.add(new JLabel("Cidade:"));
                panel.add(txtCidade);
                panel.add(new JLabel("Estado:"));
                panel.add(txtEstado);
                panel.add(new JLabel("CEP:"));
                panel.add(txtCep);
                panel.add(new JLabel("Complemento:"));
                panel.add(txtComplemento);

                int result = JOptionPane.showConfirmDialog(this, panel, "Adicionar Endereço",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    enderecoModel.addRow(new Object[]{
                            txtRua.getText(),
                            txtNumero.getText(),
                            txtCidade.getText(),
                            txtEstado.getText()
                    });
                }
            });

            btnRemEndereco.addActionListener(e -> {
                int row = enderecoTable.getSelectedRow();
                if (row >= 0) {
                    enderecoModel.removeRow(row);
                } else {
                    JOptionPane.showMessageDialog(this, "Selecione um endereço para remover!", "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            });

            int result = JOptionPane.showConfirmDialog(this, tabbedPane, "Editar Usuário",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    Date dataNasc = sdf.parse(txtDataNasc.getText());

                    // Preparar lista de telefones
                    List<Telefone> telefones = new ArrayList<>();
                    for (int i = 0; i < telefoneModel.getRowCount(); i++) {
                        telefones.add(new Telefone(
                                null,
                                usuario.getIdUsuario(),
                                (String) telefoneModel.getValueAt(i, 0),
                                (String) telefoneModel.getValueAt(i, 1)
                        ));
                    }

                    // Preparar lista de endereços
                    List<Endereco> enderecos = new ArrayList<>();
                    for (int i = 0; i < enderecoModel.getRowCount(); i++) {
                        enderecos.add(new Endereco(
                                null,
                                usuario.getIdUsuario(),
                                (String) enderecoModel.getValueAt(i, 0),
                                (String) enderecoModel.getValueAt(i, 1),
                                "", // bairro - ajustar conforme necessário
                                (String) enderecoModel.getValueAt(i, 2),
                                (String) enderecoModel.getValueAt(i, 3),
                                "", // cep - ajustar conforme necessário
                                ""  // complemento - ajustar conforme necessário
                        ));
                    }

                    controller.atualizarUsuario(
                            usuario.getIdUsuario(),
                            txtTipo.getText(),
                            txtNome.getText(),
                            dataNasc,
                            txtCurso.getText(),
                            telefones,
                            enderecos
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
}