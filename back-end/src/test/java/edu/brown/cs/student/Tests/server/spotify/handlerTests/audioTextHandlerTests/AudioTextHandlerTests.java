package edu.brown.cs.student.Tests.server.spotify.handlerTests.audioTextHandlerTests;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.server.audioRecognition.audioData.AudioData;
import edu.brown.cs.student.main.server.audioRecognition.audioData.MockedAudioData;
import edu.brown.cs.student.main.server.audioRecognition.audioData.mockedAudioSource.MockedAudioDataSource;
import edu.brown.cs.student.main.server.handlers.AudioTextHandler;
import edu.brown.cs.student.main.server.handlers.TokenHandler;
import edu.brown.cs.student.main.server.spotify.tokens.MockedToken;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
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
 * Class that tests that our AudioText handler works properly with real data and API calls.
 */
public class AudioTextHandlerTests {


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
    AudioData data = new AudioData();
    Spark.post("audioText", new AudioTextHandler(data));
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
    Spark.unmap("/audioText");
    Spark.awaitStop(); // don't proceed until the server is stopped
  }

  /**
   * Method that tests we get the correct error message when token is missing.
   *
   * @throws Exception any exception that could occur from making the api call.
   */
  @Test
  public void missingTokenTest() throws Exception{

    String uriString = "http://localhost:" + Spark.port() + "/audioText";

    MockedAudioDataSource source = new MockedAudioDataSource("data/mockedAudio/mockedAudio1.mp3");
    byte[] encoded = source.getBase64Encoded();

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(uriString))
        .header("accept", "application/json")
        .header("Content-Type", "audio/mpeg")
        .POST(HttpRequest.BodyPublishers.ofByteArray(encoded))
        .build();
    HttpResponse<String> response = HttpClient.newHttpClient().send(request,
        HttpResponse.BodyHandlers.ofString());

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);
    Map<String, String> responseBody = jsonAdapter.fromJson(response.body());
    Assert.assertEquals("Error",responseBody.get("Result"));
    Assert.assertEquals("please only pass in a token and language as params",
        responseBody.get("Error Message"));
  }


  /**
   * Method that tests we get the correct error message when extra params.
   *
   * @throws Exception any exception that could occur from making the api call.
   */
  @Test
  public void extraParamTest() throws Exception{

    String uriString = "http://localhost:" + Spark.port() +
        "/audioText?token=dsd&extra=dsd";

    MockedAudioDataSource source = new MockedAudioDataSource("data/mockedAudio/mockedAudio1.mp3");
    byte[] encoded = source.getBase64Encoded();

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(uriString))
        .header("accept", "application/json")
        .header("Content-Type", "audio/mpeg")
        .POST(HttpRequest.BodyPublishers.ofByteArray(encoded))
        .build();
    HttpResponse<String> response = HttpClient.newHttpClient().send(request,
        HttpResponse.BodyHandlers.ofString());

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);
    Map<String, String> responseBody = jsonAdapter.fromJson(response.body());
    Assert.assertEquals("Error",responseBody.get("Result"));
    Assert.assertEquals("please only pass in a token and language as params",
        responseBody.get("Error Message"));
  }

  /**
   * Method that tests that we can use the handler with a post request to get the audio as text
   */
  @Test
  public void testRealAudioTest() throws Exception {

    //TODO: Put DeepGram Token to run
    String token = "";

    String uriString = "http://localhost:" + Spark.port() +
        "/audioText?token="+token;

    MockedAudioDataSource source = new MockedAudioDataSource("data/mockedAudio/mockedAudio1.mp3");
    byte[] encoded = source.getBase64Encoded();

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(uriString))
        .header("accept", "application/json")
        .header("Content-Type", "audio/mpeg")
        .POST(HttpRequest.BodyPublishers.ofByteArray(encoded))
        .build();
    HttpResponse<String> response = HttpClient.newHttpClient().send(request,
        HttpResponse.BodyHandlers.ofString());

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);
    Map<String, String> responseBody = jsonAdapter.fromJson(response.body());
    Assert.assertEquals("Success",responseBody.get("Result"));
    Assert.assertEquals("What can I do for you?",
        responseBody.get("transcript"));
  }

  /**
   * Method that tests that we can turn audio in other language to text
   */
  @Test
  public void testOtherLanguage() throws Exception{
    String token = "";

    String uriString = "http://localhost:" + Spark.port() +
        "/audioText?token="+token;

    MockedAudioDataSource source = new MockedAudioDataSource("data/mockedAudio/mockAudio3.mp3");
    byte[] encoded = source.getBase64Encoded();

    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(uriString))
        .header("accept", "application/json")
        .header("Content-Type", "audio/mpeg")
        .POST(HttpRequest.BodyPublishers.ofByteArray(encoded))
        .build();
    HttpResponse<String> response = HttpClient.newHttpClient().send(request,
        HttpResponse.BodyHandlers.ofString());

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Map> jsonAdapter = moshi.adapter(Map.class);
    Map<String, String> responseBody = jsonAdapter.fromJson(response.body());
    Assert.assertEquals("Success",responseBody.get("Result"));
    Assert.assertEquals("O la",
        responseBody.get("transcript"));

    
  }






}
