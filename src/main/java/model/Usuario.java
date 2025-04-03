package main.java.model;

import java.util.Date;

public class Usuario {
    private Long idUsuario;
    private String tipoUsuario;
    private String nome;
    private Date dataNascimento;
    private String curso;

    // Construtores
    public Usuario() {}

    public Usuario(Long idUsuario, String tipoUsuario, String nome, Date dataNascimento, String curso) {
        this.idUsuario = idUsuario;
        this.tipoUsuario = tipoUsuario;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.curso = curso;
    }

    // Getters e Setters
    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public String getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(String tipoUsuario) { this.tipoUsuario = tipoUsuario; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Date getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(Date dataNascimento) { this.dataNascimento = dataNascimento; }

    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }

    @Override
    public String toString() {
        return nome + " (" + tipoUsuario + ")";
    }
}