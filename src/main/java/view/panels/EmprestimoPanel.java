package main.java.view.panels;

import main.java.controller.EmprestimoController;
import main.java.controller.ObraController;
import main.java.controller.UsuarioController;
import main.java.model.Emprestimo;
import main.java.model.Obra;
import main.java.model.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EmprestimoPanel extends JPanel {
    private MainPanel mainPanel;
    private EmprestimoController emprestimoController;
    private UsuarioController usuarioController;
    private ObraController obraController;
    private JTable tabelaEmprestimos;
    private DefaultTableModel tableModel;

    public EmprestimoPanel(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
        this.emprestimoController = new EmprestimoController();
        this.usuarioController = new UsuarioController();
        this.obraController = new ObraController();
        initComponents();
        carregarDados();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Painel de tabela
        tableModel = new DefaultTableModel(new Object[]{"ID", "Usuário", "Obra", "Data Empréstimo", "Prev. Devolução", "Real Devolução"}, 0);
        tabelaEmprestimos = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabelaEmprestimos);
        add(scrollPane, BorderLayout.CENTER);

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton btnNovo = new JButton("Novo Empréstimo");
        JButton btnDevolver = new JButton("Registrar Devolução");
        JButton btnRemover = new JButton("Remover");
        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnVoltar = new JButton("Voltar ao Menu");

        buttonPanel.add(btnNovo);
        buttonPanel.add(btnDevolver);
        buttonPanel.add(btnRemover);
        buttonPanel.add(btnAtualizar);
        buttonPanel.add(btnVoltar);

        add(buttonPanel, BorderLayout.SOUTH);

        // Listeners
        btnNovo.addActionListener(e -> novoEmprestimo());
        btnDevolver.addActionListener(e -> registrarDevolucao());
        btnRemover.addActionListener(e -> removerEmprestimo());
        btnAtualizar.addActionListener(e -> carregarDados());
        btnVoltar.addActionListener(e -> mainPanel.showPanel("MENU"));
    }

    private void carregarDados() {
        tableModel.setRowCount(0);
        try {
            List<Emprestimo> emprestimos = emprestimoController.listarTodosEmprestimos();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            for (Emprestimo emp : emprestimos) {
                Usuario usuario = usuarioController.buscarUsuarioPorId(emp.getIdUsuario());
                Obra obra = obraController.buscarObraPorId(emp.getIdObra());

                tableModel.addRow(new Object[]{
                        emp.getIdEmprestimo(),
                        usuario != null ? usuario.getNome() : "N/A",
                        obra != null ? obra.getTitulo() : "N/A",
                        sdf.format(emp.getDataEmprestimo()),
                        sdf.format(emp.getDataPrevDevolucao()),
                        emp.getDataRealDevolucao() != null ? sdf.format(emp.getDataRealDevolucao()) : "Pendente"
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar empréstimos: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void novoEmprestimo() {
        JPanel panel = new JPanel(new GridLayout(4, 2));

        // Campos de seleção
        JComboBox<Usuario> cbUsuario = new JComboBox<>();
        JComboBox<Obra> cbObra = new JComboBox<>();
        JTextField txtDataEmp = new JTextField(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        JTextField txtDataPrev = new JTextField();

        try {
            // Carregar usuários e obras
            for (Usuario u : usuarioController.listarUsuarios()) {
                cbUsuario.addItem(u);
            }
            for (Obra o : obraController.ListarObras()) {
                cbObra.addItem(o);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        panel.add(new JLabel("Usuário:"));
        panel.add(cbUsuario);
        panel.add(new JLabel("Obra:"));
        panel.add(cbObra);
        panel.add(new JLabel("Data Empréstimo (dd/MM/yyyy):"));
        panel.add(txtDataEmp);
        panel.add(new JLabel("Data Prev. Devolução (dd/MM/yyyy):"));
        panel.add(txtDataPrev);

        int result = JOptionPane.showConfirmDialog(this, panel, "Novo Empréstimo",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date dataEmp = sdf.parse(txtDataEmp.getText());
                Date dataPrev = sdf.parse(txtDataPrev.getText());

                Usuario usuario = (Usuario) cbUsuario.getSelectedItem();
                Obra obra = (Obra) cbObra.getSelectedItem();

                if (usuario != null && obra != null) {
                    emprestimoController.realizarEmprestimo(
                            usuario.getIdUsuario(),
                            obra.getIdObra(),
                            dataEmp,
                            dataPrev
                    );

                    carregarDados();
                    JOptionPane.showMessageDialog(this, "Empréstimo realizado com sucesso!");
                }
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Formato de data inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao realizar empréstimo: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void registrarDevolucao() {
        int selectedRow = tabelaEmprestimos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um empréstimo para registrar devolução!",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Long id = (Long) tableModel.getValueAt(selectedRow, 0);
        String devolucaoAtual = (String) tableModel.getValueAt(selectedRow, 5);

        if (!"Pendente".equals(devolucaoAtual)) {
            JOptionPane.showMessageDialog(this, "Este empréstimo já foi devolvido!",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JPanel panel = new JPanel(new GridLayout(1, 2));
        JTextField txtDataDevolucao = new JTextField(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

        panel.add(new JLabel("Data Devolução (dd/MM/yyyy):"));
        panel.add(txtDataDevolucao);

        int result = JOptionPane.showConfirmDialog(this, panel, "Registrar Devolução",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date dataDevolucao = sdf.parse(txtDataDevolucao.getText());

                emprestimoController.registrarDevolucao(id, dataDevolucao);
                carregarDados();
                JOptionPane.showMessageDialog(this, "Devolução registrada com sucesso!");
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Formato de data inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao registrar devolução: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void removerEmprestimo() {
        int selectedRow = tabelaEmprestimos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um empréstimo para remover!",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Long id = (Long) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja remover este empréstimo?", "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                emprestimoController.removerEmprestimo(id);
                carregarDados();
                JOptionPane.showMessageDialog(this, "Empréstimo removido com sucesso!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao remover empréstimo: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}