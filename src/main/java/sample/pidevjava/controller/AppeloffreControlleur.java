package sample.pidevjava.controller;

import sample.pidevjava.interfaces.IController;
import sample.pidevjava.model.Appeloffre;
import sample.pidevjava.model.Article;
import sample.pidevjava.model.Terrain;
import sample.pidevjava.model.TypeTerrain;

import java.sql.*;
import java.util.ArrayList;

public class AppeloffreControlleur implements IController<Appeloffre> {
    private final Connection cnx;

    public AppeloffreControlleur(Connection cnx) {
        this.cnx = cnx;
    }

    @Override
    public void add(Appeloffre T) {
        try {
            String requete = "INSERT INTO appeloffre (id, nom, prenom, prix, numero, cv) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pst = cnx.prepareStatement(requete)) {
                pst.setInt(1, T.getId());
                pst.setString(2, T.getNom());
                pst.setString(3, T.getPrenom());
                pst.setFloat(4, T.getPrix());
                pst.setString(5, T.getNumero());
                pst.setString(6, T.getCv());
                /*
                pst.setInt(7, T.getArticle().getId());
*/
/*

                pst.setInt(7, T.getArticleId());

*/
                pst.executeUpdate();
                System.out.println("Appel offre ajouté");
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de l'ajout de l'appel offre : " + ex.getMessage());
        }
    }

    @Override
    public ArrayList<Appeloffre> getAll() {
        ArrayList<Appeloffre> appels = new ArrayList<>();
        String query = "SELECT * FROM appeloffre";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            try (ResultSet resultSet = pst.executeQuery()) {
                while (resultSet.next()) {
                    Appeloffre appel = new Appeloffre();
                    appel.setId(resultSet.getInt("id"));
                    appel.setNom(resultSet.getString("nom"));
                    appel.setPrenom(resultSet.getString("prenom"));
                    appel.setPrix(resultSet.getFloat("prix"));
                    appel.setNumero(resultSet.getString("numero"));
                    appel.setCv(resultSet.getString("cv"));

                    int articleId = resultSet.getInt("article_id");

                    // Utilisez la méthode getArticleById pour récupérer l'article associé
                    Article article = getArticleById(articleId);

                    // Définir l'article dans l'objet Appeloffre
                    appel.setArticle(article);

                    appels.add(appel);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des appels : " + e.getMessage(), e);
        }
        return appels;
    }




    @Override
    public void update(Appeloffre T) {
        String query = "UPDATE appeloffre SET nom=?, prenom=?, prix=?, numero=?, cv=? WHERE id=?";
        try {
            try (PreparedStatement pst = cnx.prepareStatement(query)) {
                pst.setString(1, T.getNom());
                pst.setString(2, T.getPrenom());
                pst.setFloat(3, T.getPrix());
                pst.setString(4, T.getNumero());
                pst.setString(5, T.getCv());
                pst.setInt(6, T.getId());

                pst.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean delete(Appeloffre appeloffre) {
        String query = "DELETE FROM appeloffre WHERE id=?";
        try {
            try (PreparedStatement pst = cnx.prepareStatement(query)) {
                pst.setInt(1, appeloffre.getId());
                int rowsAffected = pst.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Appel offre supprimé avec succès");
                    return true;
                } else {
                    System.out.println("Appel offre introuvable");
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de l'appel offre : " + e.getMessage(), e);
        }
    }

    public Appeloffre getById(int id) {
        String query = "SELECT * FROM appeloffre WHERE id=?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, id);

            try (ResultSet resultSet = pst.executeQuery()) {
                if (resultSet.next()) {
                    Appeloffre appel = new Appeloffre();
                    appel.setId(resultSet.getInt("id"));
                    appel.setNom(resultSet.getString("nom"));
                    appel.setPrenom(resultSet.getString("prenom"));
                    appel.setPrix(resultSet.getFloat("prix"));
                    appel.setNumero(resultSet.getString("numero"));
                    appel.setCv(resultSet.getString("cv"));

                    return appel;
                } else {
                    System.out.println("Appel offre non trouvé pour l'ID : " + id);
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de l'appel offre par ID : " + e.getMessage(), e);
        }
    }


    public Article getArticleById(int articleId) {
        String query = "SELECT * FROM article WHERE id=?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, articleId);

            try (ResultSet resultSet = pst.executeQuery()) {
                if (resultSet.next()) {
                    Article article = new Article();
                    article.setId(resultSet.getInt("id"));
                    article.setTitle(resultSet.getString("title"));
                    // Ajoutez d'autres propriétés si nécessaire

                    return article;
                } else {
                    System.out.println("Article non trouvé pour l'ID : " + articleId);
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération de l'article par ID : " + e.getMessage(), e);
        }
    }


}
