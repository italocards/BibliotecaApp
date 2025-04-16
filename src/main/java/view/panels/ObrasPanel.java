package main.java.view.panels;

import main.java.controller.ObraController;
import main.java.model.Obra;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ObrasPanel extends JPanel {
    private MainPanel mainPanel;
    private ObraController controller;
    private JTable tabelaObras;
    private DefaultTableModel tableModel;

    public ObrasPanel(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
        this.controller = new ObraController();
        initComponents();
        carregarDados();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Painel de tabela
        tableModel = new DefaultTableModel(new Object[]{"ID", "Tipo", "Título", "Autor", "ISBN", "Data Public.", "Categoria"}, 0);
        tabelaObras = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabelaObras);
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
        btnAdicionar.addActionListener(e -> adicionarObra());
        btnEditar.addActionListener(e -> editarObra());
        btnRemover.addActionListener(e -> removerObra());
        btnAtualizar.addActionListener(e -> carregarDados());
        btnVoltar.addActionListener(e -> mainPanel.showPanel("MENU"));
    }

    private void carregarDados() {
        tableModel.setRowCount(0);
        try {
            List<Obra> obras = controller.ListarObras();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            for (Obra obra : obras) {
                tableModel.addRow(new Object[]{
                        obra.getIdObra(),
                        obra.getTipoObra(),
                        obra.getTitulo(),
                        obra.getAutor(),
                        obra.getISBN(),
                        sdf.format(obra.getDataPublicacao()),
                        obra.getCategoria()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar obras: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void adicionarObra() {
        JPanel panel = new JPanel(new GridLayout(7, 2));
        JTextField txtTipo = new JTextField();
        JTextField txtTitulo = new JTextField();
        JTextField txtAutor = new JTextField();
        JTextField txtISBN = new JTextField();
        JTextField txtDataPublic = new JTextField();
        JTextField txtCategoria = new JTextField();

        panel.add(new JLabel("Tipo (Livro/Revista/Artigo):"));
        panel.add(txtTipo);
        panel.add(new JLabel("Título:"));
        panel.add(txtTitulo);
        panel.add(new JLabel("Autor:"));
        panel.add(txtAutor);
        panel.add(new JLabel("ISBN:"));
        panel.add(txtISBN);
        panel.add(new JLabel("Data Public. (dd/MM/yyyy):"));
        panel.add(txtDataPublic);
        panel.add(new JLabel("Categoria:"));
        panel.add(txtCategoria);

        int result = JOptionPane.showConfirmDialog(this, panel, "Adicionar Obra",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date dataPublic = sdf.parse(txtDataPublic.getText());

                controller.CadastrarObra(
                        txtTipo.getText(),
                        txtTitulo.getText(),
                        txtAutor.getText(),
                        txtISBN.getText(),
                        dataPublic,
                        txtCategoria.getText()
                );

                carregarDados();
                JOptionPane.showMessageDialog(this, "Obra adicionada com sucesso!");
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Formato de data inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao adicionar obra: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarObra() {
        int selectedRow = tabelaObras.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma obra para editar!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Long id = (Long) tableModel.getValueAt(selectedRow, 0);

        try {
            Obra obra = controller.buscarObraPorId(id);
            if (obra == null) {
                JOptionPane.showMessageDialog(this, "Obra não encontrada!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JPanel panel = new JPanel(new GridLayout(7, 2));
            JTextField txtTipo = new JTextField(obra.getTipoObra());
            JTextField txtTitulo = new JTextField(obra.getTitulo());
            JTextField txtAutor = new JTextField(obra.getAutor());
            JTextField txtISBN = new JTextField(obra.getISBN());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            JTextField txtDataPublic = new JTextField(sdf.format(obra.getDataPublicacao()));
            JTextField txtCategoria = new JTextField(obra.getCategoria());

            panel.add(new JLabel("Tipo (Livro/Revista/Artigo):"));
            panel.add(txtTipo);
            panel.add(new JLabel("Título:"));
            panel.add(txtTitulo);
            panel.add(new JLabel("Autor:"));
            panel.add(txtAutor);
            panel.add(new JLabel("ISBN:"));
            panel.add(txtISBN);
            panel.add(new JLabel("Data Public. (dd/MM/yyyy):"));
            panel.add(txtDataPublic);
            panel.add(new JLabel("Categoria:"));
            panel.add(txtCategoria);

            int result = JOptionPane.showConfirmDialog(this, panel, "Editar Obra",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    Date dataPublic = sdf.parse(txtDataPublic.getText());

                    controller.atualizarObra(
                            obra.getIdObra(),
                            txtTipo.getText(),
                            txtTitulo.getText(),
                            txtAutor.getText(),
                            txtISBN.getText(),
                            dataPublic,
                            txtCategoria.getText()
                    );

                    carregarDados();
                    JOptionPane.showMessageDialog(this, "Obra atualizada com sucesso!");
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(this, "Formato de data inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao atualizar obra: " + ex.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar obra: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerObra() {
        int selectedRow = tabelaObras.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma obra para remover!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Long id = (Long) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja remover esta obra?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                controller.deletarObra(id);
                carregarDados();
                JOptionPane.showMessageDialog(this, "Obra removida com sucesso!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao remover obra: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}