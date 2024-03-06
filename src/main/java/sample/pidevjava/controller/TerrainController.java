package sample.pidevjava.controller;

import sample.pidevjava.interfaces.IController;
import sample.pidevjava.model.Terrain;
import sample.pidevjava.model.TypeTerrain;

import java.sql.*;
import java.util.ArrayList;

public class TerrainController implements IController<Terrain> {
    private final Connection cnx;


    public TerrainController(Connection cnx) {
        this.cnx = cnx;
    }



    @Override
    public  void add(Terrain T) {
        try {
            String requete = "INSERT INTO terrain (id, nom, active, capaciteTerrain, type) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pst = cnx.prepareStatement(requete)) {
                pst.setInt(1, T.getId());
                pst.setString(2, T.getNom());
                pst.setBoolean(3, T.isActive());
                pst.setInt(4, T.getCapaciteTerrain());
                pst.setString(5, T.getType().toString());

                pst.executeUpdate();
                System.out.println("Terrain ajouté");
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de l'ajout du terrain : " + ex.getMessage());
        }
    }

    @Override
    public ArrayList<Terrain> getAll() {
        ArrayList<Terrain> terrains = new ArrayList<>();
        String query = "SELECT * FROM terrain";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            try (ResultSet resultSet = pst.executeQuery()) {
                while (resultSet.next()) {
                    Terrain terrain = new Terrain();
                    terrain.setId(resultSet.getInt("id"));
                    terrain.setNom(resultSet.getString("nom"));
                    terrain.setActive(resultSet.getBoolean("active"));
                    terrain.setCapaciteTerrain(resultSet.getInt("capaciteTerrain"));
                    terrain.setType(TypeTerrain.valueOf(resultSet.getString("type")));

                    terrains.add(terrain);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des terrains : " + e.getMessage(), e);
        }
        return terrains;
    }


    @Override
    public void update(Terrain T) {
        String query = "UPDATE terrain SET nom=?, active=?, capaciteTerrain=?, type=? WHERE id=?";
        try {
            try (PreparedStatement pst = cnx.prepareStatement(query)) {
                pst.setString(1, T.getNom());
                pst.setBoolean(2, T.isActive());
                pst.setInt(3, T.getCapaciteTerrain());
                pst.setString(4, T.getType().toString());
                pst.setInt(5, T.getId());

                pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean delete(Terrain terrain) {
        String query = "DELETE FROM terrain WHERE id=?";
        try {
            try (PreparedStatement pst = cnx.prepareStatement(query)) {
                pst.setInt(1, terrain.getId());
                int rowsAffected = pst.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Terrain supprimé avec succès");
                    return true;
                } else {
                    System.out.println("Terrain introuvable");
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du terrain : " + e.getMessage(), e);
        }
    }
    public Terrain getById(int id) {
        String query = "SELECT * FROM terrain WHERE id=?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, id);

            try (ResultSet resultSet = pst.executeQuery()) {
                if (resultSet.next()) {
                    Terrain terrain = new Terrain();
                    terrain.setId(resultSet.getInt("id"));
                    terrain.setNom(resultSet.getString("nom"));
                    terrain.setActive(resultSet.getBoolean("active"));
                    terrain.setCapaciteTerrain(resultSet.getInt("capaciteTerrain"));
                    terrain.setType(TypeTerrain.valueOf(resultSet.getString("type")));

                    return terrain;
                } else {
                    System.out.println("Terrain non trouvé pour l'ID : " + id);
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération du terrain par ID : " + e.getMessage(), e);
        }
    }





    }


