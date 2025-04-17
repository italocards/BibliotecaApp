package main.java.model;

import java.util.Date;
import java.util.List;

public class Usuario {
    private Long idUsuario;
    private String tipoUsuario;  // Aluno, Professor
    private String nome;
    private List<Telefone> telefones;
    private List<Endereco> enderecos;
    private Date dataNascimento;
    private String curso;  // somente para alunos

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

    public List<Telefone> getTelefones() { return telefones; }
    public void setTelefones(List<Telefone> telefones) { this.telefones = telefones; }

    public List<Endereco> getEnderecos() { return enderecos; }
    public void setEnderecos(List<Endereco> enderecos) { this.enderecos = enderecos; }

    public Date getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(Date dataNascimento) { this.dataNascimento = dataNascimento; }

    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso = curso; }

    // Método para calcular idade derivada
    public int getIdade() {
        if (dataNascimento == null) return 0;

        Date now = new Date();
        long diff = now.getTime() - dataNascimento.getTime();
        return (int) (diff / (1000L * 60 * 60 * 24 * 365));
    }

    @Override
    public String toString() {
        return nome + " (" + tipoUsuario + ")";
    }
}