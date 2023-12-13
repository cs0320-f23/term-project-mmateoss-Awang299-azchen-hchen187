package edu.brown.cs.student.main.server;

import static spark.Spark.after;

import edu.brown.cs.student.main.server.handlers.AddDislikedSongsHandler;
import edu.brown.cs.student.main.server.handlers.AddInputSongsHandler;
import edu.brown.cs.student.main.server.handlers.LyricsHandler;
import edu.brown.cs.student.main.server.handlers.RecommendationHandler;
import edu.brown.cs.student.main.server.handlers.TokenHandler;
import edu.brown.cs.student.main.server.lyrics.data.LyricsData;
import edu.brown.cs.student.main.server.spotify.tokens.TokenGenerator;
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
    LyricsData lyricsData = new LyricsData();

    // Initializing Spark get handlers
    Spark.get("recommendation", new RecommendationHandler(data));
    Spark.get("token", new TokenHandler(generator));
    Spark.get("addInputSongs", new AddInputSongsHandler());
    Spark.get("addDislikedSongs", new AddDislikedSongsHandler());
    Spark.get("lyrics", new LyricsHandler(lyricsData));

    Spark.init();
    Spark.awaitInitialization();

    // Notice this link alone leads to a 404... Why is that?
    System.out.println("Server started at http://localhost:" + port);

    LyricsData data1 = new LyricsData();
    try {
      for (String[] arr : data1.getLyrics("2i2gDpKKWjvnRTOZRhaPh2")) {
        System.out.println(arr[0] + "  " + arr[1]);
      }
      System.out.println("Finished.");

    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }

  }
}
