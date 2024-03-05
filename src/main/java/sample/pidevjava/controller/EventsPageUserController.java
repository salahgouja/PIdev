package sample.pidevjava.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
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
    private FlowPane myFlowPane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getAll();

        setEventFlowPane();
    }



    void setEventFlowPane() {

        myFlowPane.getChildren().clear();
        myFlowPane.setHgap(10);
        myFlowPane.setVgap(10);

        for (Evenement item : evenements) {
            try {

                FXMLLoader loader = new FXMLLoader(Main.class.getResource("eventCard.fxml"));
                Node node = loader.load();
                EventCardController controller = loader.getController();

                controller.setEventDataUser(item);

                myFlowPane.getChildren().add(node);

                FlowPane.setMargin(node, new Insets(10));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}



