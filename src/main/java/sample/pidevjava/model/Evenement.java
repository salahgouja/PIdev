package sample.pidevjava.model;

import javafx.beans.value.ObservableValue;

public class Evenement {



    private int id_event;
    private String date;
    private String titre;
    private String description;
    private String prix;
    private String categorie;
    private String image;



    public Evenement( int id_event,String date, String titre, String description, String prix, String categorie, String image) {
        this.id_event = id_event;
        this.date = date;
        this.titre = titre;
        this.description = description;
        this.prix = prix;
        this.categorie = categorie;
        this.image = image;
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



    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Evenement{" +
                "id_event=" + id_event +
                ", date=" + date +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", prix=" + prix +
                ", categorie='" + categorie + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

}