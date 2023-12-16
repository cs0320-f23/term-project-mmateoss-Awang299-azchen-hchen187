package edu.brown.cs.student.main.server.handlers;

import com.squareup.moshi.Moshi;

import edu.brown.cs.student.main.server.lyrics.data.ILyricsData;
import edu.brown.cs.student.main.server.spotify.data.IData;

import edu.brown.cs.student.main.server.spotify.records.recommendationRecords.Recommendation;
import edu.brown.cs.student.main.server.spotify.records.recommendationRecords.TrackProps;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * This is the ScoreHandler class. It takes care of everything that has
 * to do with the score endpoint getting called.
 */
public class ScoreHandler implements Route {
    private ILyricsData lyricsData;

    public ScoreHandler(ILyricsData lyricsData) {
        this.lyricsData = lyricsData;
    }

    @Override
    public Object handle(Request req, Response res) throws Exception {
        Moshi moshi = new Moshi.Builder().build();
        Set<String> params = req.queryParams();
        String id = req.queryParams("spotifyID");
        String correctWord = req.queryParams("correctWord");
        String guessWord = req.queryParams("guessWord");
        String lineIndex = req.queryParams("line");

        // defensive programming, checking that everything that needed to be inputted
        // was inputted
        if (params.size() != 4 || id == null || correctWord == null || guessWord == null) {
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("Result", "Error");
            responseMap.put("Message", "please ensure that that you passed in a spotifyID, correctWord, and guessWord");
            return moshi.adapter(Map.class).toJson(responseMap);
        }
        ArrayList<String[]> lyrics = this.lyricsData.getLyrics(id);
        int lineCount = lyrics.size();

        double score = (1
                - ((double) LevenshteinDistance(correctWord, guessWord) /
                        Math.max(correctWord.length(), guessWord.length())))
                * 100 / lineCount;
        // double score = LevenshteinDistance(correctWord, guessWord);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("Result", "Success");
        responseMap.put("Message", score);
        responseMap.put("lineCount", lineCount);
        return moshi.adapter(Map.class).toJson(responseMap);
    }

    /**
     * 
     * @param word1- String of word 1
     * @param word2- String of word 2
     * @return integer representing the Levenshtein Distance between two strings
     */
    private int LevenshteinDistance(String word1, String word2) {
        final int m = word1.length();// first word length
        final int n = word2.length();/// second word length
        // dp[i][j] := min # of operations to convert word1[0..i) to word2[0..j)
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 1; i <= m; ++i)
            dp[i][0] = i;

        for (int j = 1; j <= n; ++j)
            dp[0][j] = j;

        for (int i = 1; i <= m; ++i)
            for (int j = 1; j <= n; ++j)
                if (word1.charAt(i - 1) == word2.charAt(j - 1))// same characters
                    dp[i][j] = dp[i - 1][j - 1];// no operation
                else
                    dp[i][j] = Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1])) + 1; // replace //delete
                                                                                                     // //insert

        return dp[m][n];
    }
    // if (x.length() == 0) {
    // return y.length();
    // } else if (y.length() == 0) {
    // return x.length();
    // }
    // int[][] dp = new int[x.length()][y.length()];

    // for (int i = 0; i < x.length(); i++) {
    // for (int j = 0; j < y.length(); j++) {
    // dp[i][j] = Math.max(x.length(), y.length());
    // }
    // }

    // for (int i = 0; i < x.length(); i++) {
    // for (int j = 0; j < y.length(); j++) {
    // if (i != 0) {
    // dp[i][j] = Math.min(dp[i][j], dp[i - 1][j]);
    // }
    // if (j != 0) {
    // dp[i][j] = Math.min(dp[i][j], dp[i][j - 1]);
    // }
    // if (i != 0 && j != 0) {
    // dp[i][j] = Math.min(dp[i][j], dp[i - 1][j - 1]);
    // }
    // if (x.charAt(i) != y.charAt(j)) {
    // dp[i][j] += 1;
    // }
    // }
    // }

    // return dp[x.length() - 1][y.length() - 1];
    // }

}