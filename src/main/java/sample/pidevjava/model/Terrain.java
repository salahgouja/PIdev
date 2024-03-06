package sample.pidevjava.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Entity
public class Terrain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nom;
    private boolean active;
    private int capaciteTerrain;
    private TypeTerrain type;

    public Terrain( String nom, boolean active, int capaciteTerrain, TypeTerrain type) {
        this.nom = nom;
        this.active = active;
        this.capaciteTerrain = capaciteTerrain;
        this.type = type;
    }

    public Terrain(int id, String nom, boolean active, int capaciteTerrain, TypeTerrain type) {
        this.id = id;
        this.nom = nom;
        this.active = active;
        this.capaciteTerrain = capaciteTerrain;
        this.type = type;
    }

    public Terrain() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getCapaciteTerrain() {
        return capaciteTerrain;
    }

    public void setCapaciteTerrain(int capaciteTerrain) {
        this.capaciteTerrain = capaciteTerrain;
    }

    public TypeTerrain getType() {
        return type;
    }

    public void setType(TypeTerrain type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Terrain{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", active=" + active +
                ", capaciteTerrain=" + capaciteTerrain +
                ", type=" + type +
                '}';
    }


}
