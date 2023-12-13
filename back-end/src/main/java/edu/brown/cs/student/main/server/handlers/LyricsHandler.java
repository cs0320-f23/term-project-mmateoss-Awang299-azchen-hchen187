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
                responseMap.put("Error Message", "no track ID provided");
                return moshi.adapter(Map.class).toJson(responseMap);
            }

            // convert language to ISO 639-1 code
            String abbrevLanguage;
            if (language == null) { // Set default language to English
                abbrevLanguage = "en";
            } else {
                abbrevLanguage = this.getLanguageCode(language);
            }

            ArrayList<String[]> lyrics = this.lyricsData.getLyrics(spotifyTrackID);

        } catch (Exception e) {
            responseMap.put("Result", "Error");
            responseMap.put("Error Message", e.getMessage());
            return moshi.adapter(Map.class).toJson(responseMap);
        }
        throw new UnsupportedOperationException("Unimplemented method 'handle'");
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
