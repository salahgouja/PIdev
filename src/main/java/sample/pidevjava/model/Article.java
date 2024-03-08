package sample.pidevjava.model;


public class Article {
    private int idarticle;
    private String titre;
    private String description;
    private String date;
    private int nbcommentaire;
    private int iduser;

    private  int nbrlike ;


    private  int nbrdislike;

    private String imageFileName;

    private boolean accepted;

    public Article(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public Article(int idarticle, String titre, String description, String date, int nbcommentaire, int iduser, int nbrlike, int nbrdislike, boolean accepted, typec t) {
        this.idarticle = idarticle;
        this.titre = titre;
        this.description = description;
        this.date = date;
        this.nbcommentaire = nbcommentaire;
        this.iduser = iduser;
        this.nbrlike = nbrlike;
        this.nbrdislike = nbrdislike;
        this.accepted = accepted;
        this.t = t;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public Article(int idarticle, String titre, String description, String date, int nbcommentaire, int iduser, int nbrlike, int nbrdislike, String imageFileName, boolean accepted, typec t) {
        this.idarticle = idarticle;
        this.titre = titre;
        this.description = description;
        this.date = date;
        this.nbcommentaire = nbcommentaire;
        this.iduser = iduser;
        this.nbrlike = nbrlike;
        this.nbrdislike = nbrdislike;
        this.imageFileName = imageFileName;
        this.accepted = accepted;
        this.t = t;
    }

    private typec t;

    public typec getT() {
        return t;
    }

    public void setT(typec t) {
        this.t = t;
    }

    public Article() {
    }

    public void setNbrlike(int nbrlike) {
        this.nbrlike = nbrlike;
    }

    public int getNbrlike() {
        return nbrlike;
    }

    public int getNbrdislike() {
        return nbrdislike;
    }

    public void setNbrdislike(int nbrdislike) {
        this.nbrdislike = nbrdislike;
    }


    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }


    public Article(int idarticle, String titre, String description, String date, int nbcommentaire, int iduser, int nbrlike, int nbrdislike, String imageFileName, typec t) {
        this.idarticle = idarticle;
        this.titre = titre;
        this.description = description;
        this.date = date;
        this.nbcommentaire = nbcommentaire;
        this.iduser = iduser;
        this.nbrlike = nbrlike;
        this.nbrdislike = nbrdislike;
        this.imageFileName = imageFileName;
        this.t=t;
    }


    public int getIdarticle() {
        return this.idarticle;
    }

    public String getTitre() {
        return this.titre;
    }

    public String getDescription() {
        return this.description;
    }

    public String getDate() {
        return this.date;
    }

    public int getNbcommentaire() {
        return this.nbcommentaire;
    }

    public int getIduser() {
        return this.iduser;
    }

    public void setIdarticle(int idarticle) {
        this.idarticle = idarticle;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setNbcommentaire(int nbcommentaire) {
        this.nbcommentaire = nbcommentaire;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }

    public String toString() {
        return "Article{idarticle=" + this.idarticle + ", titre=" + this.titre + ", description=" + this.description + ", date=" + this.date + ", nbcommentaire=" + this.nbcommentaire + ", iduser=" + this.iduser + "}";
    }

}