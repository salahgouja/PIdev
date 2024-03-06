package services;

import javafx.collections.ObservableList;
import models.Partenaire;
import models.Partenaire;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface IService<T> {
    void ajouter (T t) throws SQLException;
    void modifier (T t) throws SQLException;
    void supprimer (int id ) throws SQLException;
    ArrayList<Partenaire> recuperer (int id) throws SQLException;
    ArrayList<Partenaire> getPartenaireList() throws SQLException ;
}
