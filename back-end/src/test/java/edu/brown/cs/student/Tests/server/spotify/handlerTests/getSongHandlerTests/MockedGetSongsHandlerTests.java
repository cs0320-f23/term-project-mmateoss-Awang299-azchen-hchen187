package edu.brown.cs.student.Tests.server.spotify.handlerTests.getSongHandlerTests;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.server.handlers.GetSongHandler;
import edu.brown.cs.student.main.server.handlers.RecommendationHandler;
import edu.brown.cs.student.main.server.spotify.data.MockData;
import edu.brown.cs.student.main.server.spotify.records.recommendationRecords.Recommendation;
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
import org.testng.Assert;
import spark.Spark;

public class MockedGetSongsHandlerTests {



  /**
   * Method that is run once at the beginning. Gotten from the gearup.
   */
  @BeforeAll
  public static void setup_before_everything() {

    // Set the Spark port number
    Spark.port(0);
    Logger.getLogger("").setLevel(Level.WARNING); // empty name = root logger
  }


  /**
   * Method gotten from the gearup, that sets up our server, called before each test is run.
   */
  @BeforeEach
  public void setup() {
    // In fact, restart the entire Spark server for every test!
    MockData data = new MockData(
        "data/mockedSpotifyJsons/mockedSearch/mockedTrackSearch1.json",
        "data/mockedSpotifyJsons/mockedRecommendations/"
            + "mockedRecommendation1.json",
        "data/mockedSpotifyJsons/mockedAudioFeatures/mockedAudioFeats1.json");
    Spark.get("recommendation", new RecommendationHandler(data));
    Spark.get("getSongs", new GetSongHandler(data));

    Spark.init();
    Spark.awaitInitialization(); // don't continue until the server is listening
  }

  /**
   * Method gotten from the gearup that stops the connection to the server.
   */
  @AfterEach
  public void teardown() {
    // Gracefully stop Spark listening on both endpoints
    Spark.unmap("/recommendation");
    Spark.unmap("/getSongs");
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
   * Method that tests when token is not included as a parameter.
   */
  @Test
  public void testNoToken() throws Exception{
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("limit", "10");
    queryParams.put("query", "blah");



    HttpResponse<String> response = tryRequest("getSongs",queryParams);

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);
    Map<String, Object> responseBody = jsonAdapter.fromJson(response.body());
    Assert.assertEquals("Error",responseBody.get("Result"));
    Assert.assertEquals("Please ensure you pass in a token, limit, and query as"
            + " parameters",
        responseBody.get("Error Message"));
  }


  /**
   * Method that tests when query is not included as a parameter.
   */
  @Test
  public void testNoQuery() throws Exception{
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("token", "dsdsds");
    queryParams.put("limit", "10");



    HttpResponse<String> response = tryRequest("getSongs",queryParams);

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);
    Map<String, Object> responseBody = jsonAdapter.fromJson(response.body());
    Assert.assertEquals("Error",responseBody.get("Result"));
    Assert.assertEquals("Please ensure you pass in a token, limit, and query as"
            + " parameters",
        responseBody.get("Error Message"));
  }


  /**
   * Method that tests when limit is not included as a parameter.
   */
  @Test
  public void testNoLimit() throws Exception{

    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("token", "dsdsds");
    queryParams.put("query", "blah");



    HttpResponse<String> response = tryRequest("getSongs",queryParams);

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);
    Map<String, Object> responseBody = jsonAdapter.fromJson(response.body());
    Assert.assertEquals("Error",responseBody.get("Result"));
    Assert.assertEquals("Please ensure you pass in a token, limit, and query as"
            + " parameters",
        responseBody.get("Error Message"));
  }

  /**
   * Method that tests when not all params needed are included
   */
  @Test
  public void testCorrectNumWrongParams() throws Exception{
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("other", "dsdsds");
    queryParams.put("limit", "10");
    queryParams.put("query", "blah");



    HttpResponse<String> response = tryRequest("getSongs",queryParams);

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);
    Map<String, Object> responseBody = jsonAdapter.fromJson(response.body());
    Assert.assertEquals("Error",responseBody.get("Result"));
    Assert.assertEquals("Please ensure you pass in a token, limit, and query as"
            + " parameters",
        responseBody.get("Error Message"));
  }

  /**
   * Method that tests when there is an extra parameter.
   *
   */
  @Test
  public void testExtraParam() throws Exception{
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("token", "dsdsds");
    queryParams.put("limit", "10");
    queryParams.put("query", "blah");
    queryParams.put("extra", "other");


    HttpResponse<String> response = tryRequest("getSongs",queryParams);

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);
    Map<String, Object> responseBody = jsonAdapter.fromJson(response.body());
    Assert.assertEquals("Error",responseBody.get("Result"));
    Assert.assertEquals("Please ensure you pass in a token, limit, and query as"
            + " parameters",
        responseBody.get("Error Message"));
  }

  /**
   * Method that tests when limit is zero.
   */
  @Test
  public void testZeroLimit() throws Exception {
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("token", "dsdsds");
    queryParams.put("limit", "0");
    queryParams.put("query", "blah");



    HttpResponse<String> response = tryRequest("getSongs",queryParams);

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);
    Map<String, Object> responseBody = jsonAdapter.fromJson(response.body());
    Assert.assertEquals("Error",responseBody.get("Result"));
    Assert.assertEquals("the limit must be an integer in the range 1-20",
        responseBody.get("Error Message"));
  }

  /**
   * Method that tests when limit is negative
   */
  @Test
  public void testNegLimit() throws Exception {
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("token", "dsdsds");
    queryParams.put("limit", "-1");
    queryParams.put("query", "blah");



    HttpResponse<String> response = tryRequest("getSongs",queryParams);

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);
    Map<String, Object> responseBody = jsonAdapter.fromJson(response.body());
    Assert.assertEquals("Error",responseBody.get("Result"));
    Assert.assertEquals("the limit must be an integer in the range 1-20",
        responseBody.get("Error Message"));
  }

  /**
   * Method that tests when limit is larger than 20.
   */
  @Test
  public void testLargeLimit() throws Exception {
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("token", "dsdsds");
    queryParams.put("limit", "21");
    queryParams.put("query", "blah");



    HttpResponse<String> response = tryRequest("getSongs",queryParams);

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);
    Map<String, Object> responseBody = jsonAdapter.fromJson(response.body());
    Assert.assertEquals("Error",responseBody.get("Result"));
    Assert.assertEquals("the limit must be an integer in the range 1-20",
        responseBody.get("Error Message"));
  }

  /**
   * Method that tests when the parameters are all correct.
   */
  @Test
  public void testCorrectParams() throws Exception{
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("token", "dsdsds");
    queryParams.put("limit", "5");
    queryParams.put("query", "blah");



    HttpResponse<String> response = tryRequest("getSongs",queryParams);

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);
    Map<String, Object> responseBody = jsonAdapter.fromJson(response.body());
    Assert.assertEquals("Success",responseBody.get("Result"));

  }



}
