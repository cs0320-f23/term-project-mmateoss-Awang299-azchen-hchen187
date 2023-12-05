package edu.brown.cs.student.main.server.spotify.handlers;

import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.server.spotify.tokens.TokenGenerator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * TokenHandler class, used to allow the frontend to get access to a spotify access token which
 * they can then use to get a playback sdk or to make other calls to the backend.
 */
public class TokenHandler implements Route {

  private TokenGenerator generator;


  /**
   * Constructor for the TokenHandler
   */
  public TokenHandler(){
    this.generator = new TokenGenerator();

  }

  /**
   * Handle method that takes care of getting the query parameters and then building a
   * Song based on the string.
   *
   * @param req request object used to get the query parameters.
   * @param res response object used to create a response.
   *
   * @return returns what is mapped to the server. A song object in this case
   */
  @Override
  public Object handle(Request req, Response res){
    Moshi moshi = new Moshi.Builder().build();
    Map<String, String> responseMap = new HashMap<>();
    Set<String> params = req.queryParams();
    try{
      if(params.size() > 0){
        responseMap.put("Result", "Error");
        responseMap.put("Error message", "please do not include any parameters when trying to"
            + "get the token");
        return moshi.adapter(Map.class).toJson(responseMap);
      } else{

        String token = this.generator.getToken();
        responseMap.put("Result", "Success");
        responseMap.put("token", token);
        return moshi.adapter(Map.class).toJson(responseMap);
      }
    } // catching any possible exception that could have been thrown by the code
    catch(Exception e){
      responseMap.clear();
      responseMap.put("Result", "Error");
      responseMap.put("Error message", e.getMessage());
      return moshi.adapter(Map.class).toJson(responseMap);
    }
  }


}
