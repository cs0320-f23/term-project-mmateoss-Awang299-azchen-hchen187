package edu.brown.cs.student.Tests.server.spotify.handlerTests;

import edu.brown.cs.student.main.server.spotify.data.CachedSpotifyData;
import edu.brown.cs.student.main.server.spotify.data.MockData;
import edu.brown.cs.student.main.server.spotify.handlers.RecommendationHandler;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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


  //TODO: Create for a way to know if the token does not work and test it, also, create for a check
  // in the generated recommendation to ensure that they have a possibility to get lyrics from
  // them

  /**
   * Method that tests we get the correct error when a limit is not passed in as a parameter.
   */
  @Test
  public void missingLimitTest(){

  }

  /**
   * Method that tests we get the correct error when the limit is set to 0 or to a negative number.
   */
  @Test
  public void zeroLimitTest(){

  }

  /**
   * Method that tests we get the correct error when a token is not passed in as a parameter.
   */
  @Test
  public void missingTokenTest(){

  }

  /**
   * Method that tests we get the correct error when the song names are not passed in.
   */
  @Test
  public void missingNamesTest(){

  }

  /**
   * Method that tests we get the correct error when the names parameter is empty.
   */
  @Test
  public void emptyNamesTest(){

  }

  /**
   * Method that tests we get the correct error when the wrong number of parameters are passed in.
   */
  @Test
  public void wrongNumberParams(){

  }

  /**
   * Method that tests we get the correct error when the correct number of parameters are passed in
   * but one of the parameters passed in was not something we wanted.
   */
  @Test
  public void rightNumParamsButWrongParam(){

  }

  /**
   * Method that tests we can get a recommendation from the handler when everything is passed in
   * properly.
   */
  @Test
  public void getRecommendationTest(){

  }


}
