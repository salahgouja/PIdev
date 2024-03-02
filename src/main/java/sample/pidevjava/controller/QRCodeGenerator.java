package sample.pidevjava.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;


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
}
