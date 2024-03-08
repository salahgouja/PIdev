package sample.pidevjava.Services;

import sample.pidevjava.db.DBConnection;
import sample.pidevjava.interfaces.IController;
import sample.pidevjava.model.Equipement;
import sample.pidevjava.model.EquipementLocation;
import sample.pidevjava.model.Reservation;
import sample.pidevjava.model.TypeTerrain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EquipementService implements IController<Equipement> {
    Connection connection;
    Equipement equipement=new Equipement();
    public EquipementService()
    {
        connection = DBConnection.getInstance().getConnection();
    }


    @Override
    public void add(Equipement equipement) {

    }

    @Override
    public ArrayList<Equipement> getAll() {
        return null;
    }

    @Override
    public void update(Equipement equipement) {

    }

    @Override
    public boolean delete(Equipement equipement) {
        return false;
    }


    public ArrayList<Equipement> getEquipementsByType( String type) {
      //  System.out.println("ene tw fi getReservationsByTerrainID ");

        ArrayList<Equipement> equipements = new ArrayList<>();
        String qry = "SELECT * FROM equipement WHERE  type = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(qry);

            statement.setString(1, type);
            ResultSet rs = statement.executeQuery();

            System.out.println("rs: "+rs);

            System.out.println("rs getReservationsByTerrainID: "+rs);
            while (rs.next()) {
                Equipement equipement = new Equipement();
                // System.out.println("1");
                equipement.setId_equipement(rs.getInt("id_equipement"));
                // System.out.println("2");
                equipement.setType(rs.getString("type"));
                // System.out.println("3");
                equipement.setImageSrc(rs.getString("imageSrc"));
                //System.out.println("4");

                equipement.setNom_equipement(rs.getString("nom_equipement"));
                // System.out.println("5");
                equipement.setPrix_location(rs.getFloat("prix_location"));
                // System.out.println("6");


                equipements.add(equipement);
                // System.out.println("7");

                System.out.println("equipements: "+equipements);
            }
        } catch (SQLException e) {
            // throw new RuntimeException(e);
            System.out.println(e.getMessage());

        }
        return equipements;
    }
    // lihna chnakhou ekhir reservation
    public int getDerniereReservationId() {
        String qry = "SELECT id_reservation FROM reservation ORDER BY id_reservation DESC LIMIT 1";
        try {
            PreparedStatement stm = connection.prepareStatement(qry);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_reservation");
            } else {
                // Si aucun résultat n'est trouvé, retourner -1 ou une valeur qui indique l'absence de résultat
                return -1; // Ou une valeur par défaut appropriée
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public float getPrixLocationByIDEquipement(int id_equipement) {  //prix equipemen by id
        String qry = "SELECT prix_location FROM equipement WHERE id_equipement=?";
        try {
            PreparedStatement stm = connection.prepareStatement(qry);
            stm.setInt(1, id_equipement); // Remplacer le premier paramètre de la requête par la valeur de l'id équipement
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getFloat("prix_location");
            } else {
                // Gérer le cas où aucun équipement avec cet ID n'est trouvé
                // Par exemple, vous pouvez lancer une exception ou retourner une valeur par défaut
                throw new RuntimeException("Aucun équipement avec l'ID spécifié trouvé.");
            }
        } catch (SQLException e) {
            // Gérer les erreurs SQL
            throw new RuntimeException(e);
        }
    }

    public void ajouterLocation(int id_reservation, int id_equipement, int quantite_location) {
        String qry = "INSERT INTO equipementlocation (id_reservation, id_equipement, quantite_location) VALUES (?, ?, ?)";

        try {
            PreparedStatement stm = connection.prepareStatement(qry);
            System.out.println("1");
            stm.setInt(1, id_reservation);
            System.out.println("2");
            stm.setInt(2, id_equipement);
            System.out.println("3");
            stm.setInt(3, quantite_location);
            System.out.println("4");
            stm.executeUpdate();
            System.out.println("Ligne ajoutée avec succès.");
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'ajout de la ligne dans la table.", e);
        }
    }


    public Reservation getReservationById(int idReservation) {
        String qry = "SELECT * FROM reservation WHERE id_reservation = ?";
        try {
            PreparedStatement stm = connection.prepareStatement(qry);
            stm.setInt(1, idReservation);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                // Créer un nouvel objet Reservation à partir des données de la ligne récupérée
                Reservation reservation = new Reservation();
                reservation.setId_reservation(rs.getInt("id_reservation"));
               reservation.setDate_reserve(rs.getDate("date_reserve"));
               reservation.setTemps_reservation(rs.getTime("temps_reservation"));
                String typeValue = rs.getString("type");
                TypeTerrain typeTerrain = TypeTerrain.valueOf(typeValue);
                reservation.setType(typeTerrain);
                reservation.setPrix_reservation(rs.getFloat("prix_reservation"));
                reservation.setId(rs.getInt("id_user"));
                reservation.setId(rs.getInt("id_terrain"));
                reservation.setDuree_reservation(rs.getTime("duree_reservation"));


                // Vous devez définir d'autres propriétés de l'objet Reservation de la même manière
                // en fonction de la structure de votre table reservation
                return reservation;
            } else {
                // Si aucun résultat n'est trouvé, retourner null ou une valeur qui indique l'absence de résultat
                return null; // Ou une valeur par défaut appropriée
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public int getIdEquipementByNom(String nomEquipement) {
        String qry = "SELECT id_equipement FROM equipement WHERE nom_equipement = ?";
        try {
            PreparedStatement stm = connection.prepareStatement(qry);
            stm.setString(1, nomEquipement);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_equipement");
            } else {
                throw new RuntimeException("Aucun équipement trouvé avec le nom spécifié.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //chnakhou objet location
    public List<EquipementLocation> getLocationEquipementsByReservationId(int id_reservation) {
        List<EquipementLocation> locationEquipements = new ArrayList<>();

        String qry = "SELECT * FROM equipementlocation WHERE id_reservation = ?";
        try {
            PreparedStatement stm = connection.prepareStatement(qry);
            stm.setInt(1, id_reservation);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                int id_location= rs.getInt("id_location");
                //int id_reservation= rs.getInt("id_reservation");
                int id_equipement = rs.getInt("id_equipement");
                int quantite_location = rs.getInt("quantite_location");

                // Ajouter les autres colonnes nécessaires

                // Créer un objet LocationEquipement et l'ajouter à la liste
                EquipementLocation locationEquipement = new EquipementLocation(id_location,id_reservation, id_equipement, quantite_location);
                locationEquipements.add(locationEquipement);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des équipements de la réservation.", e);
        }

        return locationEquipements;
    }

    public List<Equipement> getAllEquipement() {
        List<Equipement> equipements = new ArrayList<>();

        // Requête SQL pour sélectionner tous les équipements
        String qry = "SELECT * FROM equipement";

        try (
                // Récupérer la connexion à la base de données

                PreparedStatement stm = connection.prepareStatement(qry);
                // Exécuter la requête et obtenir le résultat
                ResultSet resultSet = stm.executeQuery();
        ) {
            // Parcourir les résultats et mapper chaque ligne à un objet Equipement
            while (resultSet.next()) {
                int id_equipement = resultSet.getInt("id_equipement");
                String type = resultSet.getString("type");
                String imageSrc = resultSet.getString("imageSrc");
                String nom_equipement = resultSet.getString("nom_equipement");
                int quantite = resultSet.getInt("quantite");
                float prix_location = resultSet.getFloat("prix_location");

                // Créer un objet Equipement et l'ajouter à la liste des équipements
                Equipement equipement = new Equipement(type, imageSrc, nom_equipement, quantite, prix_location);
                equipement.setId_equipement(id_equipement);
                equipements.add(equipement);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Gérer les exceptions SQL
        }

        return equipements;
    }

}







