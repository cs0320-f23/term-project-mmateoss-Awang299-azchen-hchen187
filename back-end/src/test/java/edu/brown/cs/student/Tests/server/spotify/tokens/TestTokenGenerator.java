package edu.brown.cs.student.Tests.server.spotify.tokens;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

/**
 * Class that serves as a way of generating a spotify token for testing
 */
public class TestTokenGenerator {

  private String client_id;
  private String client_secret;
  private String combined;
  private String token;

  /**
   * Constructor for the TestTokenGenerator class
   */
  public TestTokenGenerator(){
    //TODO: Input your client_id and client_secret here to generate tokens for testing
    this.client_id = "62f77f3bf89c4d9eacd7276d2255e4d0";
    this.client_secret = "5417180516eb4a748dc254e0b545b06e";
    this.combined = this.client_id+":"+this.client_secret;

  }

  // learned how to create base64 encoded string: https://www.baeldung.com/java-base64-encode-and-decode
  // what and how to use bodyPublishers
  // https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/HttpRequest.BodyPublishers.html

  /**
   * Method that makes an API call to the spotify API and sets the token to the instance variable
   *
   * @throws URISyntaxException   exception where URI syntax is incorrect.
   * @throws IOException          exception where it failed to read/open
   *                              information.
   * @throws InterruptedException exception where connection to API is
   *                              interrupted.
   */
  private void generateToken() throws IOException, InterruptedException, URISyntaxException {
    String uriString ="https://accounts.spotify.com/api/token";
    String base64Encoded = Base64.getEncoder().encodeToString(this.combined.getBytes());


    HttpRequest buildRequest = HttpRequest.newBuilder()
        .uri(new URI(uriString))
        .header("Authorization", "Basic "+base64Encoded)
        .header("Content-Type","application/x-www-form-urlencoded")
        .POST(HttpRequest.BodyPublishers.ofString("grant_type=client_credentials"))
        .build();
    // building a response with the HttpRequest
    HttpResponse<String> response = HttpClient.newBuilder()
        .build()
        .send(buildRequest, HttpResponse.BodyHandlers.ofString());
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<TokenResponse> dataAdapter = moshi.adapter(TokenResponse.class);
    this.token = dataAdapter.fromJson(response.body()).access_token();
  }

  /**
   * Method that generates the token and then returns the token as a string
   *
   * @return the Spotify API token as a string
   */
  public String getToken() {
    try{
      this.generateToken();
    }
    catch(Exception e){
      System.out.println(e.toString());
    }
    return this.token;
  }




}
