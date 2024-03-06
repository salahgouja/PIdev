package sample.pidevjava.controller;


import com.aspose.pdf.*;
import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static com.aspose.pdf.internal.doc.ml.WborderProperty.WborderValues.Gradient;

public class PDFCreator {

    public static void createPdfRefused() {
        try {
            Document document = new Document();
            Page page = document.getPages().add();
            page.getParagraphs().add(new TextFragment("!"));
            document.save("HelloWorld_out.pdf");
            System.out.println("PDF created successfully ");
        } catch (Exception e) {
            System.err.println("File not found: " + e.getMessage());
        }
    }

//    public static void createPdfAccepted(javafx.scene.image.Image qrCodeImage) {
//        try {
//            // Convert JavaFX Image to BufferedImage
//            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(qrCodeImage, null);
//
//            // Convert BufferedImage to byte array
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            ImageIO.write(bufferedImage, "png", outputStream);
//            byte[] imageBytes = outputStream.toByteArray();
//
//            // Create Aspose.PDF Image object
//            com.aspose.pdf.Image asposePdfImage = new com.aspose.pdf.Image();
//            asposePdfImage.setImageStream(new ByteArrayInputStream(imageBytes));
//
//            // Create PDF document
//            Document document = new Document();
//            Page page = document.getPages().add();
//
//            // Add text to the page
//            page.getParagraphs().add(new TextFragment("Hello, this is your QR code:"));
//
//            // Add QR code image to the page
//            page.getParagraphs().add(asposePdfImage);
//
//            // Save the document to a PDF file
//            document.save("HelloWorld_out.pdf");
//            document.close();
//
//            System.out.println("PDF created successfully ");
//        } catch (Exception e) {
//            System.err.println("Error creating PDF: " + e.getMessage());
//        }
//    }
//
public static void createPdfAccepted(javafx.scene.image.Image qrCodeImage) {
    try {
            // Convert JavaFX Image to BufferedImage
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(qrCodeImage, null);

            // Convert BufferedImage to byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", outputStream);
            byte[] imageBytes = outputStream.toByteArray();

            // Create Aspose.PDF Image object
            com.aspose.pdf.Image asposePdfImage = new com.aspose.pdf.Image();
            asposePdfImage.setImageStream(new ByteArrayInputStream(imageBytes));

        // Create PDF document
        Document document = new Document();
        Page page = document.getPages().add();

        // Add title with gradient background

        TextFragment titleFragment = new TextFragment(" réponse du complexe sportif concernant votre \n demande de participation à un événement");
        titleFragment.setPosition(new Position(70, 720));
        titleFragment.getTextState().setFont(FontRepository.findFont("Arial"));
        titleFragment.getTextState().setFontSize(18);

        titleFragment.getTextState().setForegroundColor(Color.getBlack());
        titleFragment.setHorizontalAlignment(HorizontalAlignment.Center);
        page.getParagraphs().add(titleFragment);

        // Add subject
        TextFragment subjectFragment = new TextFragment("Subject:");
        subjectFragment.getPosition().setXIndent(50);
        subjectFragment.getPosition().setYIndent(670);
        subjectFragment.getTextState().setFont(FontRepository.findFont("Arial"));
        subjectFragment.getTextState().setFontSize(12);

        subjectFragment.getTextState().setForegroundColor(Color.getBlack());
        page.getParagraphs().add(subjectFragment);

        // Add paragraph
        TextFragment paragraphFragment = new TextFragment("Nous vous remercions vivement pour votre intérêt à participer à notre prochain événement dans notre complexe sportif. \n" +
                "Nous sommes ravis d'accepter votre demande de participation et nous sommes convaincus que votre implication ajoutera une valeur significative à l'expérience globale de l'événement. \n" +
                "ous avons hâte de collaborer avec vous pour créer un événement mémorable et enrichissant pour tous les participants.");
        paragraphFragment.setPosition(new Position(50, 640));
        paragraphFragment.getTextState().setFont(FontRepository.findFont("Arial"));
        paragraphFragment.getTextState().setFontSize(12);
        paragraphFragment.getTextState().setForegroundColor(Color.getBlack());
        page.getParagraphs().add(paragraphFragment);

        // Add QR code image to the page
        asposePdfImage.setFixWidth(bufferedImage.getWidth());
        asposePdfImage.setFixHeight(bufferedImage.getHeight());
        page.getParagraphs().add(asposePdfImage);

        // Save the document to a PDF file
        document.save("HelloWorld_out.pdf");
        document.close();

        System.out.println("PDF created successfully ");
    } catch (Exception e) {
        System.err.println("Error creating PDF: " + e.getMessage());
    }
}
}