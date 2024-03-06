package sample.pidevjava.model;

import java.sql.Time;
import java.time.LocalTime;

public class HoraireTravail {
    public int id_horaire;
    private String jour;
    private String heure_debut;
    private String heure_fin;
    private boolean repos;

   /* public HoraireTravail(int id_horaire,String jour, String heure_debut, String heure_fin,boolean repos) {
        this.id_horaire = id_horaire;
        this.jour = jour;
        this.heure_debut = heure_debut;
        this.heure_fin = heure_fin;
        this.repos=repos;
    }*/

    public HoraireTravail( String jour, String heure_debut, String heure_fin, boolean repos) {
       //this.id_horaire=id_horaire;
        this.jour = jour;
        this.heure_debut = heure_debut;
        this.heure_fin = heure_fin;
        this.repos = repos;
    }

    public HoraireTravail(String heureDebut, String heureFin) {
    }


    public int getId_horaire() {
        return id_horaire;
    }

    public void setId_horaire(int id_horaire) {
        this.id_horaire = id_horaire;
    }

    public String getJour() {
        return jour;
    }

    public void setJour(String jour) {
        this.jour = jour;
    }

    public String getHeure_debut() {
        return heure_debut;
    }

    public void setHeure_debut(String heure_debut) {
        this.heure_debut = heure_debut;
    }

    public String getHeure_fin() {
        return heure_fin;
    }

    public void setHeure_fin(String heure_fin) {
        this.heure_fin = heure_fin;
    }

    public boolean isRepos() {
        return repos;
    }

    public void setRepos(boolean repos) {
        this.repos = repos;
    }

    @Override
    public String toString() {
        return "HoraireTravail{" +
                "id_horaire=" + id_horaire +
                ", jour=" + jour +
                ", heure_debut=" + heure_debut +
                ", heure_fin=" + heure_fin +
                ", repos=" + repos +
                '}';
    }
}
