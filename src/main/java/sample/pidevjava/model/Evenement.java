package sample.pidevjava.model;

import javafx.beans.value.ObservableValue;

public class Evenement {

    private int id_event;
    private String date;
    private String titre;
    private String description;
    private String prix;
    private String type;
    private String categorie;

    public Evenement( int id_event,String date, String titre, String description, String prix, String type, String categorie) {
        this.id_event = id_event;
        this.date = date;
        this.titre = titre;
        this.description = description;
        this.prix = prix;
        this.type = type;
        this.categorie = categorie;
    }

    public Evenement() {
    }

    public int getId_event() {
        return id_event;
    }

    public void setId_event(int id_event) {
        this.id_event = id_event;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    @Override
    public String toString() {
        return "Evenement{" +
                "id_event=" + id_event +
                ", date=" + date +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", prix=" + prix +
                ", type='" + type + '\'' +
                ", categorie='" + categorie + '\'' +
                '}';
    }

}