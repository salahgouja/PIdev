module sample.pidevjava {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.persistence;
    requires mysql.connector.j;
    requires twilio;
    requires java.desktop;
    requires org.apache.pdfbox;


    opens sample.pidevjava to javafx.fxml;
    exports sample.pidevjava;
    exports sample.pidevjava.controller;
    opens sample.pidevjava.controller to javafx.fxml;
    opens sample.pidevjava.model to javafx.base;

}