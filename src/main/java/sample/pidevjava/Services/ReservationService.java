package sample.pidevjava.Services;



import sample.pidevjava.db.DBConnection;
import sample.pidevjava.interfaces.IController;
import sample.pidevjava.model.HoraireTravail;
import sample.pidevjava.model.Reservation;
import sample.pidevjava.model.Terrain;
import sample.pidevjava.model.TypeTerrain;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationService implements IController<Reservation> {

    Connection connection;
    public ReservationService()
    {
        connection = DBConnection.getInstance().getConnection();
    }
    public Terrain getById(int id) {
        Terrain terrain = null;
        try {
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

    @Override
    public void add(Reservation reservation) {
        String qry = "INSERT INTO reservation (date_reserve, temps_reservation, type, prix_reservation,id_user ,id_terrain, duree_reservation) " +
                "VALUES (?, ?, ?, ?, ?, ?,?)";
        try{
             PreparedStatement statement = connection.prepareStatement(qry);
            // statement.setInt(1, reservation.getId_reservation());
            statement.setDate(1, reservation.getDate_reserve());
            statement.setTime(2, reservation.getTemps_reservation());
            statement.setString(3, reservation.getType().toString());
            statement.setFloat(4, reservation.getPrix_reservation());
            statement.setInt(5, reservation.getId());
            statement.setInt(6, reservation.getId_terrain());
            statement.setTime(7, reservation.getDuree_reservation());

            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la réservation : " + e.getMessage());
        }
    }


    @Override
    public ArrayList<Reservation> getAll() {
        ArrayList<Reservation> reservations = new ArrayList<>();
        String qry = "SELECT * FROM `reservation`";
        try {
            // Initialiser la connexion dans le bloc try
            Statement stm = connection.createStatement();
            ResultSet rs = stm.executeQuery(qry);
            while (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setId_reservation(rs.getInt(1));
                reservation.setDate_reserve(rs.getDate(2));
                reservation.setTemps_reservation(rs.getTime(3));

                String typeValue = rs.getString("type");
                TypeTerrain typeTerrain = TypeTerrain.valueOf(typeValue);
                reservation.setType(typeTerrain);

                reservation.setPrix_reservation(rs.getFloat(5));
                reservation.setId(rs.getInt(6));
                reservation.setId_terrain(rs.getInt(7));
                reservation.setDuree_reservation(rs.getTime(8));

                //  System.out.println(reservation);

                reservations.add(reservation);
                //  System.out.println("mrgl");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reservations;
    }

    @Override
    public void update(Reservation reservation) {

    }

    @Override
    public boolean delete(Reservation reservation) {
        String qry = "DELETE FROM `reservation` WHERE `id_reservation`=?";
        try {
            PreparedStatement stm = DBConnection.getInstance().getConnection().prepareStatement(qry);
            stm.setInt(1, reservation.getId_reservation());

            int rowsDeleted = stm.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("La réservation a été supprimée avec succès.");
                return true; // La réservation a été supprimée avec succès
            } else {
                System.out.println("La réservation avec l'ID spécifié n'existe pas.");
                return false; // Aucune réservation correspondante n'a été trouvée
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false; // Une erreur s'est produite lors de la suppression de la réservation
        }
    }



    public boolean estJourDeRepos(String jour) {
        String qry = "SELECT repos FROM horairetravaille WHERE jour = ?";

        try {

            PreparedStatement stm = connection.prepareStatement(qry);

            stm.setString(1, jour);
            ResultSet resultSet = stm.executeQuery();
            System.out.println("ene tw fi estJourDeRepos ");
            System.out.println("resultSet estJourDeRepos: "+ resultSet.toString());

            if (resultSet.next()) {
                boolean repos = resultSet.getBoolean("repos");

                System.out.println(repos);
                return repos;

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Par défaut, considérons que ce n'est pas un jour de repos
        return false;
    }
    public ArrayList<Reservation> getReservationsByTerrainID(LocalDate selectedDate, int Id_terrain) {
        System.out.println("ene tw fi getReservationsByTerrainID ");

        ArrayList<Reservation> reservations_filtre = new ArrayList<>();
        String qry = "SELECT * FROM reservation WHERE date_reserve = ? AND id_terrain = ?";
        try {
             PreparedStatement statement = connection.prepareStatement(qry);
            statement.setDate(1, java.sql.Date.valueOf(selectedDate));
            statement.setInt(2, Id_terrain);
            ResultSet rs = statement.executeQuery();

            System.out.println("rs getReservationsByTerrainID: "+rs);
            while (rs.next()) {
                Reservation reservation = new Reservation();
               // System.out.println("1");
                reservation.setId_reservation(rs.getInt("id_reservation"));
               // System.out.println("2");
                reservation.setDate_reserve(rs.getDate("date_reserve"));
               // System.out.println("3");
                reservation.setTemps_reservation(rs.getTime("temps_reservation"));
                //System.out.println("4");
                reservation.setType(TypeTerrain.valueOf(rs.getString("type")));
                reservation.setPrix_reservation(rs.getFloat("prix_reservation"));
               // System.out.println("5");
                reservation.setId(rs.getInt("id_user"));
               // System.out.println("6");
              reservation.setDuree_reservation((rs.getTime("duree_reservation")));

                reservations_filtre.add(reservation);
               // System.out.println("7");

                System.out.println("reservations_filtre: "+reservations_filtre);
            }
        } catch (SQLException e) {
            // throw new RuntimeException(e);
            System.out.println(e.getMessage());

        }
        return reservations_filtre;
    }


    public List<HoraireTravail> getHorairesTravail(String jour) {
        List<HoraireTravail> horairesTravail = new ArrayList<>();

        String qry = "SELECT id_horaire, jour, heure_debut, heure_fin, repos FROM horairetravaille WHERE jour = ?";

        try  {
            PreparedStatement stm = connection.prepareStatement(qry);
            stm.setString(1, jour);
            System.out.println(stm);
            ResultSet resultSet = stm.executeQuery();

            System.out.println("Service getHorairesTravail ");
            System.out.println("Service resultSet getHorairesTravail: "+ resultSet.toString());

            while (resultSet.next()) {
                System.out.println("12134564879");
                int id_horaire = resultSet.getInt("id_horaire");
                String journew = resultSet.getString("jour");
                String heureDebut = resultSet.getString("heure_debut");
                String heureFin = resultSet.getString("heure_fin");
                boolean repos = resultSet.getBoolean("repos");
                System.out.println("--- heure debut"+ heureDebut);
                System.out.println("--- heure fin"+ heureFin);

                // Créez un objet HoraireTravail avec les données récupérées et ajoutez-le à la liste
                HoraireTravail horaire = new HoraireTravail(journew,heureDebut, heureFin, repos);

                horaire.setId_horaire(id_horaire);
                System.out.println("horaire : "+ horaire);
                horairesTravail.add(horaire);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return horairesTravail;
    }





}
