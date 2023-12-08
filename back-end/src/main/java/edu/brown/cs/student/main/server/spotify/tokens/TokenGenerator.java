package edu.brown.cs.student.main.server.spotify.tokens;

import io.github.cdimascio.dotenv.Dotenv;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.server.spotify.records.searchRecords.Song;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Class that serves as a way of generating a spotify token for testing
 */
public class TokenGenerator implements IToken {

  public String client_id;
  public String client_secret;
  private String combined;
  private LoadingCache<String, String> tokenCache;

  /**
   * Constructor for the TokenGenerator class
   */
  public TokenGenerator() {
    // TODO: Input your client_id and client_secret here to generate tokens for
    // testing
    Dotenv dotenv = Dotenv.load();
    this.client_id = dotenv.get("SPOTIFY_CLIENT_ID");
    this.client_secret = dotenv.get("SPOTIFY_CLIENT_SECRET");
    this.combined = this.client_id + ":" + this.client_secret;

    // building the cache that will hold a token for an hour, which is how long
    // before it needs to
    // be regenerated
    this.tokenCache = CacheBuilder.newBuilder()
        .maximumSize(1)
        .expireAfterWrite(1, TimeUnit.HOURS)
        .recordStats().build(
            new CacheLoader<String, String>() {
              @Override
              public String load(String tokenName) throws Exception {

                return generateToken();
              }
            });

  }

  // learned how to create base64 encoded string:
  // https://www.baeldung.com/java-base64-encode-and-decode
  // what and how to use bodyPublishers
  // https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/HttpRequest.BodyPublishers.html

  /**
   * Method that makes an API call to the spotify API and sets the token to the
   * instance variable
   *
   * @throws URISyntaxException   exception where URI syntax is incorrect.
   * @throws IOException          exception where it failed to read/open
   *                              information.
   * @throws InterruptedException exception where connection to API is
   *                              interrupted.
   */
  private String generateToken() throws IOException, InterruptedException, URISyntaxException {
    String uriString = "https://accounts.spotify.com/api/token";
    String base64Encoded = Base64.getEncoder().encodeToString(this.combined.getBytes());

    HttpRequest buildRequest = HttpRequest.newBuilder()
        .uri(new URI(uriString))
        .header("Authorization", "Basic " + base64Encoded)
        .header("Content-Type", "application/x-www-form-urlencoded")
        .POST(HttpRequest.BodyPublishers.ofString("grant_type=client_credentials"))
        .build();
    // building a response with the HttpRequest
    HttpResponse<String> response = HttpClient.newBuilder()
        .build()
        .send(buildRequest, HttpResponse.BodyHandlers.ofString());
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<TokenResponse> dataAdapter = moshi.adapter(TokenResponse.class);
    return dataAdapter.fromJson(response.body()).access_token();
  }

  /**
   * Method that generates the token and then returns the token as a string
   *
   * @return the Spotify API token as a string
   */
  @Override
  public String getToken() throws ExecutionException {

    String token = "";
    // getting the token from the cache
    token = this.tokenCache.get("token");
    return token;
  }

}
