package edu.brown.cs.student.main.server;

import static spark.Spark.after;

import com.beust.ah.A;

import edu.brown.cs.student.main.server.CachedSpotifyData;
import edu.brown.cs.student.main.server.audioRecognition.audioData.AudioData;
import edu.brown.cs.student.main.server.handlers.AudioTextHandler;
import edu.brown.cs.student.main.server.handlers.GetSongHandler;
import edu.brown.cs.student.main.server.handlers.AddDislikedSongsHandler;
import edu.brown.cs.student.main.server.handlers.AddInputSongsHandler;
import edu.brown.cs.student.main.server.handlers.LyricsHandler;
import edu.brown.cs.student.main.server.handlers.RecommendationHandler;
import edu.brown.cs.student.main.server.handlers.ScoreHandler;
import edu.brown.cs.student.main.server.handlers.TokenHandler;
import edu.brown.cs.student.main.server.lyrics.data.LyricsData;
import edu.brown.cs.student.main.server.spotify.tokens.TokenGenerator;
import edu.brown.cs.student.main.server.translate.data.LibreTranslateData;
import spark.Spark;

/**
 * Top-level class for the server. Contains the main() method which starts Spark
 * and runs
 * the various handlers. We have four endpoints.
 */
public class Server {

  /**
   * Main method of the server, which starts up the server when run.
   *
   * @param args arguments that can be inputted when running main.
   */
  public static void main(String[] args) {
    // gotten from gearup
    int port = 3232;
    Spark.port(port);

    after((request, response) -> {
      response.header("Access-Control-Allow-Origin", "*");
      response.header("Access-Control-Allow-Methods", "*");
    });

    // Creating variables that need to be passed into
    CachedSpotifyData data = new CachedSpotifyData();
    TokenGenerator generator = new TokenGenerator();
    AudioData audioData = new AudioData();
    LyricsData lyricsData = new LyricsData();
    LibreTranslateData translateData = new LibreTranslateData();

    // Initializing Spark get handlers
    Spark.get("recommendation", new RecommendationHandler(data, lyricsData));
    Spark.get("token", new TokenHandler(generator));
    Spark.get("getSongs", new GetSongHandler(data, lyricsData));
    Spark.get("addInputSongs", new AddInputSongsHandler());
    Spark.get("addDislikedSongs", new AddDislikedSongsHandler());
    Spark.post("audioText", new AudioTextHandler(audioData));
    Spark.get("getLyrics", new LyricsHandler(lyricsData, translateData));
    Spark.get("getScore", new ScoreHandler(lyricsData));

    Spark.init();
    Spark.awaitInitialization();

    // Notice this link alone leads to a 404... Why is that?
    System.out.println("Server started at http://localhost:" + port);

    LibreTranslateData data1 = new LibreTranslateData();
    try {
      String res = data1.getTranslation("I am absolutely super cool", "en", "fr");
      System.out.println(res);
      System.out.println("done");
    } catch (Exception e) {
      System.out.println("Invalid BRUH");
    }

    // LyricsData data1 = new LyricsData();
    // try {
    // for (String[] arr : data1.getLyrics("2i2gDpKKWjvnRTOZRhaPh2")) {
    // System.out.println(arr[0] + " " + arr[1]);
    // }
    // System.out.println("Finished.");

    // } catch (Exception e) {
    // System.out.println("Error: " + e.getMessage());
    // }
  }
}
