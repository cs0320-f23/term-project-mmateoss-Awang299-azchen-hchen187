package edu.brown.cs.student.Tests.server.webapi;

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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

/**
 * Testing class that tests the functionality of the BroadbandHandler class.
 */
public class BroadbandHandlerTests {


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
   * Method that tests when the correct parameters are inputted into the broadband handler.
   *
   * @throws IOException exception where it failed to read/open information.
   * @throws URISyntaxException exception where URI syntax is incorrect.
   * @throws InterruptedException exception where connection to API is interrupted.
   */
  @Test
  public void testCorrectParameters() throws IOException, URISyntaxException, InterruptedException {
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("county", "Bexar%20County");
    queryParams.put("state", "Texas");
    HttpResponse<String> response = tryRequest("broadband", queryParams);

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);
    Map<String, String> responseBody = jsonAdapter.fromJson(response.body());

    assertEquals("success", responseBody.get("result"));
    assertEquals("87.6", responseBody.get("percentage"));
    assertEquals("Bexar County", responseBody.get("county"));
    assertEquals("Texas", responseBody.get("state"));
  }

  /**
   * Method that tests when there are not enough parameters inputted into the broadband handler.
   *
   * @throws IOException exception where it failed to read/open information.
   * @throws URISyntaxException exception where URI syntax is incorrect.
   * @throws InterruptedException exception where connection to API is interrupted.
   */
  @Test
  public void testMissingParameters() throws IOException, URISyntaxException, InterruptedException {
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("state", "Texas");
    HttpResponse<String> response = tryRequest("broadband", queryParams);

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);
    Map<String, String> responseBody = jsonAdapter.fromJson(response.body());

    assertEquals("error_bad_request", responseBody.get("result"));
  }


  /**
   * Method that tests when the correct parameters are not inputted into the broadband handler.
   *
   * @throws IOException exception where it failed to read/open information.
   * @throws URISyntaxException exception where URI syntax is incorrect.
   * @throws InterruptedException exception where connection to API is interrupted.
   */
  @Test
  public void testIncorrectParameters() throws IOException, URISyntaxException, InterruptedException {
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("notThis", "what");
    queryParams.put("notCorrect", "false");
    HttpResponse<String> response = tryRequest("broadband", queryParams);

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);
    Map<String, String> responseBody = jsonAdapter.fromJson(response.body());

    assertEquals("error_bad_request", responseBody.get("result"));
  }


  /**
   * Method that tests where the query params are null (no params passed in)
   *
   * @throws IOException exception where it failed to read/open information.
   * @throws URISyntaxException exception where URI syntax is incorrect.
   * @throws InterruptedException exception where connection to API is interrupted.
   */
  @Test
  public void testNull() throws IOException, URISyntaxException, InterruptedException {
    HttpResponse<String> response = tryRequest("broadband", null);

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);
    Map<String, String> responseBody = jsonAdapter.fromJson(response.body());

    assertEquals("error_bad_request", responseBody.get("result"));
  }

  /**
   * Method that test when the county inputted does not exist or match one in the census.
   *
   * @throws IOException exception where it failed to read/open information.
   * @throws URISyntaxException exception where URI syntax is incorrect.
   * @throws InterruptedException exception where connection to API is interrupted.
   */
  @Test
  public void testNotCounty() throws IOException, URISyntaxException, InterruptedException {
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("state", "Texas");
    queryParams.put("county", "NotCounty");
    HttpResponse<String> response = tryRequest("broadband", queryParams);

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);
    Map<String, String> responseBody = jsonAdapter.fromJson(response.body());

    assertEquals("error_bad_request", responseBody.get("result"));
  }

  /**
   * Method that tests when the state inputted does not exist or match one in the census.
   *
   * @throws IOException exception where it failed to read/open information.
   * @throws URISyntaxException exception where URI syntax is incorrect.
   * @throws InterruptedException exception where connection to API is interrupted.
   */
  @Test
  public void testNotState() throws IOException, URISyntaxException, InterruptedException {
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("state", "Tex");
    queryParams.put("county", "Bexar%20County");
    HttpResponse<String> response = tryRequest("broadband", queryParams);

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);
    Map<String, String> responseBody = jsonAdapter.fromJson(response.body());

    assertEquals("error_bad_request", responseBody.get("result"));
  }
}
