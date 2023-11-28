package edu.brown.cs.student.Tests.server.csv;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.server.csv.CsvLoader;
import edu.brown.cs.student.main.server.csv.LoadCsvHandler;
import edu.brown.cs.student.main.server.csv.SearchCsvHandler;
import edu.brown.cs.student.main.server.csv.ViewCsvHandler;
import edu.brown.cs.student.main.server.webapi.BroadbandHandler;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

public class ViewCsvHandlerTests {
  /**
   * Method that is ran once at the beginning. Gotten from the gearup.
   */
  @BeforeAll
  public static void setup_before_everything() {

    // Set the Spark port number
    Spark.port(0);
    Logger.getLogger("").setLevel(Level.WARNING); // empty name = root logger
  }

  /**
   * Method gotten from the gearup, that sets up our server, called before each test is ran.
   */
  @BeforeEach
  public void setup() {
    CsvLoader csvLoader = new CsvLoader();

    // In fact, restart the entire Spark server for every test!
    Spark.get("loadcsv", new LoadCsvHandler(csvLoader));
    Spark.get("viewcsv", new ViewCsvHandler(csvLoader));
    Spark.get("searchcsv", new SearchCsvHandler(csvLoader));
    Spark.get("broadband", new BroadbandHandler());

    Spark.init();
    Spark.awaitInitialization(); // don't continue until the server is listening
  }

  /**
   * Method gotten from the gearup that stops the connection to the server.
   */
  @AfterEach
  public void teardown() {
    // Gracefully stop Spark listening on both endpoints
    Spark.unmap("/loadcsv");
    Spark.unmap("/viewcsv");
    Spark.unmap("/searchcsv");
    Spark.unmap("/broadband");
    Spark.awaitStop(); // don't proceed until the server is stopped
  }

  /**
   * Method gotten from the gear up that allows us to try an API request
   *
   * @param apiCall the endpoint you are calling to.
   * @param queryParams the query parameters that you want to use.
   *
   * @return HttpResponse that can be used to set up a connection with an API.
   * @throws IOException exception where it failed to read/open information.
   * @throws InterruptedException exception where connection to API is interrupted.
   * @throws URISyntaxException exception where URI syntax is incorrect.
   */
  static private HttpResponse<String> tryRequest(String apiCall, Map<String, String> queryParams)
      throws IOException, InterruptedException, URISyntaxException {

    StringBuilder queryString = new StringBuilder();

    if (queryParams != null && !queryParams.isEmpty()) {
      queryString.append("?");

      for (Map.Entry<String, String> entry : queryParams.entrySet()) {
        String queryName = entry.getKey();
        String queryValue = entry.getValue();

        queryString.append(queryName).append("=").append(queryValue).append("&");
      }

      // Remove the trailing "&"
      queryString.deleteCharAt(queryString.length() - 1);
    }

    // Create an HTTP client
    HttpClient client = HttpClient.newHttpClient();

    // Build the HTTP request
    HttpRequest request = HttpRequest.newBuilder()
        .uri(new URI("http://localhost:" + Spark.port() + "/" + apiCall + queryString))
        .GET()  // This is optional since GET is the default method.
        .build();

    // Send the request and get the response
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    return response;
  }

  /**
   * Method that tests that ViewCsvHandler will work with the correct parameter names and values
   *
   * @throws URISyntaxException exception where URI syntax is incorrect.
   * @throws IOException exception where it failed to read/open information.
   * @throws InterruptedException exception where connection to API is interrupted.
   */
  @Test
  public void testValidParameters() throws IOException, URISyntaxException, InterruptedException {
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("filePath", "data/census/RI%20Income%20Census.csv");
    queryParams.put("hasHeader", "true");
    HttpResponse<String> loadResponse = tryRequest("loadcsv", queryParams);

    queryParams.clear();
    HttpResponse<String> searchResponse = tryRequest("viewcsv", queryParams);

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);
    Map<String, Object> responseBody = jsonAdapter.fromJson(searchResponse.body());

    String dataFirst = "Rhode Island";

    assertEquals("success", responseBody.get("result"));
    assertEquals(dataFirst, ((List<List<String>>) responseBody.get("data")).get(0).get(0));
  }

  /**
   * Method that tests that ViewCsvHandler will return an error when given extra parameters
   *
   * @throws URISyntaxException exception where URI syntax is incorrect.
   * @throws IOException exception where it failed to read/open information.
   * @throws InterruptedException exception where connection to API is interrupted.
   */
  @Test
  public void testExtraParameters() throws IOException, URISyntaxException, InterruptedException {
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("filePath", "data/census/RI%20Income%20Census.csv");
    queryParams.put("hasHeader", "true");
    HttpResponse<String> loadResponse = tryRequest("loadcsv", queryParams);

    queryParams.clear();
    queryParams.put("target", "wrong-parameter");
    HttpResponse<String> searchResponse = tryRequest("viewcsv", queryParams);

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);
    Map<String, Object> responseBody = jsonAdapter.fromJson(searchResponse.body());

    assertEquals("error_bad_request", responseBody.get("result"));
    assertEquals("No query parameters allowed", responseBody.get("message"));
  }

  /**
   * Method that tests that ViewCsvHandler will return an error when viewing before loading in
   * a csv file
   *
   * @throws URISyntaxException exception where URI syntax is incorrect.
   * @throws IOException exception where it failed to read/open information.
   * @throws InterruptedException exception where connection to API is interrupted.
   */
  @Test
  public void testViewBeforeLoading() throws IOException, URISyntaxException, InterruptedException {
    Map<String, String> queryParams = new HashMap<>();
    HttpResponse<String> searchResponse = tryRequest("viewcsv", queryParams);

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);
    Map<String, Object> responseBody = jsonAdapter.fromJson(searchResponse.body());

    assertEquals("error_data_source", responseBody.get("result"));
  }
}
