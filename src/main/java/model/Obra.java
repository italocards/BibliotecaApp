package main.java.model;

import java.util.Date;

public class Obra {
    private Long idObra;
    private String tipoObra;
    private String titulo;
    private String autor;
    private String ISBN;
    private Date dataPublicacao;
    private String categoria;

    public Obra(){}

    public Obra(Long idObra, String tipoObra, String titulo, String autor, String ISBN, Date dataPublicacao, String categoria) {
        this.idObra = idObra;
        this.tipoObra = tipoObra;
        this.titulo = titulo;
        this.autor = autor;
        this.ISBN = ISBN;
        this.dataPublicacao = dataPublicacao;
        this.categoria = categoria;
    }

    public Long getIdObra() { return idObra; }
    public void setIdObra(Long idObra) { this.idObra = idObra; }

    public String getTipoObra() { return tipoObra; }
    public void setTipoObra(String tipoObra) { this.tipoObra = tipoObra; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public String getISBN() { return ISBN; }
    public void setISBN(String ISBN) { this.ISBN = ISBN; }

    public Date getDataPublicacao() { return dataPublicacao; }
    public void setDataPublicacao(Date dataPublicacao) { this.dataPublicacao = dataPublicacao; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    @Override
    public String toString() {
        return this.titulo; // Retorna apenas o título da obra
    }
}
