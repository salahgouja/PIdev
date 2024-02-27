package sample.pidevjava.controller;

import java.util.Arrays;
import java.util.List;

public class BadWords {

    private static final List<String> BAD_WORDS = Arrays.asList("badword1", "badword2", "badword3");

    public static String filterBadWords(String text) {
        text = text.toLowerCase();
        for (String word : BAD_WORDS) {
            text = text.replaceAll("(?i)" + word.toLowerCase(), "*".repeat(word.length()));
        }
        return text;
    }
}


