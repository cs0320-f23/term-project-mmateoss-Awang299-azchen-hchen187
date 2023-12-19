package edu.brown.cs.student.Tests.server.spotify.handlerTests.integrationTests;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.server.audioRecognition.audioData.AudioData;
import edu.brown.cs.student.main.server.handlers.AudioTextHandler;
import edu.brown.cs.student.main.server.handlers.GetSongHandler;
import edu.brown.cs.student.main.server.handlers.LyricsHandler;
import edu.brown.cs.student.main.server.handlers.RecommendationHandler;
import edu.brown.cs.student.main.server.handlers.ScoreHandler;
import edu.brown.cs.student.main.server.handlers.TokenHandler;
import edu.brown.cs.student.main.server.lyrics.mockedLyrics.MockLyricsData;
import edu.brown.cs.student.main.server.spotify.data.MockData;
import edu.brown.cs.student.main.server.spotify.records.recommendationRecords.Recommendation;
import edu.brown.cs.student.main.server.spotify.tokens.MockedToken;
import edu.brown.cs.student.main.server.translate.mockedData.MockTranslateData;
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

/** Class that makes an integration test to ensure that all the handlers work well together */
public class IntegrationTests {

  /** Method that is run once at the beginning. Gotten from the gearup. */
  @BeforeAll
  public static void setup_before_everything() {

    // Set the Spark port number
    Spark.port(0);
    Logger.getLogger("").setLevel(Level.WARNING); // empty name = root logger
  }

  /** Method gotten from the gearup, that sets up our server, called before each test is run. */
  @BeforeEach
  public void setup() {
    // In fact, restart the entire Spark server for every test!
    MockData data =
        new MockData(
            "data/mockedSpotifyJsons/mockedSearch/mockedTrackSearch1.json",
            "data/mockedSpotifyJsons/mockedRecommendations/" + "mockedRecommendation1.json",
            "data/mockedSpotifyJsons/mockedAudioFeatures/mockedAudioFeats1.json");
    MockLyricsData lyricsData = new MockLyricsData();
    MockTranslateData mockTranslateData = new MockTranslateData();
    AudioData audioData = new AudioData();
    MockedToken generator = new MockedToken();

    Spark.post("audioText", new AudioTextHandler(audioData));
    Spark.get("getLyrics", new LyricsHandler(lyricsData, mockTranslateData));
    Spark.get("getScore", new ScoreHandler(lyricsData));
    Spark.get("recommendation", new RecommendationHandler(data, lyricsData));
    Spark.get("getSongs", new GetSongHandler(data, lyricsData));
    Spark.get("token", new TokenHandler(generator));

    Spark.init();
    Spark.awaitInitialization(); // don't continue until the server is listening
  }

  /** Method gotten from the gearup that stops the connection to the server. */
  @AfterEach
  public void teardown() {
    // Gracefully stop Spark listening on both endpoints
    Spark.unmap("/recommendation");
    Spark.unmap("/getSongs");
    Spark.unmap("/audioText");
    Spark.unmap("/getLyrics");
    Spark.unmap("/getScore");
    Spark.unmap("/token");
    Spark.awaitStop(); // don't proceed until the server is stopped
  }

  /**
   * Method gotten from the gear up that allows us to try an API request
   *
   * @param apiCall the endpoint you are calling to.
   * @param queryParams the query parameters that you want to use.
   * @return HttpResponse that can be used to set up a connection with an API.
   * @throws IOException exception where it failed to read/open information.
   * @throws InterruptedException exception where connection to API is interrupted.
   * @throws URISyntaxException exception where URI syntax is incorrect.
   */
  private static HttpResponse<String> tryRequest(String apiCall, Map<String, String> queryParams)
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
    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(new URI("http://localhost:" + Spark.port() + "/" + apiCall + queryString))
            .GET() // This is optional since GET is the default method.
            .build();

    // Send the request and get the response
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    return response;
  }

  /**
   * Method that makes a query to the server, made specifically for recommendation handler because
   * it allows for an extra parameter that is needed.
   */
  private static HttpResponse<String> tryRequestRec(
      String apiCall, Map<String, String> queryParams, String allNames)
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
    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(
                new URI(
                    "http://localhost:" + Spark.port() + "/" + apiCall + queryString + allNames))
            .GET() // This is optional since GET is the default method.
            .build();

    // Send the request and get the response
    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    return response;
  }

  /**
   * Method that integrates multiple handlers testing that we can
   *
   * @throws Exception
   */
  @Test
  public void integrationTest() throws Exception {
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("token", "dsdsds");
    queryParams.put("limit", "5");
    queryParams.put("query", "blah");
    HttpResponse<String> response = tryRequest("getSongs", queryParams);
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);
    Map<String, Object> responseBody = jsonAdapter.fromJson(response.body());
    Assert.assertEquals("Success", responseBody.get("Result"));

    queryParams.clear();
    queryParams.put("token", "dsdsds");
    queryParams.put("variability", "0.2");
    queryParams.put("limit", "5");

    // recommendation
    String allNames = "&allNames=";
    allNames += "Enchanted";
    allNames += "&allNames=All%20too%20well";

    HttpResponse<String> response2 = tryRequestRec("recommendation", queryParams, allNames);

    Moshi moshi2 = new Moshi.Builder().build();
    JsonAdapter<Recommendation> jsonAdapter2 = moshi2.adapter(Recommendation.class);
    Recommendation responseBody2 = jsonAdapter2.fromJson(response2.body());
    Assert.assertTrue(responseBody2.tracks().size() == 5);

    queryParams.clear();
    HttpResponse<String> response3 = tryRequest("token", queryParams);
    JsonAdapter<Map> jsonAdapter3 = moshi.adapter(Map.class);

    Map<String, String> responseBody3 = jsonAdapter3.fromJson(response3.body());
    Assert.assertEquals("Success", responseBody3.get("Result"));
    Assert.assertEquals("mockedTokenABCDEFG", responseBody3.get("token"));

    queryParams.clear();

    // building an incorrect call to check stuff still works after
    queryParams.put("fromLanguage", "english");
    queryParams.put("toLanguage", "spanish");

    HttpResponse<String> response4 = tryRequest("getLyrics", queryParams);
    JsonAdapter<Map> jsonAdapter4 = moshi.adapter(Map.class);

    Map<String, String> responseBody4 = jsonAdapter4.fromJson(response4.body());
    Assert.assertEquals("Error", responseBody4.get("Result"));

    queryParams.clear();

    queryParams.put("token", "dsdsds");
    queryParams.put("limit", "5");
    queryParams.put("query", "blah");
    HttpResponse<String> response5 = tryRequest("getSongs", queryParams);
    Moshi moshi5 = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter5 = moshi5.adapter(Map.class);
    Map<String, Object> responseBody5 = jsonAdapter5.fromJson(response5.body());
    Assert.assertEquals("Success", responseBody5.get("Result"));
  }
}
