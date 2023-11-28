package edu.brown.cs.student.main.server.spotify.handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import edu.brown.cs.student.main.server.spotify.data.IData;
import edu.brown.cs.student.main.server.spotify.data.SpotifyData;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 *
 */
public class RecommendationHandler implements Route {
  private IData spotifyData;

  /**
   *
   */
  public RecommendationHandler(IData spotifyData) {
    this.spotifyData = spotifyData;
  }

  /**
   *
   * @param req
   * @param resp
   * @return
   * @throws Exception
   */
  @Override
  public Object handle(Request req, Response resp) throws Exception {

    // create variables that will be used throughout
    Moshi moshi = new Moshi.Builder().build();
    Set<String> params = req.queryParams();
    String token = req.queryParams("token");
    String seedTrack = req.queryParams("seedTrack");
    String variability = req.queryParams("variability");

    if (variability == null) {

    } else {

    }
    if (token == null || seedTrack == null) {
      Map<String, Object> responseMap = new HashMap<>();
      responseMap.put("Result", "Error");
      responseMap.put("Error Message", "Need a Seed Track or Token in order to make a request");
      return moshi.adapter(Map.class).toJson(responseMap);
    }

    return null;
  }

  /**
   * Helper method that makes a request from an API to get needed information for
   * return of handle.
   *
   * @param uriString String that is the URI where we get the information from.
   * @return List of Lists of Strings which are the contents of the Json returned
   *         from
   *         the API called.
   * @throws URISyntaxException   exception where URI syntax is incorrect.
   * @throws IOException          exception where it failed to read/open
   *                              information.
   * @throws InterruptedException exception where connection to API is
   *                              interrupted.
   */
  private List<List<String>> fetchApiData(String uriString)
      throws URISyntaxException, IOException, InterruptedException {
    // building a new HttpRequest
    HttpRequest buildRequest = HttpRequest.newBuilder()
        .uri(new URI(uriString))
        .GET()
        .build();
    // building a response with the HttpRequest
    HttpResponse<String> response = HttpClient.newBuilder()
        .build()
        .send(buildRequest, HttpResponse.BodyHandlers.ofString());

    // making target type and then using Moshi to turn it into a List of lists of
    // strings
    Type targetClassType = Types.newParameterizedType(List.class,
        Types.newParameterizedType(List.class, String.class));
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<List<List<String>>> dataAdapter = moshi.adapter(targetClassType);

    return dataAdapter.fromJson(response.body());

  }
}
