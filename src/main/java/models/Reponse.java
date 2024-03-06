package models;

public class Reponse {
    private int id;
    private String motif;
    private int id_par;

    public Reponse(int id, String motif, int id_par) {
        this.id = id;
        this.motif = motif;
        this.id_par = id_par;
    }

    public Reponse() {
    }

    public Reponse(String motif, int id_par) {
        this.motif = motif;
        this.id_par = id_par;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public int getId_par() {
        return id_par;
    }

    public void setId_par(int id_par) {
        this.id_par = id_par;
    }
}
