package edu.brown.cs.student.Tests.server.spotify.handlerTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import edu.brown.cs.student.main.server.spotify.tokens.TokenGenerator;
import edu.brown.cs.student.main.server.spotify.data.CachedSpotifyData;
import edu.brown.cs.student.main.server.spotify.handlers.RecommendationHandler;
import edu.brown.cs.student.main.server.spotify.records.recommendationRecords.Recommendation;
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
import org.testng.Assert;
import spark.Spark;

public class RecommendationHandlerTests {

  private String token;
  public RecommendationHandlerTests(){

  }

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
    CachedSpotifyData data = new CachedSpotifyData();
    Spark.get("recommendation", new RecommendationHandler(data));

    Spark.init();
    Spark.awaitInitialization(); // don't continue until the server is listening

    TokenGenerator generator = new TokenGenerator();
    this.token = generator.getToken();
  }

  /**
   * Method gotten from the gearup that stops the connection to the server.
   */
  @AfterEach
  public void teardown() {
    // Gracefully stop Spark listening on both endpoints
    Spark.unmap("/recommendation");
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
  static private HttpResponse<String> tryRequest(String apiCall, Map<String, String> queryParams, String allNames)
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
        .uri(new URI("http://localhost:" + Spark.port() + "/" + apiCall + queryString+ allNames))
        .GET()  // This is optional since GET is the default method.
        .build();

    // Send the request and get the response
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    return response;
  }

  /**
   * Testing that we can call the handler and get a recommendation with one song passed in.
   *
   * @throws URISyntaxException   exception where URI syntax is incorrect.
   * @throws IOException          exception where it failed to read/open
   *                              information.
   * @throws InterruptedException exception where connection to API is
   *                              interrupted.
   *
   */
  @Test
  public void testHandlerRec() throws IOException, InterruptedException, URISyntaxException{
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("token", this.token);
    queryParams.put("variability", "0.2");
    queryParams.put("limit", "5");

    //missing names
    String allNames = "&allNames=";
    allNames += "Enchanted";


    HttpResponse<String> response = tryRequest("recommendation", queryParams, allNames);

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Recommendation> jsonAdapter = moshi.adapter(Recommendation.class);
    Recommendation responseBody = jsonAdapter.fromJson(response.body());
    Assert.assertEquals(responseBody.tracks().size(), 5);
    Assert.assertTrue(responseBody.seeds().get(0).afterFilteringSize() > 10);
    Assert.assertEquals(responseBody.seeds().get(0).initialPoolSize(), 500);
  }

  /**
   * Testing that we can call the handler and get a recommendation with two songs passed in.
   *
   * @throws URISyntaxException   exception where URI syntax is incorrect.
   * @throws IOException          exception where it failed to read/open
   *                              information.
   * @throws InterruptedException exception where connection to API is
   *                              interrupted.
   *
   */
  @Test
  public void testHandlerRecMultiSong() throws IOException, InterruptedException, URISyntaxException {
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("token", this.token);
    queryParams.put("variability", "0.2");
    queryParams.put("limit", "5");

    //missing names
    String allNames = "&allNames=";
    allNames += "Enchanted";
    allNames += "&allNames=All%20too%20well";

    HttpResponse<String> response = tryRequest("recommendation", queryParams, allNames);

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Recommendation> jsonAdapter = moshi.adapter(Recommendation.class);
    Recommendation responseBody = jsonAdapter.fromJson(response.body());
    Assert.assertEquals(responseBody.tracks().size(), 5);
    Assert.assertTrue(responseBody.seeds().get(0).afterFilteringSize() > 10);

  }

  /**
   * Testing that we can call the handler and get a recommendation with three songs passed in.
   *
   * @throws URISyntaxException   exception where URI syntax is incorrect.
   * @throws IOException          exception where it failed to read/open
   *                              information.
   * @throws InterruptedException exception where connection to API is
   *                              interrupted.
   *
   */
  @Test
  public void threeSongRecTest() throws IOException, InterruptedException, URISyntaxException{
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("token", this.token);
    queryParams.put("variability", "0.2");
    queryParams.put("limit", "5");

    //missing names
    String allNames = "&allNames=";
    allNames += "Enchanted";
    allNames += "&allNames=All%20too%20well";
    allNames += "&allNames=Anti-Hero";

    HttpResponse<String> response = tryRequest("recommendation", queryParams, allNames);

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Recommendation> jsonAdapter = moshi.adapter(Recommendation.class);
    Recommendation responseBody = jsonAdapter.fromJson(response.body());
    Assert.assertEquals(responseBody.tracks().size(), 5);
    Assert.assertTrue(responseBody.seeds().get(0).afterFilteringSize() > 10);

  }




}
