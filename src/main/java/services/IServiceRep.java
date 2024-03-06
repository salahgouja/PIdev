package services;

import models.Reponse;

import java.sql.SQLException;

public interface IServiceRep<T>{
    void ajouter (T t) throws SQLException;
    Reponse rechercherParIdPar(int idPar) throws SQLException;
}
