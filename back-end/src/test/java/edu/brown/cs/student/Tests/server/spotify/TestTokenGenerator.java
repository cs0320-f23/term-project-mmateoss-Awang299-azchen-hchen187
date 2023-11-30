package edu.brown.cs.student.Tests.server.spotify;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.server.spotify.records.audioFeaturesRecords.FeaturesProp;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpResponse;
import java.util.Base64;

public class TestTokenGenerator {

  private String client_id;
  private String client_secret;
  private String combined;

  private String token;

  public TestTokenGenerator(){
    this.client_id = "";
    this.client_secret = "";
    this.combined = this.client_id+":"+this.client_secret;

  }

  // learned how to create base64 encoded string: https://www.baeldung.com/java-base64-encode-and-decode
  // what and how to use bodyPublishers
  // https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/HttpRequest.BodyPublishers.html
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
