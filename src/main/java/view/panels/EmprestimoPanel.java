package main.java.view.panels;

import main.java.controller.EmprestimoController;
import main.java.controller.ObraController;
import main.java.controller.UsuarioController;
import main.java.model.Emprestimo;
import main.java.model.Obra;
import main.java.model.Usuario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
        JButton btnEditar = new JButton("Editar");
        JButton btnDevolver = new JButton("Registrar Devolução");
        JButton btnRemover = new JButton("Remover");
        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnVoltar = new JButton("Voltar ao Menu");

        buttonPanel.add(btnNovo);
        buttonPanel.add(btnEditar);  // Botão novo adicionado aqui
        buttonPanel.add(btnDevolver);
        buttonPanel.add(btnRemover);
        buttonPanel.add(btnAtualizar);
        buttonPanel.add(btnVoltar);

        add(buttonPanel, BorderLayout.SOUTH);

        // Listeners
        btnNovo.addActionListener(e -> novoEmprestimo());
        btnEditar.addActionListener(e -> editarEmprestimo());  // Listener para o novo botão
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

        // Campos de data com máscara
        JFormattedTextField txtDataEmp = criarCampoDataComMascara();
        JFormattedTextField txtDataPrev = criarCampoDataComMascara();

        // Define a data atual como padrão
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        txtDataEmp.setText(sdf.format(new Date()));

        // Calcula 15 dias para devolução prevista
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 15);
        txtDataPrev.setText(sdf.format(cal.getTime()));

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
                JOptionPane.showMessageDialog(this, "Formato de data inválido! Use dd/MM/yyyy", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Erro ao realizar empréstimo: " + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarEmprestimo() {
        int selectedRow = tabelaEmprestimos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um empréstimo para editar!",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Long id = (Long) tableModel.getValueAt(selectedRow, 0);

        try {
            Emprestimo emprestimo = emprestimoController.buscarEmprestimoPorId(id);
            if (emprestimo == null) {
                JOptionPane.showMessageDialog(this, "Empréstimo não encontrado!",
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JPanel panel = new JPanel(new GridLayout(5, 2));  // Mudei para 5 linhas

            // Campos de seleção
            JComboBox<Usuario> cbUsuario = new JComboBox<>();
            JComboBox<Obra> cbObra = new JComboBox<>();

            // Campos de data com máscara
            JFormattedTextField txtDataEmp = criarCampoDataComMascara();
            JFormattedTextField txtDataPrev = criarCampoDataComMascara();
            JFormattedTextField txtDataReal = criarCampoDataComMascara();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            // Preenche os campos com os dados atuais
            try {
                // Carrega usuários e obras
                for (Usuario u : usuarioController.listarUsuarios()) {
                    cbUsuario.addItem(u);
                    if (u.getIdUsuario().equals(emprestimo.getIdUsuario())) {
                        cbUsuario.setSelectedItem(u);
                    }
                }

                for (Obra o : obraController.ListarObras()) {
                    cbObra.addItem(o);
                    if (o.getIdObra().equals(emprestimo.getIdObra())) {
                        cbObra.setSelectedItem(o);
                    }
                }

                // Preenche as datas
                txtDataEmp.setText(sdf.format(emprestimo.getDataEmprestimo()));
                txtDataPrev.setText(sdf.format(emprestimo.getDataPrevDevolucao()));

                // Preenche data real (se existir)
                if (emprestimo.getDataRealDevolucao() != null) {
                    txtDataReal.setText(sdf.format(emprestimo.getDataRealDevolucao()));
                } else {
                    txtDataReal.setText("");
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
            panel.add(new JLabel("Data Real Devolução (dd/MM/yyyy):"));
            panel.add(txtDataReal);

            int result = JOptionPane.showConfirmDialog(this, panel, "Editar Empréstimo",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    Date dataEmp = sdf.parse(txtDataEmp.getText());
                    Date dataPrev = sdf.parse(txtDataPrev.getText());
                    Date dataReal = txtDataReal.getText().isEmpty() ? null : sdf.parse(txtDataReal.getText());

                    Usuario usuario = (Usuario) cbUsuario.getSelectedItem();
                    Obra obra = (Obra) cbObra.getSelectedItem();

                    if (usuario != null && obra != null) {
                        // Atualiza todos os campos incluindo data real de devolução
                        emprestimo.setIdUsuario(usuario.getIdUsuario());
                        emprestimo.setIdObra(obra.getIdObra());
                        emprestimo.setDataEmprestimo(dataEmp);
                        emprestimo.setDataPrevDevolucao(dataPrev);
                        emprestimo.setDataRealDevolucao(dataReal);

                        emprestimoController.atualizarEmprestimo(emprestimo);
                        carregarDados();
                        JOptionPane.showMessageDialog(this, "Empréstimo atualizado com sucesso!");
                    }
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(this, "Formato de data inválido! Use dd/MM/yyyy",
                            "Erro", JOptionPane.ERROR_MESSAGE);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao atualizar empréstimo: " + ex.getMessage(),
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao buscar empréstimo: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método auxiliar para criar campo de data com máscara
    private JFormattedTextField criarCampoDataComMascara() {
        try {
            MaskFormatter mask = new MaskFormatter("##/##/####");
            mask.setPlaceholderCharacter('_');
            JFormattedTextField field = new JFormattedTextField(mask);
            field.setColumns(10);
            return field;
        } catch (ParseException e) {
            // Se der erro na máscara, retorna um campo normal
            return new JFormattedTextField();
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
        JFormattedTextField txtDataDevolucao = criarCampoDataComMascara();

        // Define a data atual como padrão
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        txtDataDevolucao.setText(sdf.format(new Date()));

        panel.add(new JLabel("Data Devolução (dd/MM/yyyy):"));
        panel.add(txtDataDevolucao);

        int result = JOptionPane.showConfirmDialog(this, panel, "Registrar Devolução",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Date dataDevolucao = sdf.parse(txtDataDevolucao.getText());

                emprestimoController.registrarDevolucao(id, dataDevolucao);
                carregarDados();
                JOptionPane.showMessageDialog(this, "Devolução registrada com sucesso!");
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Formato de data inválido! Use dd/MM/yyyy", "Erro", JOptionPane.ERROR_MESSAGE);
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