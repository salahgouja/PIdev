package sample.pidevjava.model;



public class Commentaire {
    private int idcom;
    private String contenu;
    private String date;
    private int idarticle;
    private int iduser;
    private int nblike;
    private int nbdislike;

    public Commentaire(String contenuDuCommentaire, String date, int i, int idarticle, int iduser, int nblike) {
    }

    public Commentaire(int idcom, String contenu, String date, int idarticle, int iduser, int nblike, int nbdislike) {
        this.idcom = idcom;
        this.contenu = contenu;
        this.date = date;
        this.idarticle = idarticle;
        this.iduser = iduser;
        this.nblike = nblike;
        this.nbdislike = nbdislike;
    }



    public Commentaire(String contenu, String date, int idarticle, int iduser, int nblike) {
        this.contenu = contenu;
        this.date = date;
        this.idarticle = idarticle;
        this.iduser = iduser;
        this.nblike = nblike;
    }

    public Commentaire(String contenu, String date, int idarticle, int iduser) {
        this.contenu = contenu;
        this.date = date;
        this.idarticle = idarticle;
        this.iduser = iduser;
    }

    public Commentaire(int idcom, String contenu, String date, int idarticle, int iduser) {
        this.idcom = idcom;
        this.contenu = contenu;
        this.date = date;
        this.idarticle = idarticle;
        this.iduser = iduser;
    }

    public int getIdcom() {
        return this.idcom;
    }

    public String getContenu() {
        return this.contenu;
    }

    public String getDate() {
        return this.date;
    }

    public void setIdcom(int idcom) {
        this.idcom = idcom;
    }

    public int getIdarticle() {
        return this.idarticle;
    }

    public int getIduser() {
        return this.iduser;
    }

    public int getNblike() {
        return this.nblike;
    }

    public int getNbdislike() {
        return this.nbdislike;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setIdartcile(int idarticle) {
        this.idarticle = idarticle;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }

    public void setNblike(int nblike) {
        this.nblike = nblike;
    }

    public void setNbdislike(int nbdislike) {
        this.nbdislike = nbdislike;
    }

    public String toString() {
        return "Commentaire{idcom=" + this.idcom + ", contenu=" + this.contenu + ", date=" + this.date + ", idarticle=" + this.idarticle + ", iduser=" + this.iduser + ", nblike=" + this.nblike + ", nbdislike=" + this.nbdislike + "}";
    }
}
