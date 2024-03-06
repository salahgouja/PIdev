package sample.pidevjava.controller;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import sample.pidevjava.interfaces.SMSService;

public class TwilioSMSService implements SMSService {
    // Twilio Account SID and Auth Token
    private static final String ACCOUNT_SID = "your_account_sid";
    private static final String AUTH_TOKEN = "your_auth_token";

    // Twilio phone number (provided by Twilio)
    private static final String TWILIO_PHONE_NUMBER = "your_twilio_phone_number";

    // Initialize Twilio
    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    @Override
    public void sendSMS(String phoneNumber, String message) {
        Message.creator(
                new PhoneNumber(phoneNumber),
                new PhoneNumber(TWILIO_PHONE_NUMBER),
                message
        ).create();
    }
}