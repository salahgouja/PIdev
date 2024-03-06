package sample.pidevjava.model;

public class Participation {

    private int  id_participation;
    private String etat;

    private int id_event;

    private int id;

    private String dateDeCreation;


    @Override
    public String toString() {
        return "Participation{" +
                "id_participation=" + id_participation +
                ", etat='" + etat + '\'' +
                ", id_event=" + id_event +
                ", id=" + id +
                ", dateDeCreation=" + dateDeCreation +
                '}';
    }

    public Participation() {
    }

    public Participation(int id_participation, String etat, int id_event, int id ,String dateDeCreation) {
        this.id_participation = id_participation;
        this.etat = etat;
        this.id_event = id_event;
        this.id = id;
        this.dateDeCreation = dateDeCreation;
    }

    public int getId_participation() {
        return id_participation;
    }

    public void setId_participation(int id_participation) {
        this.id_participation = id_participation;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public int getId_event() {
        return id_event;
    }

    public void setId_event(int id_event) {
        this.id_event = id_event;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateDeCreation() {
        return dateDeCreation;
    }

    public void setDateDeCreation(String dateDeCreation) {
        this.dateDeCreation = dateDeCreation;
    }

}
