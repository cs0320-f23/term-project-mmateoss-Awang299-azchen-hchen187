package edu.brown.cs.student.main.server.handlers;

import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.server.spotify.data.IData;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Class that takes care of handling the logic for
 * when someone calls the getsong endpoint of the server.
 */
public class GetSongHandler implements Route {

  private IData data;

  /**
   * Constructor for the GetSongHandler class.
   *
   * @param data IData object for getting data either mocked or real.
   */
  public GetSongHandler(IData data){
    this.data = data;
  }


  /**
   * Handle method that takes care of getting the query parameters and then building a
   * List of List of strings based on how the users are searching for the
   *
   * @param req request object used to get the query parameters.
   * @param res response object used to create a response.
   *
   * @return returns what is mapped to the server. A song object in this case
   */
  @Override
  public Object handle(Request req, Response res){
    Moshi moshi = new Moshi.Builder().build();

    Map<String, Object> responseMap = new HashMap<>();
    Set<String> params = req.queryParams();
    String token = req.queryParams("token");
    String limit = req.queryParams("limit");
    String query = req.queryParams("query");

    // defensive programming
    if(params.size() != 3 || token == null || limit == null || query == null){
      responseMap.put("Result", "Error");
      responseMap.put("Error Message", "Please ensure you pass in a token, limit, and query as"
          + "parameters");
      return moshi.adapter(Map.class).toJson(responseMap);
    }
    try{

      this.data.setToken(token);
      List<List<String>> result = this.data.getSongsPrompt(query,limit);
      responseMap.put("Result", "Success");
      responseMap.put("data", result);
      return moshi.adapter(Map.class).toJson(responseMap);

    } catch (IOException e){
      responseMap.put("Result", "Error");
      responseMap.put("Error Message", "Failed to reach Spotify API server");
      return moshi.adapter(Map.class).toJson(responseMap);


    } catch (URISyntaxException e){
      responseMap.put("Result", "Error");
      responseMap.put("Error Message", "Malformed URI for API calls, please check the syntax of"
          + "the created URIs");
      return moshi.adapter(Map.class).toJson(responseMap);


    } catch (InterruptedException e){
      responseMap.put("Result", "Error");
      responseMap.put("Error Message", "connection interrupted, while working to get data from "
          + "Spotify API server");
      return moshi.adapter(Map.class).toJson(responseMap);

    } catch (Exception e){
      responseMap.put("Result", "Error");
      responseMap.put("Error Message", e.getMessage());
      return moshi.adapter(Map.class).toJson(responseMap);

    }

  }


}
