package edu.brown.cs.student.main.server.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;
import com.squareup.moshi.Moshi;

import edu.brown.cs.student.main.server.lyrics.data.LyricsData;
import spark.Request;
import spark.Response;
import spark.Route;

public class LyricsHandler implements Route {
    private LyricsData lyricsData;

    public LyricsHandler(LyricsData lyricsData) {
        this.lyricsData = lyricsData;
    }

    public LyricsHandler() {
        this.lyricsData = new LyricsData();
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        Map<String, Object> responseMap = new HashMap<String, Object>();
        Moshi moshi = new Moshi.Builder().build();
        try {
            String spotifyTrackID = request.queryParams("SpotifyTrackID"); // Required
            String language = request.queryParams("language"); // Optional
            if (spotifyTrackID == null) {
                responseMap.put("Result", "Error");
                responseMap.put("Message", "no track ID provided");
                return moshi.adapter(Map.class).toJson(responseMap);
            }

            // convert language to ISO 639-1 code
            String abbrevLanguage;
            if (language == null) { // Set default language to English
                if (request.queryParams().size() != 1) {
                    responseMap.put("Result", "Error");
                    responseMap.put("Message", "invalid parameters provided");
                    return moshi.adapter(Map.class).toJson(responseMap);
                }
                abbrevLanguage = "en";
            } else {
                if (request.queryParams().size() != 2) {
                    responseMap.put("Result", "Error");
                    responseMap.put("Message", "invalid parameters provided");
                    return moshi.adapter(Map.class).toJson(responseMap);
                }
                abbrevLanguage = this.getLanguageCode(language);
            }

            ArrayList<String[]> defaultLyrics = this.lyricsData.getLyrics(spotifyTrackID);
            responseMap.put("Result", "Success");
            responseMap.put("Message", defaultLyrics);
            return moshi.adapter(Map.class).toJson(responseMap);

        } catch (Exception e) {
            responseMap.put("Result", "Error");
            responseMap.put("Message", e.getMessage());
            return moshi.adapter(Map.class).toJson(responseMap);
        }
    }

    /**
     * 
     * @param language- name of the language
     * @return String representing the ISO 639-1 of the language
     * @throws Exception if no code is found
     */
    private String getLanguageCode(String language) throws Exception {
        Locale[] locales = Locale.getAvailableLocales();
        for (Locale locale : locales) {
            if (language.equalsIgnoreCase(locale.getDisplayLanguage())) {
                return locale.getLanguage();
            }
        }
        throw new Exception("language doesn't exist");
    }

}
