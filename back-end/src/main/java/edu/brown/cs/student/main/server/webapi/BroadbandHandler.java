package edu.brown.cs.student.main.server.webapi;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import spark.Request;
import spark.Response;
import spark.Route;


/**
 * A class that handles the logic for returning the BroadBand percentage to the user.
 */
public class BroadbandHandler implements Route {

  private CachedData cachedData;
  Map<String, Object> responseMap;
  boolean cacheInitialized;

  /**
   * Constructor for the BroadbandHandler class. Here we construct our cache.
   */
  public BroadbandHandler() {
    this.responseMap = new HashMap<>();
    this.cacheInitialized = false;
    try {
      this.cachedData = new CachedData(20, 1);
    } catch (IOException e) {
      this.responseMap.put("result", "error_bad_request");
    } catch (URISyntaxException | InterruptedException e) {
      this.responseMap.put("result", "error_bad_json");
    }
  }

  /**
   * Handle method that takes care of getting the query parameters and then finding the broadband
   * percentage at the county that was inputted by those parameters. Utilizes the cache to get that
   * information.
   *
   * @param req request object used to get the query parameters.
   * @param res response object used to create a response.
   * @return returns what is mapped to the server. Usually the broadband percentage, times info is
   *        accessed, and other helpful things, but if an error occurs that is returned.
   * @throws Exception generic exception which is caught in order to not crash the server.
   */
  @Override
  public Object handle(Request req, Response res) throws Exception {
    this.responseMap.clear();

    // create variables that will be used throughout
    Set<String> params = req.queryParams();
    String state = req.queryParams("state");
    String county = req.queryParams("county");
    Moshi moshi = new Moshi.Builder().build();

    // if construction of the cache threw any errors and were caught in constructor,
    // handle them here.
    if (this.cacheInitialized == false) {
      this.cacheInitialized = true;
      if (this.responseMap.size() > 0) {
        responseMap.put("query parameters", params);
        return moshi.adapter(Map.class).toJson(this.responseMap);
      }
    }

    // case where there is a bad request
    if (params.size() != 2 || !params.contains("state") || !params.contains("county")) {
      this.responseMap.put("result", "error_bad_request");
      this.responseMap.put("query parameters", params);
      return moshi.adapter(Map.class).toJson(this.responseMap);
    }

    try {
      // filling the response map with all the needed info
      this.responseMap.put("result", "success");
      this.responseMap.put("state", state);
      this.responseMap.put("county", county);
      this.responseMap.put("percentage", this.cachedData.getBroadBandInfo(state, county)[0]);
      this.responseMap.put("broadband data time retrieved",
          this.cachedData.getBroadBandInfo(state, county)[1]);

      for (String param : params) {
        responseMap.put(param, req.queryParams(param));
      }

      return moshi.adapter(Map.class).toJson(this.responseMap);

    } catch (IllegalArgumentException | ExecutionException e) {
      // clearing map before so that it does not contain the things put before error was reached
      this.responseMap.clear();
      this.responseMap.put("result", "error_bad_request");
      this.responseMap.put("query parameters", params);
      return moshi.adapter(Map.class).toJson(this.responseMap);
    }
  }

  /**
   * Helper method that makes a request from an API to get needed information for return of handle.
   *
   * @param uriString String that is the URI where we get the information from.
   * @return List of Lists of Strings which are the contents of the Json returned from
   *        the API called.
   * @throws URISyntaxException exception where URI syntax is incorrect.
   * @throws IOException exception where it failed to read/open information.
   * @throws InterruptedException exception where connection to API is interrupted.
   */
  private List<List<String>> fetchApiData(String uriString)
      throws URISyntaxException, IOException, InterruptedException {
    //building a new HttpRequest
    HttpRequest buildRequest = HttpRequest.newBuilder()
        .uri(new URI(uriString))
        .GET()
        .build();
    // building a response with the HttpRequest
    HttpResponse<String> response = HttpClient.newBuilder()
        .build()
        .send(buildRequest, HttpResponse.BodyHandlers.ofString());

    // making target type and then using Moshi to turn it into a List of lists of strings
    Type targetClassType = Types.newParameterizedType(List.class,
        Types.newParameterizedType(List.class, String.class));
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<List<List<String>>> dataAdapter = moshi.adapter(targetClassType);

    return dataAdapter.fromJson(response.body());
  }

}

