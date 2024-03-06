package sample.pidevjava.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String description;
    private static final String FORM_LINK = "FormulaireAppel.fxml";

    @OneToMany(mappedBy = "article")
    private List<Appeloffre> appeloffres;

    // Constructeur par défaut nécessaire pour JPA
    public Article() {
    }

    // Constructeur avec paramètres
    public Article(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFormLink() {
        return FORM_LINK;
    }

    public List<Appeloffre> getAppeloffres() {
        return appeloffres;
    }

    public void setAppeloffres(List<Appeloffre> appeloffres) {
        this.appeloffres = appeloffres;
    }
}
