package edu.brown.cs.student.main.server.handlers;

import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.server.audioRecognition.audioData.IAudioData;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * AudioTextHandler class which takes care of all the logic for the AudioText handler of the server.
 */
public class AudioTextHandler implements Route {

  private IAudioData audioData;

  /**
   * Constructor for the AudioTextHandler
   *
   * @param audioData data which allows us to use mocked or real data.
   */
  public AudioTextHandler(IAudioData audioData) {
    this.audioData = audioData;
  }

  /**
   * Handle method that takes care of getting the query parameters and then building a token for the
   * frontend to use.
   *
   * @param req request object used to get the query parameters.
   * @param res response object used to create a response.
   * @return returns what is mapped to the server. A song object in this case
   */
  @Override
  public Object handle(Request req, Response res) {

    Map<String, String> responseMap = new HashMap<>();
    Moshi moshi = new Moshi.Builder().build();

    try {
      Set<String> params = req.queryParams();
      String token = req.queryParams("token");
      byte[] bytes = req.bodyAsBytes();

      if (params.size() != 1 || token == null || bytes == null) {
        responseMap.put("Result", "Error");
        responseMap.put("Error Message", "please only pass in a token and language as params");
        ;
        return moshi.adapter(Map.class).toJson(responseMap);
      } else {
        String encoded = Base64.getEncoder().encodeToString(bytes);
        String transcript = this.audioData.getTranslation(token, encoded);
        responseMap.put("Result", "Success");
        responseMap.put("transcript", transcript);
        return moshi.adapter(Map.class).toJson(responseMap);
      }
    } // catching any possible exception that could have been thrown by the code
    catch (IOException e) {
      responseMap.put("Result", "Error");
      responseMap.put(
          "Error Message",
          "Please make a post request of a json object with"
              + " your base64 encoded audio as the value to the key data");
      return moshi.adapter(Map.class).toJson(responseMap);
    } catch (URISyntaxException e) {
      responseMap.put("Result", "Error");
      responseMap.put(
          "Error Message",
          "Malformed URI for API calls, please ensure"
              + "passed in audio is a Base64 encoded string of your audio file");
      return moshi.adapter(Map.class).toJson(responseMap);
    } catch (InterruptedException e) {
      responseMap.put("Result", "Error");
      responseMap.put(
          "Error Message",
          "connection interrupted, while working to get data from " + "DeepGram API server");
      return moshi.adapter(Map.class).toJson(responseMap);
    } catch (IllegalStateException e) {
      responseMap.put("Result", "Error");
      responseMap.put(
          "Error Message",
          "Audio either had no words, model could not find words in" + " the audio");
      return moshi.adapter(Map.class).toJson(responseMap);
    } catch (Exception e) {
      responseMap.put("Result", "Error");
      responseMap.put("Error Message", e.getMessage());
      return moshi.adapter(Map.class).toJson(responseMap);
    }
  }
}
