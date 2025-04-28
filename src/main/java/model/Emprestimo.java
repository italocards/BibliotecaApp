package main.java.model;

import java.util.Date;

public class Emprestimo {
    private Long idEmprestimo;
    private Long idUsuario;
    private Long idObra;
    private Date dataEmprestimo;
    private Date dataPrevDevolucao;
    private Date dataRealDevolucao;

    public Emprestimo() {}

    public Emprestimo(Long idEmprestimo, Long idUsuario, Long idObra, Date dataEmprestimo, Date dataPrevDevolucao, Date dataRealDevolucao) {
        this.idEmprestimo = idEmprestimo;
        this.idUsuario = idUsuario;
        this.idObra = idObra;
        this.dataEmprestimo = dataEmprestimo;
        this.dataPrevDevolucao = dataPrevDevolucao;
        this.dataRealDevolucao = dataRealDevolucao;
    }

    // Getters e Setters
    public Long getIdEmprestimo() { return idEmprestimo; }
    public void setIdEmprestimo(Long idEmprestimo) { this.idEmprestimo = idEmprestimo; }

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public Long getIdObra() { return idObra; }
    public void setIdObra(Long idObra) { this.idObra = idObra; }

    public Date getDataEmprestimo() { return dataEmprestimo; }
    public void setDataEmprestimo(Date dataEmprestimo) { this.dataEmprestimo = dataEmprestimo; }

    public Date getDataPrevDevolucao() { return dataPrevDevolucao; }
    public void setDataPrevDevolucao(Date dataPrevDevolucao) { this.dataPrevDevolucao = dataPrevDevolucao; }

    public Date getDataRealDevolucao() { return dataRealDevolucao; }
    public void setDataRealDevolucao(Date dataRealDevolucao) { this.dataRealDevolucao = dataRealDevolucao; }
}