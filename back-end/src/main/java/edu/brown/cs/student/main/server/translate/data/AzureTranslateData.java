package edu.brown.cs.student.main.server.translate.data;

import java.io.IOException;
import java.net.URISyntaxException;

public class AzureTranslateData implements ITranslateData {
    /**
     * 
     * @param text-         String to be translated
     * @param fromLanguage- String representing the ISO 639-1 code of the language
     *                      to be translated from
     * @param toLanguage-   String representing the ISO 639-1 code of the language
     *                      to be translated to
     * @return String containing the translated lyrics.
     * @throws IOException
     * @throws InterruptedException
     * @throws URISyntaxException
     */
    @Override
    public String getTranslation(String text, String fromLanguage, String toLanguage) throws Exception {
        // if (fromLanguage.equals(toLanguage)) {
        // return text;
        // }

        // String subscriptionKey = "f7b0b0c4c9a34c6e9f8f8b6b6b6b6b6b";
        // String endpoint = "https://api.cognitive.microsofttranslator.com/";

        // // Instantiates the Java client
        // AzureTranslateClient client = new AzureTranslateClient(endpoint,
        // subscriptionKey);

        // // Translates text into German
        // String result = client.translate(text, fromLanguage, toLanguage);

        // return result;
        return null;
    }

}
