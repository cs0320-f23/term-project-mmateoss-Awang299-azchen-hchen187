package edu.brown.cs.student.main.server;

import static spark.Spark.after;

import edu.brown.cs.student.main.server.spotify.data.CachedSpotifyData;
import edu.brown.cs.student.main.server.spotify.data.SpotifyData;
import edu.brown.cs.student.main.server.spotify.handlers.AddDislikedSongsHandler;
import edu.brown.cs.student.main.server.spotify.handlers.AddInputSongsHandler;
import edu.brown.cs.student.main.server.spotify.handlers.GenerateNewPlaylistHandler;
import edu.brown.cs.student.main.server.spotify.handlers.RecommendationHandler;
import edu.brown.cs.student.main.server.spotify.handlers.TokenHandler;
import edu.brown.cs.student.main.server.webapi.BroadbandHandler;
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
    CachedSpotifyData data = new CachedSpotifyData();

    // Initializing Spark get handlers
    Spark.get("recommendation", new RecommendationHandler(data));
    Spark.get("token", new TokenHandler());
    Spark.get("addInputSongs", new AddInputSongsHandler());
    Spark.get("addDislikedSongs", new AddDislikedSongsHandler());

    Spark.init();
    Spark.awaitInitialization();

    // Notice this link alone leads to a 404... Why is that?
    System.out.println("Server started at http://localhost:" + port);
  }
}
