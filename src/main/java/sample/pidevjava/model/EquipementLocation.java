package sample.pidevjava.model;

public class EquipementLocation {
    private int id_location;
    private int id_reservation;
    private int id_equipemment;
    private  int quantite_location;

    public int getId_location() {
        return id_location;
    }

    public void setId_location(int id_location) {
        this.id_location = id_location;
    }

    public int getId_reservation() {
        return id_reservation;
    }

    public void setId_reservation(int id_reservation) {
        this.id_reservation = id_reservation;
    }

    public int getId_equipemment() {
        return id_equipemment;
    }

    public void setId_equipemment(int id_equipemment) {
        this.id_equipemment = id_equipemment;
    }

    public int getQuantite_location() {
        return quantite_location;
    }

    public void setQuantite_location(int quantite_location) {
        this.quantite_location = quantite_location;
    }

    public EquipementLocation(int id_location, int id_reservation, int id_equipemment, int quantite_location) {
        this.id_location = id_location;
        this.id_reservation = id_reservation;
        this.id_equipemment = id_equipemment;
        this.quantite_location = quantite_location;
    }

    @Override
    public String toString() {
        return "EquipementLocation{" +
                "id_location=" + id_location +
                ", id_reservation=" + id_reservation +
                ", id_equipemment=" + id_equipemment +
                ", quantite_location=" + quantite_location +
                '}';
    }

}
