package sample.pidevjava.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.scene.image.Image;


import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

public class QRCodeGenerator {

    public File generateQRCode(String email, String password) {
        // Encode the email and password as a single string
        String data = email + ":" + password;

        // Set up the QR code writer
        QRCodeWriter writer = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1);

        try {
            // Create the QR code matrix
            BitMatrix matrix = writer.encode(data, BarcodeFormat.QR_CODE, 200, 200, hints);

            // Create the file to save the QR code image
            File qrCodeFile = new File("qrcode.png");

            // Convert the matrix to an image and save it to the file
            MatrixToImageWriter.writeToPath(matrix, "PNG", qrCodeFile.toPath());

            return qrCodeFile;
        } catch (WriterException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Image generateQRCodeImage(String qrCodeData, int qrCodeSize) {
        try {
            Map<EncodeHintType, Object> hintMap = new HashMap<>();
            hintMap.put(EncodeHintType.MARGIN, 0);
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix byteMatrix = qrCodeWriter.encode(qrCodeData, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize, hintMap);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                ImageIO.write(toBufferedImage(byteMatrix), "png", out);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
            return new Image(in);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static java.awt.image.BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return image;
    }



}
