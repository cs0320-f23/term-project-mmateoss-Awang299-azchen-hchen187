package edu.brown.cs.student.main.server.translate.data;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import edu.brown.cs.student.main.server.translate.records.TranslateResult;
import edu.brown.cs.student.main.server.translate.records.AzureRecords.TranslateInfo;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import javax.print.attribute.SetOfIntegerSyntax;

/**
 * Class that will be used to get the needed data from the spotify API, allowing
 * our server to use it accordingly.
 */
public class LibreTranslateData implements ITranslateData {

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
        public TranslateResult getTranslation(String text, String fromLanguage, String toLanguage)
                        throws IOException, InterruptedException, URISyntaxException {
                if (fromLanguage.equals(toLanguage)) {
                        return new TranslateResult(text, fromLanguage);
                }

                HttpRequest buildRequest = HttpRequest.newBuilder()
                                .uri(new URI(
                                                "http://localhost:5000/translate"))
                                .header("Content-Type", "application/json")
                                .POST(HttpRequest.BodyPublishers
                                                .ofString("{\"q\": \"" + text + "\",\"source\": \"" + fromLanguage
                                                                + "\",\"target\": \"" + toLanguage + "\"}"))
                                .build();
                // building a response with the HttpRequest
                HttpResponse<String> response = HttpClient.newBuilder()
                                .build()
                                .send(buildRequest, HttpResponse.BodyHandlers.ofString());

                // using Moshi to turn it into a Map<String, Object> object
                Moshi moshi = new Moshi.Builder().build();
                JsonAdapter<Map<String, String>> dataAdapter = moshi
                                .adapter(Types.newParameterizedType(Map.class, String.class, String.class));
                Map<String, String> body = dataAdapter.fromJson(response.body());
                return new TranslateResult(body.get("translatedText"), fromLanguage);
        }
}