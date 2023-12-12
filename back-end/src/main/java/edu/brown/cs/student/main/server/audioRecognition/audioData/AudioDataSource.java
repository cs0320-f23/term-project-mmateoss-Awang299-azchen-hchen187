package edu.brown.cs.student.main.server.audioRecognition.audioData;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.server.audioRecognition.audioRecords.audioObjRecords.AudioObj;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Class used to get text data from an audio file
 */
public class AudioDataSource {


  /**
   * Constructor for the MockedAudioDataSource class.
   */
  public AudioDataSource(){

  }


  /**
   * Method that makes the request to parse the audioObject to a text version of what it is.
   *
   * @param token Deepgram API token.
   * @param encodedAudio Base64 encoded version of the audio, allowing us to send it securly
   *                     through an API call.
   * @return An audio Object that contains the parsed response from the API model.
   *
   * @throws URISyntaxException exception where URI syntax is incorrect.
   * @throws IOException exception where it failed to read/open
   *                     information.
   * @throws InterruptedException exception where connection to API is
   *                              interrupted.
   */
  public AudioObj getAudioText(String token, byte[] encodedAudio)
      throws URISyntaxException, IOException, InterruptedException{

    String uriString
        = "https://api.deepgram.com/v1/listen?detect_language=true&filler_words=false&summarize=v2";
    AudioObj parsedAudio = this.fetchAudioApiData(uriString, token,encodedAudio);

    return parsedAudio;
  }




  /**
   * Helper method that makes a request from API to get needed information and turns
   * it into an AudioObj object.
   *
   * @param uriString String that is the URI where we get the information from.
   * @return AudioObj object which is the contents of the Json returned
   *         from the API called.
   * @throws URISyntaxException   exception where URI syntax is incorrect.
   * @throws IOException          exception where it failed to read/open
   *                              information.
   * @throws InterruptedException exception where connection to API is
   *                              interrupted.
   *
   * https://github.com/deepgram-starters/deepgram-java-starters/blob/main/Starter-01/src/main/java/com/deepgram/App.java
   */
  private AudioObj fetchAudioApiData(String uriString, String token, byte[] encodedAudio)
      throws URISyntaxException, IOException, InterruptedException {

    // building a new HttpRequest
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(uriString))
        .header("accept", "application/json")
        .header("Content-Type", "audio/mpeg")
        .header("Authorization", "Token " + token)
        .POST(HttpRequest.BodyPublishers.ofByteArray(encodedAudio))
        .build();
    HttpResponse<String> response = HttpClient.newHttpClient().send(request,
        HttpResponse.BodyHandlers.ofString());

    //using Moshi to turn it into a Song object
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<AudioObj> dataAdapter = moshi.adapter(AudioObj.class);
    return dataAdapter.fromJson(response.body());

  }

}
