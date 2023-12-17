package edu.brown.cs.student.main.server.translate.data;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import edu.brown.cs.student.main.server.translate.records.TranslateResult;
import edu.brown.cs.student.main.server.translate.records.AzureRecords.DetectTranslateInfo;
import edu.brown.cs.student.main.server.translate.records.AzureRecords.TranslateInfo;
import edu.brown.cs.student.main.server.translate.records.AzureRecords.Translation;
import io.github.cdimascio.dotenv.Dotenv;

public class AzureTranslateData implements ITranslateData {
    /**
     * 
     * @param text-         String to be translated
     * @param fromLanguage- String representing the ISO 639-1 code of the language
     *                      to be translated from
     * @param toLanguage-   String representing the ISO 639-1 code of the language
     *                      to be translated to
     * @return TranslateResult containing the translated lyrics and fromLanguage.
     * @throws IOException
     * @throws InterruptedException
     * @throws URISyntaxException
     */
    @Override
    public TranslateResult getTranslation(String text, String fromLanguage, String toLanguage) throws Exception {
        if (fromLanguage.equals(toLanguage) || text.equals("")) {
            return new TranslateResult(text, fromLanguage);
        }

        String endpoint = "https://api.cognitive.microsofttranslator.com";
        String route = fromLanguage != null ? "/translate?api-version=3.0&from=" + fromLanguage + "&to=" + toLanguage
                : "/translate?api-version=3.0&to=" + toLanguage;
        String url = endpoint.concat(route);
        Dotenv dotenv = Dotenv.load();
        String key = dotenv.get("AZURE_KEY");

        HttpRequest buildRequest = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Ocp-Apim-Subscription-Key", key)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers
                        .ofString("[{\"Text\": \"" + text + "\"}]"))
                .build();
        // building a response with the HttpRequest
        HttpResponse<String> response = HttpClient.newBuilder()
                .build()
                .send(buildRequest, HttpResponse.BodyHandlers.ofString());

        // using Moshi to turn it into a List<> object depending on if language
        // detection is used or not
        Moshi moshi = new Moshi.Builder().build();
        Translation translation;
        if (fromLanguage != null) {
            JsonAdapter<List<TranslateInfo>> dataAdapter = moshi
                    .adapter(Types.newParameterizedType(List.class, TranslateInfo.class));
            List<TranslateInfo> body = dataAdapter.fromJson(response.body());
            translation = body.get(0).translations().get(0);
        } else { // Not dead code, we are assigning fromLanguage to detected language here
            JsonAdapter<List<DetectTranslateInfo>> dataAdapter = moshi
                    .adapter(Types.newParameterizedType(List.class, DetectTranslateInfo.class));
            List<DetectTranslateInfo> body = dataAdapter.fromJson(response.body());
            translation = body.get(0).translations().get(0);
            fromLanguage = body.get(0).detectedLanguage().language();
        }

        if (translation.to().equals(toLanguage)) {
            return new TranslateResult(translation.text(), fromLanguage);
        } else {
            throw new Exception("Translation failed");
        }

        // JsonAdapter<Map<String, String>> dataAdapter = moshi
        // .adapter(Types.newParameterizedType(Map.class, String.class, String.class));
        // Map<String, String> body = dataAdapter.fromJson(response.body());
        // return body.get("translatedText");
    }

}
