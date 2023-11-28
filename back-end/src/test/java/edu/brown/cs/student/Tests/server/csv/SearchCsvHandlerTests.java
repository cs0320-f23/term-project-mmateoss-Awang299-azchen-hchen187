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
import java.util.ArrayList;
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

public class SearchCsvHandlerTests {
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
   * Method that tests that SearchCsvHandler will work with the correct parameter names and values
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
    queryParams.put("target", "South%20Kingstown");
    queryParams.put("colIdx", "0");
    HttpResponse<String> searchResponse = tryRequest("searchcsv", queryParams);

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);
    Map<String, Object> responseBody = jsonAdapter.fromJson(searchResponse.body());

    List<List<String>> dataBody = List.of(List.of("South Kingstown", "102,242.00", "114,202.00", "42,080.00"));
    List<String> parametersBody = List.of("colIdx", "target");

    assertEquals("success", responseBody.get("result"));
    assertEquals(dataBody, responseBody.get("data"));
  }

  /**
   * Method that tests that SearchCsvHandler will return an error when given incorrect parameter names
   *
   * @throws URISyntaxException exception where URI syntax is incorrect.
   * @throws IOException exception where it failed to read/open information.
   * @throws InterruptedException exception where connection to API is interrupted.
   */
  @Test
  public void testInvalidParameterNames() throws IOException, URISyntaxException, InterruptedException {
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("filePath", "data/census/RI%20Income%20Census.csv");
    queryParams.put("hasHeader", "true");
    HttpResponse<String> loadResponse = tryRequest("loadcsv", queryParams);

    queryParams.clear();
    queryParams.put("invalid-target", "South%20Kingstown");
    HttpResponse<String> searchResponse = tryRequest("searchcsv", queryParams);

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);
    Map<String, Object> responseBody = jsonAdapter.fromJson(searchResponse.body());

    List<List<String>> dataBody = List.of(List.of("South Kingstown", "102,242.00", "114,202.00", "42,080.00"));
    List<String> parametersBody = List.of("invalid-target");

    assertEquals("error_bad_request", responseBody.get("result"));
    assertEquals(parametersBody, responseBody.get("parameters"));
    assertEquals("invalid parameters", responseBody.get("message"));
  }

  /**
   * Method that tests that SearchCsvHandler will return success even when given a nonexistent target value
   *
   * @throws URISyntaxException exception where URI syntax is incorrect.
   * @throws IOException exception where it failed to read/open information.
   * @throws InterruptedException exception where connection to API is interrupted.
   */
  @Test
  public void testNonexistentTargetValue() throws IOException, URISyntaxException, InterruptedException {
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("filePath", "data/census/RI%20Income%20Census.csv");
    queryParams.put("hasHeader", "true");
    HttpResponse<String> loadResponse = tryRequest("loadcsv", queryParams);

    queryParams.clear();
    queryParams.put("target", "Invalid-South-Target");
    HttpResponse<String> searchResponse = tryRequest("searchcsv", queryParams);

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);
    Map<String, Object> responseBody = jsonAdapter.fromJson(searchResponse.body());

    List<String> parametersBody = List.of("target");

    assertEquals("success", responseBody.get("result"));
    assertEquals(new ArrayList<String>(), responseBody.get("data"));
  }

  /**
   * Method that tests that SearchCsvHandler will return an error when missing parameters
   *
   * @throws URISyntaxException exception where URI syntax is incorrect.
   * @throws IOException exception where it failed to read/open information.
   * @throws InterruptedException exception where connection to API is interrupted.
   */
  @Test
  public void testMissingParameters() throws IOException, URISyntaxException, InterruptedException {
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("filePath", "data/census/RI%20Income%20Census.csv");
    queryParams.put("hasHeader", "true");
    HttpResponse<String> loadResponse = tryRequest("loadcsv", queryParams);

    queryParams.clear();
    HttpResponse<String> searchResponse = tryRequest("searchcsv", queryParams);

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);
    Map<String, Object> responseBody = jsonAdapter.fromJson(searchResponse.body());

    List<List<String>> dataBody = List.of(List.of("South Kingstown", "102,242.00", "114,202.00", "42,080.00"));

    assertEquals("error_bad_request", responseBody.get("result"));
    assertEquals(null, responseBody.get("data"));
    assertEquals(new ArrayList<String>(), responseBody.get("parameters"));
  }

  /**
   * Method that tests that SearchCsvHandler will return an error when searching before
   * loading in a csv file
   *
   * @throws URISyntaxException exception where URI syntax is incorrect.
   * @throws IOException exception where it failed to read/open information.
   * @throws InterruptedException exception where connection to API is interrupted.
   */
  @Test
  public void testSearchBeforeLoading() throws IOException, URISyntaxException, InterruptedException {
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("target", "Test%20Target");
    HttpResponse<String> searchResponse = tryRequest("searchcsv", queryParams);

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);
    Map<String, Object> responseBody = jsonAdapter.fromJson(searchResponse.body());

    assertEquals("error_data_source", responseBody.get("result"));
    assertEquals(null, responseBody.get("data"));
    assertEquals("load in csv file before searching", responseBody.get("message"));
    assertEquals(null, responseBody.get("target"));
  }

  /**
   * Method that tests that SearchCsvHandler will return an error with invalid colIdx
   *
   * @throws URISyntaxException exception where URI syntax is incorrect.
   * @throws IOException exception where it failed to read/open information.
   * @throws InterruptedException exception where connection to API is interrupted.
   */
  @Test
  public void testInvalidColIdx() throws IOException, URISyntaxException, InterruptedException {
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("filePath", "data/census/RI%20Income%20Census.csv");
    queryParams.put("hasHeader", "true");
    HttpResponse<String> loadResponse = tryRequest("loadcsv", queryParams);

    queryParams.clear();
    queryParams.put("target", "South%20Kingstown");
    queryParams.put("colIdx", "-1");
    HttpResponse<String> searchResponse = tryRequest("searchcsv", queryParams);

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);
    Map<String, Object> responseBody = jsonAdapter.fromJson(searchResponse.body());

    assertEquals("error_bad_request", responseBody.get("result"));
    assertEquals("Make sure you are searching for a column index that exists", responseBody.get("message"));
  }

  /**
   * Method that tests that SearchCsvHandler will return an error with invalid colIdx
   *
   * @throws URISyntaxException exception where URI syntax is incorrect.
   * @throws IOException exception where it failed to read/open information.
   * @throws InterruptedException exception where connection to API is interrupted.
   */
  @Test
  public void testInvalidColName() throws IOException, URISyntaxException, InterruptedException {
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("filePath", "data/census/RI%20Income%20Census.csv");
    queryParams.put("hasHeader", "true");
    HttpResponse<String> loadResponse = tryRequest("loadcsv", queryParams);

    queryParams.clear();
    queryParams.put("target", "South%20Kingstown");
    queryParams.put("colName", "random-name");
    HttpResponse<String> searchResponse = tryRequest("searchcsv", queryParams);

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);
    Map<String, Object> responseBody = jsonAdapter.fromJson(searchResponse.body());

    assertEquals("error_bad_request", responseBody.get("result"));
    assertEquals("Acceptable headers: [City/Town, Median Household Income , Median Family Income, Per Capita Income]", responseBody.get("message"));
  }
}
