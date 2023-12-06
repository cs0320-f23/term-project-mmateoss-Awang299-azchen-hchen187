package edu.brown.cs.student.main.server.handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import edu.brown.cs.student.main.server.spotify.data.IData;
import edu.brown.cs.student.main.server.spotify.data.SpotifyData;

import edu.brown.cs.student.main.server.spotify.records.recommendationRecords.Recommendation;
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
import java.util.concurrent.ExecutionException;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * This is the RecommendationHandler class. It takes care of everything that has to do with the
 * recommendation endpoint getting called.
 */
public class RecommendationHandler implements Route {
  private IData spotifyData;

  /**
   * The constructor for the RecommendationHandler class.
   */
  public RecommendationHandler(IData spotifyData) {
    this.spotifyData = spotifyData;
  }

  /**
   * Handle method that takes care of getting the query parameters and then building a
   * recommendation based on the inputted songs. The recommendation will be
   *
   * @param req request object used to get the query parameters.
   * @param res response object used to create a response.
   * @return returns what is mapped to the server. Usually the broadband percentage, times info is
   *        accessed, and other helpful things, but if an error occurs that is returned.
   */
  @Override
  public Object handle(Request req, Response res){

    // create variables that will be used throughout
    Moshi moshi = new Moshi.Builder().build();
    Set<String> params = req.queryParams();
    String token = req.queryParams("token");
    String limit = req.queryParams("limit");
    String variability = req.queryParams("variability");
    // getting an array of strings as a parameter
    String[] allNames = req.queryParamsValues("allNames");

    // defensive programming, checking that everything that needed to be inputted was inputted
    if(params.size() != 4 || variability == null || token == null || limit == null){
      Map<String, Object> responseMap = new HashMap<>();
      responseMap.put("Result", "Error");
      responseMap.put("Error Message", "please ensure that that you passed in a variability,"
          + " token, limit, and list of song names for generating recommendations");
      return moshi.adapter(Map.class).toJson(responseMap);
    }
    if(allNames == null || allNames.length == 0){
      Map<String, Object> responseMap = new HashMap<>();
      responseMap.put("Result", "Error");
      responseMap.put("Error Message", "please ensure that you passed in a list of song names"
          + " as a parameter in order to generate recommendations");
      return moshi.adapter(Map.class).toJson(responseMap);
    }
    //TODO: check to make sure that variability is greater than 0
    if(limit.equals("0") || Integer.valueOf(limit) > 100 || Integer.valueOf(limit) < 0){
      Map<String, Object> responseMap = new HashMap<>();
      responseMap.put("Result", "Error");
      responseMap.put("Error Message", "the limit must be an integer in the range 1-100");
      return moshi.adapter(Map.class).toJson(responseMap);
    }

    try{
      // returning and generating the actual recommendation
      this.spotifyData.setToken(token);
      Recommendation rec = this.spotifyData.getRecommendation(limit, allNames, variability);
      // making sure recommendation returns something
      if(rec.tracks().size() == 0){
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("Result", "Error");
        responseMap.put("Error Message", "Recommendation cannot be built from the passed in "
            + "variability, please increase variability to generate recommendation");
        return moshi.adapter(Map.class).toJson(responseMap);
      }
      // returning the recommendation
      return moshi.adapter(Recommendation.class).toJson(rec);

    } // error handling -- taking care of any possible exception and returning informative message
    catch (ExecutionException e){
      Map<String, Object> responseMap = new HashMap<>();
      responseMap.put("Result", "Error");
      responseMap.put("Error Message", "failed to get object from within the Cache");
      return moshi.adapter(Map.class).toJson(responseMap);

    } catch (IOException e){
      Map<String, Object> responseMap = new HashMap<>();
      responseMap.put("Result", "Error");
      responseMap.put("Error Message", "Failed to reach Spotify API server");
      return moshi.adapter(Map.class).toJson(responseMap);

    } catch (URISyntaxException e){
      Map<String, Object> responseMap = new HashMap<>();
      responseMap.put("Result", "Error");
      responseMap.put("Error Message", "Malformed URI for API calls, please check the syntax of"
          + "the created URIs");
      return moshi.adapter(Map.class).toJson(responseMap);

    } catch (InterruptedException e){
      Map<String, Object> responseMap = new HashMap<>();
      responseMap.put("Result", "Error");
      responseMap.put("Error Message", "connection interrupted, while working to get data from "
          + "Spotify API server");
      return moshi.adapter(Map.class).toJson(responseMap);

    } catch(Exception e){
      Map<String, Object> responseMap = new HashMap<>();
      responseMap.put("Result", "Error");
      responseMap.put("Error Message", e.getMessage());
      return moshi.adapter(Map.class).toJson(responseMap);
    }

  }

}
