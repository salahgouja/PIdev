package sample.pidevjava.controller;

import sample.pidevjava.db.DBConnection;
import sample.pidevjava.model.Terrain;
import sample.pidevjava.model.TypeTerrain;
import sample.pidevjava.model.User;

import java.sql.*;

public class TerrainController {
    public Terrain getById(int id) {
        Terrain terrain = null;
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM terrain WHERE id = ?");
            statement.setInt(1, id); // Set the id parameter for the prepared statement
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) { // Move the cursor to the first row
                terrain = new Terrain();
                terrain.setId(resultSet.getInt("id"));
                terrain.setNom(resultSet.getString("nom"));
                terrain.setActive(resultSet.getBoolean("active"));
                terrain.setCapaciteTerrain(resultSet.getInt("capacite"));
                terrain.setPrix_location_terrain(resultSet.getFloat("prix_location_terrain"));
                terrain.setType(TypeTerrain.valueOf(resultSet.getString("type")));

            }

            // statement.close();
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return terrain;
    }
}
