package sample.pidevjava.controller;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import java.awt.image.BufferedImage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class QRCodeScanner {

    private Webcam webcam;
    private ScheduledExecutorService executor;

    public void startCamera() {
        webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.VGA.getSize());
        webcam.open();

        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            BufferedImage image = webcam.getImage();
            decodeQRCode(image);
        }, 0, 33, TimeUnit.MILLISECONDS);
    }

    public void stopCamera() {
        executor.shutdown();
        webcam.close();
    }

    private void decodeQRCode(BufferedImage image) {
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
            Result result = new MultiFormatReader().decode(bitmap);
            String text = result.getText();
            System.out.println("QR code text: " + text);
            // You can do something with the decoded text here
        } catch (NotFoundException e) {
            // QR code not found in the image
        }
    }
}
