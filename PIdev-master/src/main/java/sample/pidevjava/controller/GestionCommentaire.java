package sample.pidevjava.controller;

import sample.pidevjava.db.DBConnection;
import sample.pidevjava.model.Commentaire;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GestionCommentaire {



    public static void addComment(String commentaire, int articleId, int userId) {
        try {

            commentaire = BadWords.filterBadWords(commentaire);

            DBConnection dbConnection = new DBConnection();
            Connection connection = dbConnection.getConnection();

            PreparedStatement ps = connection.prepareStatement("INSERT INTO commentaire (contenu, idarticle, date, Iduser) VALUES (?, ?, ?, ?)");
            ps.setString(1, commentaire);
            ps.setInt(2, articleId);
            ps.setObject(3, LocalDateTime.now());
            ps.setInt(4, userId);
            ps.executeUpdate();

            System.out.println("Comment added successfully.");

            // Update the comment count for the article
            updateNbCommentaire(articleId);

            ps.close();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void deleteCommentaire(int idcom, int articleId) {
        try {
            DBConnection dbConnection = new DBConnection();
            Connection connection = dbConnection.getConnection();

            PreparedStatement ps = connection.prepareStatement("DELETE FROM commentaire WHERE idcom = ?");
            ps.setInt(1, idcom);
            ps.executeUpdate();

            System.out.println("Commentaire supprimé avec succès.");

            // Update the comment count for the article
            updateNbCommentaire(articleId);

            ps.close();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }





    private static void updateNbCommentaire(int articleId) {
        try {
            DBConnection dbConnection = new DBConnection();
            Connection connection = dbConnection.getConnection();

            PreparedStatement ps = connection.prepareStatement("UPDATE article SET nbcommentaire = (SELECT COUNT(*) FROM commentaire WHERE idarticle = ?) WHERE idarticle = ?");
            ps.setInt(1, articleId);
            ps.setInt(2, articleId);
            ps.executeUpdate();

            ps.close();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }




}
