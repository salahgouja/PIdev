package sample.pidevjava.interfaces;

import sample.pidevjava.model.Partenaire;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IService1<T> {
    void ajouter (T t) throws SQLException;
    void modifier (T t) throws SQLException;
    void supprimer (int id ) throws SQLException;
    ArrayList<Partenaire> recuperer (int id) throws SQLException;
    ArrayList<Partenaire> getPartenaireList() throws SQLException ;
}
