package sample.pidevjava.model;

import javax.persistence.*;

@Entity
public class Appeloffre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nom;
    private String prenom;
    private float prix;
    private String numero;
    private String cv;
    private int articleId;
    public Appeloffre(String nom, String prenom, float prix, String numero, String cv, int articleId) {
        this.nom = nom;
        this.prenom = prenom;
        this.prix = prix;
        this.numero = numero;
        this.cv = cv;
        this.articleId = articleId;
    }



    // Constructeur par défaut nécessaire pour JPA
    public Appeloffre() {
    }

    // Constructeur avec paramètres


    public Appeloffre(String nom, String prenom, float prix, String numero, String cv) {
        this.nom = nom;
        this.prenom = prenom;
        this.prix = prix;
        this.numero = numero;
        this.cv = cv;
    }


    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;


    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    // Getters et Setters
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

    public int getArticleId() {
        return articleId;
    }


    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }











    @Override
    public String toString() {
        Article article = getArticle();
        String articleTitle = (article != null) ? article.getTitre() : "N/A";

        return "Appeloffre{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", prix=" + prix +
                ", numero='" + numero + '\'' +
                ", cv=" + cv +
                ", articleTitle='" + articleTitle + '\'' +
                '}';
    }


}