package main.java.controller;

import main.java.model.DAO.ObraDAO;
import main.java.model.Obra;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class ObraController {
    private ObraDAO obraDAO;

    public ObraController(){
        this.obraDAO = new ObraDAO();
    }
    public void CadastrarObra(String tipoObra, String titulo, String autor, String ISBN, Date dataPublicacao, String categoria) throws SQLException {
            Obra obra = new Obra(null, tipoObra, titulo, autor, ISBN, dataPublicacao, categoria);
            obraDAO.create(obra);
    }
    public List<Obra> ListarObras() throws SQLException{
        return obraDAO.readAll();
    }

    public Obra buscarObraPorId(Long id) throws SQLException{
        return obraDAO.readById(id);
    }

    public void atualizarObra(Long id, String tipoObra, String titulo, String autor, String ISBN, Date dataPublicacao, String categoria) throws SQLException{
        Obra obra = new Obra(id, tipoObra, titulo, autor, ISBN, dataPublicacao, categoria);
        obraDAO.update(obra);
    }

    public void deletarObra(Long id) throws SQLException{
        obraDAO.delete(id);
    }
}
