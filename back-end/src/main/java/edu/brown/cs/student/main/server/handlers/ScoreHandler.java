package edu.brown.cs.student.main.server.handlers;

import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.server.lyrics.data.ILyricsData;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
// import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
// import org.deeplearning4j.models.word2vec.WordVectors;
import org.apache.commons.text.similarity.CosineSimilarity;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * This is the ScoreHandler class. It takes care of everything that has to do
 * with the score
 * endpoint getting called.
 */
public class ScoreHandler implements Route {
  private ILyricsData lyricsData;

  public ScoreHandler(ILyricsData lyricsData) {
    this.lyricsData = lyricsData;
  }

  @Override
  public Object handle(Request req, Response res) {
    Moshi moshi = new Moshi.Builder().build();
    Set<String> params = req.queryParams();
    // String id = req.queryParams("spotifyID");
    String lineCountString = req.queryParams("lineCount");
    String correctWord = req.queryParams("correctWord");
    String guessWord = req.queryParams("guessWord");
    // String lineIndex = req.queryParams("line");

    // defensive programming, checking that everything that needed to be inputted
    // was inputted
    if (params.size() != 3 || lineCountString == null || correctWord == null || guessWord == null) {
      Map<String, Object> responseMap = new HashMap<>();
      responseMap.put("Result", "Error");
      responseMap.put(
          "Message",
          "please ensure that that you passed in a spotifyID, correctWord, and guessWord");
      return moshi.adapter(Map.class).toJson(responseMap);
    }

    try {
      int lineCount = Integer.parseInt(lineCountString);
      correctWord = correctWord.toLowerCase().trim();
      guessWord = guessWord.toLowerCase().trim();
      double maxScore = 1000 / lineCount;
      double score = (1.0
          - ((double) LevenshteinDistance(correctWord, guessWord)
              / Math.max(correctWord.length(), guessWord.length())))
          * maxScore;

      Map<String, Object> scoreMap = new HashMap<>();
      scoreMap.put("score", score);
      scoreMap.put("maxScore", maxScore);
      Map<String, Object> responseMap = new HashMap<>();
      responseMap.put("Result", "Success");
      responseMap.put("Message", scoreMap);
      // responseMap.put("lineCount", lineCount);
      return moshi.adapter(Map.class).toJson(responseMap);
    } catch (Exception e) {
      Map<String, Object> responseMap = new HashMap<>();
      responseMap.put("Result", "Error");
      responseMap.put("Message", e.getMessage());
      return moshi.adapter(Map.class).toJson(responseMap);
    }
  }

  /**
   * Learn more about Levenshtein's Distance here:
   * https://leetcode.com/problems/edit-distance/solutions/3230662/clean-codes-full-explanation-dynamic-programming-c-java-python3/
   *
   * @param word1- String of word 1
   * @param word2- String of word 2
   * @return integer representing the Levenshtein Distance between two strings
   */
  private int LevenshteinDistance(String word1, String word2) {
    if (word1.equals(word2)) {
      return 0;
    }
    final int m = word1.length(); // first word length
    final int n = word2.length(); // / second word length
    // dp[i][j] := min # of operations to convert word1[0..i) to word2[0..j)
    int[][] dp = new int[m + 1][n + 1];

    for (int i = 1; i <= m; ++i)
      dp[i][0] = i;

    for (int j = 1; j <= n; ++j)
      dp[0][j] = j;

    for (int i = 1; i <= m; ++i)
      for (int j = 1; j <= n; ++j)
        if (word1.charAt(i - 1) == word2.charAt(j - 1))
          dp[i][j] = dp[i - 1][j - 1];
        else
          dp[i][j] = Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1]))
              + 1; // replace //delete
    // //insert

    return dp[m][n];
  }

  /**
   * Calculates the similarity between two words using vector embeddings and
   * cosine similarity.
   */
  // private int CosineSimilarity(String word1, String word2) {
  // WordVectors wordVectors =
  // WordVectorSerializer.readWord2VecModel("path/to/word2vec/model.bin");

  // CosineSimilarity cosineSimilarity = new CosineSimilarity();
  // double similarity = cosineSimilarity.cosineSimilarity(
  // wordVectors.getWordVector(word1),
  // wordVectors.getWordVector(word2));
  // }
}
