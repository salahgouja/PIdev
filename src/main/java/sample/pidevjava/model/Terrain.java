package sample.pidevjava.model;

public class Terrain {
    private int id;
    private String nom;
    private boolean active;
    private int capaciteTerrain;
    private TypeTerrain type;
    private float prix_location_terrain; //edhi zidtha ene

  public Terrain(){

  }

    public Terrain(String nom) {    // edhi zidtha ene
        this.nom = nom;
    }

    public Terrain(int id, String nom, boolean active, int capaciteTerrain, TypeTerrain type,float prix_location_terrain) {

        this.id = id;
        this.nom = nom;
        this.active = active;
        this.capaciteTerrain = capaciteTerrain;
        this.type = type;
        this.prix_location_terrain=prix_location_terrain;
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

    public float getPrix_location_terrain() {
        return prix_location_terrain;
    }

    public void setPrix_location_terrain(float prix_location_terrain) {
        this.prix_location_terrain = prix_location_terrain;
    }

    @Override
    public String toString() {
        return "Terrain{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", active=" + active +
                ", capaciteTerrain=" + capaciteTerrain +
                ", type=" + type +
                ", prix_location_terrain=" + prix_location_terrain +
                '}';
    }
}
