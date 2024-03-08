package sample.pidevjava.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import sample.pidevjava.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;





    public class StatsController {



        @FXML
        private LineChart<Integer, Integer> lineChart;

        public void initialize() {
            try {
                DBConnection dbConnection = new DBConnection();
                Connection connection = dbConnection.getConnection();

                String query = "SELECT DATE(date) AS day, COUNT(DISTINCT idarticle) AS articleCount FROM article GROUP BY day";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();

                XYChart.Series<Integer, Integer> series = new XYChart.Series<>();
                while (resultSet.next()) {
                    String day = resultSet.getString("day");
                    series.getData().add(new XYChart.Data<>(Integer.parseInt(day.split("-")[2]), resultSet.getInt("articleCount")));
                }

                resultSet.close();
                preparedStatement.close();
                connection.close();

                lineChart.getData().add(series);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }



