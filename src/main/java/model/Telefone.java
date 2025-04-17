package main.java.model;

public class Telefone {
    private Long idTelefone;
    private Long idUsuario;  // FK para Usuario
    private String tipoTelefone;  // Celular, Fixo, etc.
    private String numero;

    public Telefone() {}

    public Telefone(Long idTelefone, Long idUsuario, String tipoTelefone, String numero) {
        this.idTelefone = idTelefone;
        this.idUsuario = idUsuario;
        this.tipoTelefone = tipoTelefone;
        this.numero = numero;
    }

    // Getters e Setters
    public Long getIdTelefone() { return idTelefone; }
    public void setIdTelefone(Long idTelefone) { this.idTelefone = idTelefone; }

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public String getTipoTelefone() { return tipoTelefone; }
    public void setTipoTelefone(String tipoTelefone) { this.tipoTelefone = tipoTelefone; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
}