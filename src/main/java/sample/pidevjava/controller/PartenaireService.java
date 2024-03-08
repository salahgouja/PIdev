package sample.pidevjava.controller;

import sample.pidevjava.model.Partenaire;
import sample.pidevjava.db.DBConnection;

import java.sql.*;
import java.util.ArrayList;

public class PartenaireService implements sample.pidevjava.interfaces.IService1<Partenaire> {
    private Connection connection;

    public PartenaireService() {
        connection = DBConnection.getInstance().getConnection();
    }

    @Override
    public void ajouter(Partenaire partenaire) throws SQLException {
        String req = "INSERT INTO partenaire(nom, prenom, num_tel, email, type_partenaire, montant, type_equi, quantite, prix_location,etat,id_user) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            preparedStatement.setString(1, partenaire.getNom());
            preparedStatement.setString(2, partenaire.getPrenom());
            preparedStatement.setInt(3, partenaire.getNum_tel());
            preparedStatement.setString(4, partenaire.getEmail());
            preparedStatement.setString(5, partenaire.getType_partenaire());
            preparedStatement.setFloat(6, partenaire.getMontant());
            preparedStatement.setString(7, partenaire.getType_equipement());
            preparedStatement.setInt(8, partenaire.getQuantite());
            preparedStatement.setString(9, partenaire.getPrix_location());
            preparedStatement.setInt(10, partenaire.getEtat());
            preparedStatement.setInt(11, partenaire.getId_user());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void modifier(Partenaire partenaire) throws SQLException {
        String sql = "UPDATE partenaire SET etat=? WHERE id_partenaire=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, partenaire.getEtat());
            preparedStatement.setInt(2, partenaire.getId_partenaire());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM partenaire WHERE id_partenaire=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(req)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public ArrayList<Partenaire> recuperer(int id) throws SQLException {
        String sql = "SELECT * FROM partenaire WHERE id_user = " + id;
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            ArrayList<Partenaire> list = new ArrayList<>();
            while (rs.next()) {
                Partenaire p = new Partenaire();
                p.setId_partenaire(rs.getInt("id_partenaire"));
                p.setNom(rs.getString("nom"));
                p.setPrenom(rs.getString("prenom"));
                p.setNum_tel(rs.getInt("num_tel"));
                p.setEmail(rs.getString("email"));
                p.setType_partenaire(rs.getString("type_partenaire"));
                p.setMontant(rs.getFloat("montant"));
                p.setType_equipement(rs.getString("type_equi"));
                p.setQuantite(rs.getInt("quantite"));
                p.setPrix_location(rs.getString("prix_location"));
                p.setEtat(rs.getInt("etat")); // Corrected line
                list.add(p);
            }
            return list;
        }
    }


    public ArrayList<Partenaire> getPartenaireList() throws SQLException {
        ArrayList<Partenaire> partenaireList = new ArrayList<>();
        String query = "SELECT * FROM partenaire";
        try (Statement stm = connection.createStatement();
             ResultSet rs = stm.executeQuery(query)) {
            while (rs.next()) {
                Partenaire p = new Partenaire();
                p.setId_partenaire(rs.getInt("id_partenaire"));
                p.setNom(rs.getString("nom"));
                p.setPrenom(rs.getString("prenom"));
                p.setNum_tel(rs.getInt("num_tel"));
                p.setEmail(rs.getString("email"));
                p.setType_partenaire(rs.getString("type_partenaire"));
                p.setMontant(rs.getFloat("montant"));
                p.setType_equipement(rs.getString("type_equi"));
                p.setQuantite(rs.getInt("quantite"));
                p.setPrix_location(rs.getString("prix_location"));
                p.setEtat(rs.getInt("etat"));
                partenaireList.add(p);
            }
        }
        return partenaireList;
    }
}
