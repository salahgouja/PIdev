package sample.pidevjava.model;

import sample.pidevjava.Services.EquipementService;
import sample.pidevjava.controller.LocationEquipementController;
import sample.pidevjava.Services.Smstwilio;

import java.util.List;

public class TwilioSmsSender {
    EquipementService equipementService = new EquipementService();
    LocationEquipementController locationEquipementController = new LocationEquipementController(equipementService);
    Smstwilio twilioService;

    public static final String ACCOUNT_SID = "AC0062ce3b98f679f6c6843e153073d3f0";
    public static final String AUTH_TOKEN = "966e401fcca534a439cac425df826a2f";
    public static final String TWILIO_PHONE_NUMBER = "+12565673526";

    public TwilioSmsSender() {
        this.twilioService = new Smstwilio(ACCOUNT_SID, AUTH_TOKEN, TWILIO_PHONE_NUMBER);
    }

    public void envoyerSMS() {
        int id_reservation = equipementService.getDerniereReservationId();
        List<Equipement> equipements = equipementService.getAllEquipement();
        List<EquipementLocation> locations = equipementService.getLocationEquipementsByReservationId(id_reservation);

        StringBuilder smsBody = new StringBuilder("Détails de la réservation :\n");
        for (EquipementLocation location : locations) {
            Equipement equipement = locationEquipementController.getEquipementById(location.getId_equipemment(), equipements);
            smsBody.append("Equipement: ").append(equipement.getNom_equipement()).append("\n");
        }

        float prixTotal = locationEquipementController.calculerPrixTotalLocation(locations);
        smsBody.append("Prix total de la réservation : ").append(prixTotal).append(" euros");

        try {
            twilioService.sendSMS("+21624391014", smsBody.toString());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public void envoyerSMS(String destinataire, String message) {
        try {
            twilioService.sendSMS(destinataire, message);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

}
