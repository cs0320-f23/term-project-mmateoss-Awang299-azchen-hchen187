package edu.brown.cs.student.Tests.server.spotify.handlerTests.recommendationHandlerTests;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.server.spotify.data.CachedSpotifyData;
import edu.brown.cs.student.main.server.spotify.data.MockData;
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

/**
 * Class used to test that the RecommendationHandler works as intended but used mocked data to
 * prevent and restrict calls made to the spotify api, allowing us to test as many times and as
 * often as we want.
 */
public class MockedRecommendationHandlerTests {


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
   * Method that tests we get the correct error when a limit is not passed in as a parameter.
   */
  @Test
  public void missingLimitTest() throws IOException, InterruptedException, URISyntaxException{

    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("token", "dsdsds");
    queryParams.put("variability", "0.2");
    // leaving limit out
    String allNames = "&allNames=";
    allNames += "Enchanted";
    allNames += "&allNames=All%20too%20well";

    HttpResponse<String> response = tryRequest("recommendation",queryParams,allNames);

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);
    Map<String, String> responseBody = jsonAdapter.fromJson(response.body());
    Assert.assertEquals("Error",responseBody.get("Result"));
    Assert.assertEquals("please ensure that that you passed in a variability, token, limit, "
            + "and list of song names for generating recommendations",
        responseBody.get("Error Message"));
  }

  /**
   * Method that tests we get the correct error when the limit is set to 0 or to a negative number.
   */
  @Test
  public void zeroLimitTest() throws IOException, InterruptedException, URISyntaxException{
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("token", "dsdsds");
    queryParams.put("variability", "0.2");
    queryParams.put("limit", "0");
    String allNames = "&allNames=";
    allNames += "Enchanted";
    allNames += "&allNames=All%20too%20well";

    HttpResponse<String> response = tryRequest("recommendation", queryParams, allNames);

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);
    Map<String, String> responseBody = jsonAdapter.fromJson(response.body());
    Assert.assertEquals("Error",responseBody.get("Result"));
    Assert.assertEquals("the limit must be an integer in the range 1-100",
        responseBody.get("Error Message"));
  }

  /**
   * Method that tests we get the correct error when the limit is set to int above 100
   */
  @Test
  public void overHundredLimTest() throws IOException, InterruptedException, URISyntaxException{
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("token", "dsdsds");
    queryParams.put("variability", "0.2");
    queryParams.put("limit", "101");
    String allNames = "&allNames=";
    allNames += "Enchanted";
    allNames += "&allNames=All%20too%20well";

    HttpResponse<String> response = tryRequest("recommendation", queryParams, allNames);

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);
    Map<String, String> responseBody = jsonAdapter.fromJson(response.body());
    Assert.assertEquals("Error",responseBody.get("Result"));
    Assert.assertEquals("the limit must be an integer in the range 1-100",
        responseBody.get("Error Message"));
  }

  /**
   * Method that tests we get the correct error when a token is not passed in as a parameter.
   */
  @Test
  public void missingTokenTest() throws IOException, InterruptedException, URISyntaxException{
    Map<String, String> queryParams = new HashMap<>();
    // missing token
    queryParams.put("variability", "0.2");
    queryParams.put("limit", "2");
    String allNames = "&allNames=";
    allNames += "Enchanted";
    allNames += "&allNames=All%20too%20well";

    HttpResponse<String> response = tryRequest("recommendation", queryParams, allNames);

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);
    Map<String, String> responseBody = jsonAdapter.fromJson(response.body());
    Assert.assertEquals("Error",responseBody.get("Result"));
    Assert.assertEquals("please ensure that that you passed in a variability, token, limit, "
            + "and list of song names for generating recommendations",
        responseBody.get("Error Message"));
  }

  /**
   * Method that tests we get the correct error when the song names are not passed in.
   */
  @Test
  public void missingNamesTest() throws IOException, InterruptedException, URISyntaxException{
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("token", "dsdsds");
    queryParams.put("variability", "0.2");
    queryParams.put("limit", "2");
    queryParams.put("extra", "blah");
    //missing names
    String allNames = "";

    HttpResponse<String> response = tryRequest("recommendation", queryParams, allNames);

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);
    Map<String, String> responseBody = jsonAdapter.fromJson(response.body());
    Assert.assertEquals("Error",responseBody.get("Result"));
    Assert.assertEquals("please ensure that you passed in a list of song names as a parameter"
            + " in order to generate recommendations",
        responseBody.get("Error Message"));
  }

  /**
   * Method that tests we get the correct error when the names parameter is empty.
   */
  @Test
  public void noNamesTest() throws IOException, InterruptedException, URISyntaxException {

    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("token", "dsdsds");
    queryParams.put("variability", "0.2");
    queryParams.put("limit", "2");

    //missing allNames
    String allNames = "&allName";

    HttpResponse<String> response = tryRequest("recommendation", queryParams, allNames);

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);
    Map<String, String> responseBody = jsonAdapter.fromJson(response.body());
    Assert.assertEquals("Error",responseBody.get("Result"));
    Assert.assertEquals("please ensure that you passed in a list of song names as a parameter"
            + " in order to generate recommendations",
        responseBody.get("Error Message"));

  }

  /**
   * Method that tests we get the correct error when the wrong number of parameters are passed in.
   */
  @Test
  public void wrongNumberParams() throws IOException, InterruptedException, URISyntaxException{
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("token", "dsdsds");
    queryParams.put("variability", "0.2");
    queryParams.put("limit", "2");
    queryParams.put("extra", "blah");
    //missing names
    String allNames = "&allNames=";
    allNames += "Enchanted";
    allNames += "&allNames=All%20too%20well";

    HttpResponse<String> response = tryRequest("recommendation", queryParams, allNames);

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);
    Map<String, String> responseBody = jsonAdapter.fromJson(response.body());
    Assert.assertEquals("Error",responseBody.get("Result"));
    Assert.assertEquals("please ensure that that you passed in a variability, token, limit, "
            + "and list of song names for generating recommendations",
        responseBody.get("Error Message"));
  }

  /**
   * Method that tests we get the correct error when the correct number of parameters are passed in
   * but one of the parameters passed in was not something we wanted.
   */
  @Test
  public void rightNumParamsButWrongParam() throws IOException, InterruptedException, URISyntaxException{
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("token", "dsdsds");
    queryParams.put("variability", "0.2");
    queryParams.put("blahhhh", "2");

    //missing names
    String allNames = "&allNames=";
    allNames += "Enchanted";
    allNames += "&allNames=All%20too%20well";

    HttpResponse<String> response = tryRequest("recommendation", queryParams, allNames);

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);
    Map<String, String> responseBody = jsonAdapter.fromJson(response.body());
    Assert.assertEquals("Error",responseBody.get("Result"));
    Assert.assertEquals("please ensure that that you passed in a variability, token, limit, "
            + "and list of song names for generating recommendations",
        responseBody.get("Error Message"));

  }

  /**
   * Method that tests we can get a recommendation from the handler when everything is passed in
   * properly.
   */
  @Test
  public void getRecommendationTest() throws IOException, InterruptedException, URISyntaxException{
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("token", "dsdsds");
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
    Assert.assertEquals(responseBody.tracks().size(), 20);
    Assert.assertEquals(responseBody.tracks().get(0).id(), "5N9M7Ji7KYrmfJ6Jki3raU");

  }


}
