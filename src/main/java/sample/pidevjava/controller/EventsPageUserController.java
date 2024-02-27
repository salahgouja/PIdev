package sample.pidevjava.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import sample.pidevjava.Main;
import sample.pidevjava.model.Evenement;
import sample.pidevjava.model.EvetCategory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static sample.pidevjava.model.EvetCategory.evetCategory;

public class EventsPageUserController extends ISevecesEvent implements Initializable {


    @FXML
    private GridPane myGridPane;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getAll();
        setEventGridPane();
    }



    void setEventGridPane() {
        // Clear existing children from the GridPane
        myGridPane.getChildren().clear();
        final int NUM_COLUMNS = 4;

        // Counter variables for row and column indices
        int row = 0;
        int col = 0;
        // Loop through each item in the list
        for (Evenement item : evenements) {
            try {
                // Load the FXML file for the event card
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("eventCard.fxml"));
                Node node = loader.load();
                EventCardController controller = loader.getController();

                // Set the event data for the event card
                controller.setEventDataUser(item);


                // Add the event card to the GridPane at the current row and column
                myGridPane.add(node, col++, row);




                // If the column index exceeds the number of columns in the GridPane, move to the next row
                if (col == NUM_COLUMNS) {
                    col = 0;
                    row++;
                }

                GridPane.setMargin(node,new Insets(10));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
