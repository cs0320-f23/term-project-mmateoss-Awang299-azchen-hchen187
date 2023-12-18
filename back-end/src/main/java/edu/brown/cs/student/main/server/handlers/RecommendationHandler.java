package edu.brown.cs.student.main.server.handlers;

import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.server.lyrics.data.ILyricsData;
import edu.brown.cs.student.main.server.spotify.data.IData;
import edu.brown.cs.student.main.server.spotify.records.recommendationRecords.Recommendation;
import edu.brown.cs.student.main.server.spotify.records.recommendationRecords.TrackProps;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
  private ILyricsData lyricsData;

  /** The constructor for the RecommendationHandler class. */
  public RecommendationHandler(IData spotifyData, ILyricsData lyricsData) {
    this.spotifyData = spotifyData;
    this.lyricsData = lyricsData;
  }

  /**
   * Handle method that takes care of getting the query parameters and then building a
   * recommendation based on the inputted songs. The recommendation will be
   *
   * @param req request object used to get the query parameters.
   * @param res response object used to create a response.
   * @return returns what is mapped to the server. Usually the broadband percentage, times info is
   *     accessed, and other helpful things, but if an error occurs that is returned.
   */
  @Override
  public Object handle(Request req, Response res) {

    // create variables that will be used throughout
    Moshi moshi = new Moshi.Builder().build();
    Set<String> params = req.queryParams();
    String token = req.queryParams("token");
    String limit = req.queryParams("limit");
    String variability = req.queryParams("variability");
    // getting an array of strings as a parameter
    String[] allNames = req.queryParamsValues("allNames");

    // defensive programming, checking that everything that needed to be inputted
    // was inputted
    if (params.size() != 4 || variability == null || token == null || limit == null) {
      Map<String, Object> responseMap = new HashMap<>();
      responseMap.put("Result", "Error");
      responseMap.put(
          "Message",
          "please ensure that that you passed in a variability,"
              + " token, limit, and list of song names for generating recommendations");
      return moshi.adapter(Map.class).toJson(responseMap);
    }
    if (allNames == null || allNames.length == 0) {
      Map<String, Object> responseMap = new HashMap<>();
      responseMap.put("Result", "Error");
      responseMap.put(
          "Message",
          "please ensure that you passed in a list of song names"
              + " as a parameter in order to generate recommendations");
      return moshi.adapter(Map.class).toJson(responseMap);
    }
    if (limit.equals("0") || Integer.valueOf(limit) > 100 || Integer.valueOf(limit) < 0) {
      Map<String, Object> responseMap = new HashMap<>();
      responseMap.put("Result", "Error");
      responseMap.put("Message", "the limit must be an integer in the range 1-100");
      return moshi.adapter(Map.class).toJson(responseMap);
    }

    try {
      // set the token
      this.spotifyData.setToken(token);

      Recommendation rec =
          this.RecommendationAlgorithm(Integer.parseInt(limit), 1, 10, allNames, variability);

      return moshi.adapter(Recommendation.class).toJson(rec);
      // Map<String, Object> responseMap = new HashMap<>();
      // responseMap.put("Result", "Success");
      // responseMap.put("Message", rec);
      // return moshi.adapter(Map.class).toJson(responseMap);

    } // error handling -- taking care of any possible exception and returning
    // informative message
    catch (ExecutionException e) {
      Map<String, Object> responseMap = new HashMap<>();
      responseMap.put("Result", "Error");
      responseMap.put("Message", "failed to get object from within the Cache");
      return moshi.adapter(Map.class).toJson(responseMap);

    } catch (IOException e) {
      Map<String, Object> responseMap = new HashMap<>();
      responseMap.put("Result", "Error");
      responseMap.put("Message", "Failed to reach Spotify API server");
      return moshi.adapter(Map.class).toJson(responseMap);

    } catch (URISyntaxException e) {
      Map<String, Object> responseMap = new HashMap<>();
      responseMap.put("Result", "Error");
      responseMap.put(
          "Message",
          "Malformed URI for API calls, please check the syntax of" + "the created URIs");
      return moshi.adapter(Map.class).toJson(responseMap);

    } catch (InterruptedException e) {
      Map<String, Object> responseMap = new HashMap<>();
      responseMap.put("Result", "Error");
      responseMap.put(
          "Message",
          "connection interrupted, while working to get data from " + "Spotify API server");
      return moshi.adapter(Map.class).toJson(responseMap);

    } catch (Exception e) {
      Map<String, Object> responseMap = new HashMap<>();
      responseMap.put("Result", "Error");
      responseMap.put("Message", e.getMessage());
      return moshi.adapter(Map.class).toJson(responseMap);
    }
  }

  /**
   * @param maxRecommendations- the maximum number of recommendations to be returned
   * @param minRecommendations- the minimum number of recommendations to be returned
   * @param maxAttempts- maximum Spotify API attempts until error is thrown
   * @param allNames- String[] representing Spotify API parameter
   * @param variability- String representing Spotify API parameter
   * @return Recommednation record containing the recommednations in a List<TrackProps>
   * @throws Exception - Thrown when maxAttempts exceeded
   */
  private Recommendation RecommendationAlgorithm(
      int maxRecommendations,
      int minRecommendations,
      int maxAttempts,
      String[] allNames,
      String variability)
      throws Exception {
    // making sure at least 1 recommendation is returned
    int attempts = 0;
    Recommendation rec = new Recommendation(new ArrayList<>(), new ArrayList<>());
    List<TrackProps> filteredTracks =
        new ArrayList<TrackProps>(); // Ensures our Recommendation's order
    Set<TrackProps> setTracks =
        new HashSet<TrackProps>(); // Ensures only unique songs recommended by Spotify counted
    while (filteredTracks.size() < minRecommendations) {
      // Return an error after 10 unsuccessful attempts
      if (attempts == maxAttempts) {
        throw new Exception(
            "Recommendation cannot be built from the passed in "
                + "variability, please increase variability to generate recommendation");
      }
      attempts += 1;

      // Get at least 50 Spotify song recommendations
      rec =
          this.spotifyData.getRecommendation(
              String.valueOf(Math.max(50, maxRecommendations)), allNames, variability);
      // Recommendation processed = rec;
      Recommendation processed = this.spotifyData.postProcess(rec, allNames);
      // Checks that a lyric for the song exists
      for (TrackProps track : processed.tracks()) {
        if (filteredTracks.size() >= maxRecommendations) {
          break;
        } else if (!setTracks.contains(track) && this.lyricsData.LyricsExist(track.id())) {
          filteredTracks.add(track);
          setTracks.add(track);
        }
      }
    }
    return new Recommendation(rec.seeds(), filteredTracks);
  }
}
