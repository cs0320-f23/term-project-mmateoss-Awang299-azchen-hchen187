package edu.brown.cs.student.Tests.server.spotify.handlerTests.tokenHandlerTests;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.server.spotify.data.MockData;
import edu.brown.cs.student.main.server.spotify.handlers.RecommendationHandler;
import edu.brown.cs.student.main.server.spotify.handlers.TokenHandler;
import edu.brown.cs.student.main.server.spotify.records.recommendationRecords.Recommendation;
import edu.brown.cs.student.main.server.spotify.tokens.MockedToken;
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

public class MockedTokenHandlerTests {

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

    MockedToken generator = new MockedToken();
    Spark.get("token", new TokenHandler(generator));

    Spark.init();
    Spark.awaitInitialization(); // don't continue until the server is listening
  }

  /**
   * Method gotten from the gearup that stops the connection to the server.
   */
  @AfterEach
  public void teardown() {
    // Gracefully stop Spark listening on both endpoints
    Spark.unmap("/token");
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
   * Method that tests we get the correct error message when a parameter is included.
   *
   * @throws Exception any exception that could occur from making the api call.
   */
  @Test
  public void hasParamsTest() throws Exception{
    Map<String, String> queryParams = new HashMap<>();
    queryParams.put("blah", "bluh");


    HttpResponse<String> response = tryRequest("token",queryParams);

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);
    Map<String, String> responseBody = jsonAdapter.fromJson(response.body());
    Assert.assertEquals("Error",responseBody.get("Result"));
    Assert.assertEquals("please do not include any parameters when trying to get the token",
        responseBody.get("Error Message"));


  }

  /**
   * Method that tests that the correct mocked token is returned when it should be.
   *
   * @throws Exception any exception that could occur from making the api call.
   */
  @Test
  public void getTokenTest() throws Exception{
    Map<String, String> queryParams = new HashMap<>();

    HttpResponse<String> response = tryRequest("token", queryParams);

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);

    Map<String, String> responseBody = jsonAdapter.fromJson(response.body());
    Assert.assertEquals("Success",responseBody.get("Result"));
    Assert.assertEquals("mockedTokenABCDEFG",
        responseBody.get("token"));

  }


}
