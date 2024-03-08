module sample.pidevjava {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.persistence;
    requires mysql.connector.j;
    requires com.jfoenix;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires java.desktop;
    requires javafx.swing;
    requires mail;
    requires webcam.capture;
    requires org.apache.pdfbox;
    requires twilio;


    requires aspose.pdf;
    requires de.jensd.fx.glyphs.fontawesome;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    requires MaterialFX;

    opens sample.pidevjava to javafx.fxml;

    exports sample.pidevjava;
    exports sample.pidevjava.controller;

    opens sample.pidevjava.controller to javafx.fxml;
    exports sample.pidevjava.model;
    opens sample.pidevjava.model to javafx.base, javafx.fxml;
    exports sample.pidevjava.Services;
    opens sample.pidevjava.Services to javafx.fxml;


}