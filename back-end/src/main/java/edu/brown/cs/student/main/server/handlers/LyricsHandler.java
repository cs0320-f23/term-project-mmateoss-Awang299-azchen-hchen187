package edu.brown.cs.student.main.server.handlers;

import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.server.lyrics.data.ILyricsData;
import edu.brown.cs.student.main.server.lyrics.data.LyricsData;
import edu.brown.cs.student.main.server.translate.data.AzureTranslateData;
import edu.brown.cs.student.main.server.translate.data.ITranslateData;
import edu.brown.cs.student.main.server.translate.data.LibreTranslateData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

public class LyricsHandler implements Route {
    private ILyricsData lyricsData;
    private ITranslateData translateData;

    public LyricsHandler(ILyricsData lyricsData, ITranslateData translateData) {
        this.lyricsData = lyricsData;
        this.translateData = translateData;
    }

    public LyricsHandler() {
        this.lyricsData = new LyricsData();
        this.translateData = new LibreTranslateData();
    }

    @Override
    public Object handle(Request request, Response response) {
        Map<String, Object> responseMap = new HashMap<String, Object>();
        Moshi moshi = new Moshi.Builder().build();
        try {
            String spotifyTrackID = request.queryParams("SpotifyTrackID"); // Required
            String fromLanguage = request.queryParams("fromLanguage"); // Optional, default is English
            String toLanguage = request.queryParams("toLanguage"); // Required

            if (spotifyTrackID == null) {
                responseMap.put("Result", "Error");
                responseMap.put("Message", "no track ID provided");
                return moshi.adapter(Map.class).toJson(responseMap);
            } else if (toLanguage == null) {
                responseMap.put("Result", "Error");
                responseMap.put("Message", "no toLanguage provided");
                return moshi.adapter(Map.class).toJson(responseMap);
            }
            // convert language to ISO 639-1 code
            if (fromLanguage == null) { // Set default language to English
                if (request.queryParams().size() != 2) {
                    responseMap.put("Result", "Error");
                    responseMap.put("Message", "invalid parameters provided");
                    return moshi.adapter(Map.class).toJson(responseMap);
                }
                fromLanguage = "english";
            } else {
                if (request.queryParams().size() != 3) {
                    responseMap.put("Result", "Error");
                    responseMap.put("Message", "invalid parameters provided");
                    return moshi.adapter(Map.class).toJson(responseMap);
                }
                fromLanguage = fromLanguage.toLowerCase();
            }
            toLanguage = toLanguage.toLowerCase();

            // Testing purposes
            // AzureTranslateData data = new AzureTranslateData();
            // System.out.println("fromLanguage: " +
            // data.getLanguageCode(fromLanguage).code());
            // System.out.println("before toLanguage: " + toLanguage);
            // System.out.println("after toLanguage: " +
            // data.getLanguageCode(toLanguage).code());

            int count = 0;
            ArrayList<String[]> defaultLyrics = this.lyricsData.getLyrics(spotifyTrackID);
            // ---------------------------------------------
            // Attempt to batch entire song's lyrics into one string
            // StringBuilder superLine = new StringBuilder("");
            // for (String[] line : defaultLyrics) {
            // // if (line[1].equals("")) {
            // // continue;
            // // }
            // superLine.append(line[1] + ":");
            // }
            // String[] resString = this.translateData.getTranslation(superLine.toString(),
            // fromLanguage, toLanguage)
            // .split(":");
            // for (int i = 0; i < defaultLyrics.size(); i++) {
            // // if (defaultLyrics.get(i)[1].equals("")) {
            // // continue;
            // // }
            // defaultLyrics.get(i)[2] = resString[i];
            // }
            // for (String val : resString) {
            // System.out.println(val);
            // }
            // ---------------------------------------------
            for (String[] line : defaultLyrics) {
                if (line[1].equals("")) {
                    continue;
                }
                count += line[1].length();
                line[2] = this.translateData
                        .getTranslation(line[1].trim(), fromLanguage, toLanguage)
                        .translatedText();
            }
            // defaultLyrics.get(0)[2] =
            // this.translateData.getTranslation(defaultLyrics.get(0)[1], fromLanguage,
            // toLanguage);
            responseMap.put("Result", "Success");
            responseMap.put("Message", defaultLyrics);
            responseMap.put("Count", count);
            return moshi.adapter(Map.class).toJson(responseMap);

        } catch (Exception e) {
            responseMap.put("Result", "Error");
            responseMap.put("Message", e.getMessage());
            return moshi.adapter(Map.class).toJson(responseMap);
        }
    }

    // /**
    // * @param language- name of the language
    // * @return String representing the ISO 639-1 of the language
    // * @throws Exception if no code is found
    // */
    // private String getLanguageCode(String language) throws Exception {
    // Locale[] locales = Locale.getAvailableLocales();
    // for (Locale locale : locales) {
    // if (language.equalsIgnoreCase(locale.getDisplayLanguage())) {
    // System.out.println("toLanguage: " + locale.toLanguageTag());
    // return locale.toLanguageTag();
    // }
    // }
    // throw new Exception("language doesn't exist");
    // }
}
