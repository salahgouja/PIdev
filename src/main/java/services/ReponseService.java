package services;

import models.Reponse;
import utils.Mydatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReponseService implements IServiceRep<Reponse>{
    private Connection connection;

    public ReponseService() {
        connection = Mydatabase.getInstance().getConnection();
    }
    @Override
    public void ajouter(Reponse reponse) throws SQLException {
        String req = "INSERT INTO reponse(motif, id_par) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            preparedStatement.setString(1, reponse.getMotif());
            preparedStatement.setInt(2, reponse.getId_par());
            preparedStatement.executeUpdate();
        }
    }
    public Reponse rechercherParIdPar(int idPar) throws SQLException {
        Reponse reponse = null;
        String req = "SELECT * FROM reponse WHERE id_par = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            preparedStatement.setInt(1, idPar);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    reponse = new Reponse();
                    reponse.setId(resultSet.getInt("id"));
                    reponse.setMotif(resultSet.getString("motif"));
                    reponse.setId_par(resultSet.getInt("id_par"));
                }
            }
        }
        return reponse;
    }
}
