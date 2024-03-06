package GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import models.Partenaire;
import services.PartenaireService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;

public class dashController implements Initializable {

    @FXML
    private ListView<Partenaire> mylistview;

    public ListView<Partenaire> getMylistview() {
        return mylistview;
    }

    @FXML
    private Label nbEvents;

    @FXML
    private Label nbParticipation;

    @FXML
    private TextField searchField;

    ArrayList<Partenaire> partenaires = new ArrayList<>();
    private FilteredList<Partenaire> filteredPartenaires;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PartenaireService par = new PartenaireService();
        try {
            partenaires = par.getPartenaireList();
            ObservableList<Partenaire> observableList = FXCollections.observableArrayList(partenaires);
            filteredPartenaires = new FilteredList<>(observableList, p -> true);
            setEventList();
            setSearchListener();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void setEventList() {
        ObservableList<Partenaire> observableList = FXCollections.observableArrayList(partenaires);

        mylistview.setItems(observableList);

        nbEvents.setText(String.valueOf(partenaires.size()));
        long count = partenaires.stream().filter(partenaire -> partenaire.getEtat() == 1).count();
        nbParticipation.setText(String.valueOf(count));

        mylistview.setCellFactory(param -> new ListCell<Partenaire>() {
            @Override
            protected void updateItem(Partenaire item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/partenaireCardDash.fxml"));
                        Node node = loader.load();
                        CardController controller = loader.getController();
                        controller.setEventData(item);
                        setGraphic(node);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    void setSearchListener() {
        searchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String query = newValue.toLowerCase().trim();

                if (query.isEmpty()) {
                    mylistview.setItems(FXCollections.observableArrayList(partenaires));
                } else {

                    ObservableList<Partenaire> filteredList = FXCollections.observableArrayList();
                    for (Partenaire partenaire : partenaires) {
                        if (partenaire.getNom().toLowerCase().contains(query) ||
                                partenaire.getPrenom().toLowerCase().contains(query) ||
                                String.valueOf(partenaire.getNum_tel()).toLowerCase().contains(query) ||
                                partenaire.getEmail().toLowerCase().contains(query) ||
                                partenaire.getType_partenaire().toLowerCase().contains(query)) {
                            filteredList.add(partenaire);
                        }
                    }
                    mylistview.setItems(filteredList);
                }
            }
        });
    }
    @FXML
  void refresh(ActionEvent event) {
        try {
            PartenaireService par = new PartenaireService();
            partenaires = par.getPartenaireList();
            ObservableList<Partenaire> observableList = FXCollections.observableArrayList(partenaires);
            mylistview.setItems(observableList);
            nbEvents.setText(String.valueOf(partenaires.size()));
            long count = partenaires.stream().filter(partenaire -> partenaire.getEtat() == 1).count();
            nbParticipation.setText(String.valueOf(count));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void Tri(ActionEvent event) {
        Collections.sort(partenaires, new Comparator<Partenaire>() {
            @Override
            public int compare(Partenaire p1, Partenaire p2) {
                return p1.getNom().compareToIgnoreCase(p2.getNom());
            }
        });

        ObservableList<Partenaire> sortedList = FXCollections.observableArrayList(partenaires);
        mylistview.setItems(sortedList);
    }

    @FXML
    void excel(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName("partenaires.xlsx");
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try {
                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("Partenaires");

                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Nom");
                headerRow.createCell(1).setCellValue("Prenom");
                headerRow.createCell(2).setCellValue("Numéro de téléphone");
                headerRow.createCell(3).setCellValue("Email");
                headerRow.createCell(4).setCellValue("Type de partenaire");
                headerRow.createCell(5).setCellValue("Montant");
                headerRow.createCell(6).setCellValue("Type d'équipements");
                headerRow.createCell(7).setCellValue("Quantité");

                int rowNum = 1;
                for (Partenaire partenaire : mylistview.getItems()) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(partenaire.getNom());
                    row.createCell(1).setCellValue(partenaire.getPrenom());
                    row.createCell(2).setCellValue(partenaire.getNum_tel());
                    row.createCell(3).setCellValue(partenaire.getEmail());
                    row.createCell(4).setCellValue(partenaire.getType_partenaire());
                    row.createCell(5).setCellValue(partenaire.getMontant());
                    row.createCell(6).setCellValue(partenaire.getType_equipement());
                    row.createCell(7).setCellValue(partenaire.getQuantite());
                }

                try (FileOutputStream outputStream = new FileOutputStream(file)) {
                    workbook.write(outputStream);
                }

                workbook.close();

                System.out.println("Excel file generated successfully!");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void search(MouseEvent event) {
        // Implement your search logic here
    }

    @FXML
    void selectItem(MouseEvent event) {
        // Implement your item selection logic here
    }
}
