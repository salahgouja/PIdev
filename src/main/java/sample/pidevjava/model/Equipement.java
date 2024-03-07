package sample.pidevjava.model;

import java.util.Objects;

public class Equipement {
    private int id_equipement;
    private String type;
    private String imageSrc; // zidtha ene
    private String nom_equipement; //zidtha ene
    private int quantite;
    private float prix_location;

    public Equipement(){

    }


    public Equipement(String type,String imageSrc,String nom_equipement, int quantite, float prix_location) {
      //  this.id_equipement = id_equipement;
        this.type = type;
        this.imageSrc=imageSrc; // dont forget this
        this.nom_equipement=nom_equipement; //dont forget this
        this.quantite=quantite;
        this.prix_location=prix_location;
    }

    public String getNom_equipement() {
        return nom_equipement;
    }

    public void setNom_equipement(String nom_equipement) {
        this.nom_equipement = nom_equipement;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public int getId_equipement() {
        return id_equipement;
    }

    // Setter for id_equipement
    public void setId_equipement(int id_equipement) {
        this.id_equipement = id_equipement;
    }

    // Getter for type
    public String getType() {
        return type;
    }

    // Setter for type
    public void setType(String type) {
        this.type = type;
    }

    // Getter for quantite
    public int getQuantite() {
        return quantite;
    }

    // Setter for quantite
    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    // Getter for prix_location
    public float getPrix_location() {
        return prix_location;
    }

    // Setter for prix_location
    public void setPrix_location(float prix_location) {
        this.prix_location = prix_location;
    }

    // toString method
    @Override
    public String toString() {
        return "Equipement{" +
                "id_equipement=" + id_equipement +
                ", type='" + type + '\'' +
                ", quantite=" + quantite +
                ", prix_location=" + prix_location +
                '}';
    }

    // equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipement that = (Equipement) o;
        return id_equipement == that.id_equipement &&
                quantite == that.quantite &&
                Float.compare(that.prix_location, prix_location) == 0 &&
                Objects.equals(type, that.type);
    }

    // hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(id_equipement, type, quantite, prix_location);
    }
}



