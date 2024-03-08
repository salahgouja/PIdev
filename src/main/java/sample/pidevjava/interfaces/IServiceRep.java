package sample.pidevjava.interfaces;

import sample.pidevjava.model.Reponse;

import java.sql.SQLException;

public interface IServiceRep<T>{
    void ajouter (T t) throws SQLException;
    Reponse rechercherParIdPar(int idPar) throws SQLException;
}
