package edu.brown.cs.student.main.server;

import static spark.Spark.after;

import edu.brown.cs.student.main.server.csv.CsvLoader;
import edu.brown.cs.student.main.server.csv.LoadCsvHandler;
import edu.brown.cs.student.main.server.csv.SearchCsvHandler;
import edu.brown.cs.student.main.server.csv.ViewCsvHandler;
import edu.brown.cs.student.main.server.webapi.BroadbandHandler;
import spark.Spark;


/**
 * Top-level class for the server. Contains the main() method which starts Spark and runs
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

    // Initializing Spark get handlers
//    CsvLoader csvLoader = new CsvLoader();

//    Spark.get("loadcsv", new LoadCsvHandler(csvLoader));
//    Spark.get("viewcsv", new ViewCsvHandler(csvLoader));
//    Spark.get("searchcsv", new SearchCsvHandler(csvLoader));
//    Spark.get("broadband", new BroadbandHandler());
//    Spark.get("spotify" new SpotifyHandler());

    Spark.init();
    Spark.awaitInitialization();

    // Notice this link alone leads to a 404... Why is that?
    System.out.println("Server started at http://localhost:" + port);
  }
}
