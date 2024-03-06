package models;

public class Partenaire {
    private int id_partenaire;
    private String nom;
    private String prenom;
    private int num_tel;
    private String email;
    private String type_partenaire;
    private float montant;
    private String type_equipement;
    private int quantite;
    private String prix_location;
    private int etat;
    private int id_user;

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getId_partenaire() {
        return id_partenaire;
    }

    public void setId_partenaire(int id_partenaire) {
        this.id_partenaire = id_partenaire;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public int getNum_tel() {
        return num_tel;
    }

    public void setNum_tel(int num_tel) {
        this.num_tel = num_tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType_partenaire() {
        return type_partenaire;
    }

    public void setType_partenaire(String type_partenaire) {
        this.type_partenaire = type_partenaire;
    }

    public float getMontant() {
        return montant;
    }

    public void setMontant(float montant) {
        this.montant = montant;
    }

    public String getType_equipement() {
        return type_equipement;
    }

    public void setType_equipement(String type_equipement) {
        this.type_equipement = type_equipement;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public String getPrix_location() {
        return prix_location;
    }

    public void setPrix_location(String prix_location) {
        this.prix_location = prix_location;
    }

    public Partenaire(){}

    public Partenaire(int id_partenaire, String nom, String prenom, int num_tel, String email, String type_partenaire, float montant, String type_equipement, int quantite, String prix_location) {
        this.id_partenaire = id_partenaire;
        this.nom = nom;
        this.prenom = prenom;
        this.num_tel = num_tel;
        this.email = email;
        this.type_partenaire = type_partenaire;
        this.montant = montant;
        this.type_equipement = type_equipement;
        this.quantite = quantite;
        this.prix_location = prix_location;
    }
    public Partenaire(String nom, String prenom, int num_tel, String email, String type_partenaire, float montant, String type_equipement, int quantite, String prix_location) {
        this.nom = nom;
        this.prenom = prenom;
        this.num_tel = num_tel;
        this.email = email;
        this.type_partenaire = type_partenaire;
        this.montant = montant;
        this.type_equipement = type_equipement;
        this.quantite = quantite;
        this.prix_location = prix_location;
    }
    public Partenaire(String nom, String prenom, int num_tel, String email, String type_partenaire, float montant,int etat,int id_user) {
        this.nom = nom;
        this.prenom = prenom;
        this.num_tel = num_tel;
        this.email = email;
        this.type_partenaire = type_partenaire;
        this.montant = montant;
        this.etat = etat;
        this.id_user = id_user;

    }
    public Partenaire(String nom, String prenom, int num_tel, String email, String type_partenaire, String type_equipement, int quantite,int etat,int id_user) {
        this.nom = nom;
        this.prenom = prenom;
        this.num_tel = num_tel;
        this.email = email;
        this.type_partenaire = type_partenaire;
        this.type_equipement = type_equipement;
        this.quantite = quantite;
        this.etat = etat;
        this.id_user = id_user;
    }
}
