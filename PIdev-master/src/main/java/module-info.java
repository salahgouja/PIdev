module sample.pidevjava {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.persistence;
    requires mysql.connector.j;
    requires de.jensd.fx.glyphs.fontawesome;


    opens sample.pidevjava to javafx.fxml;
    exports sample.pidevjava;
    exports sample.pidevjava.controller;
    opens sample.pidevjava.controller to javafx.fxml;
    opens sample.pidevjava.model to javafx.base;

}