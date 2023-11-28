package edu.brown.cs.student.main.server.spotify.data;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import edu.brown.cs.student.main.server.spotify.records.audioFeaturesRecords.FeaturesProp;
import edu.brown.cs.student.main.server.spotify.records.recommendationRecords.Recommendation;
import edu.brown.cs.student.main.server.spotify.records.searchRecords.Song;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

/**
 * Class that will be used to get the needed data from the spotify API, allowing
 * our server to use it accordingly.
 */
public class SpotifyData implements IData {

  /**
   * Constructor for the SpotifyData class.
   */
  public SpotifyData() {

  }

  /**
   * Method that will allow the server to get a song object based on the name of the songs
   * inputted
   *
   * @return a song object based on the name of the song inputted
   */
  @Override
  public Song getSong() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getSong'");
  }

  /**
   * Method that will allow the server to get a recommendation based on a seed song, and
   * different metadata values inputted.
   *
   * @return a recommendation object.
   */
  @Override
  public Recommendation getRecommendation() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getRecommendation'");
  }



  @Override
  public FeaturesProp getFeatures() throws Exception{
    return null;
  }

  /**
   * Helper method that makes a request from API to get needed information and turns
   * it into a Song object.
   *
   * @param uriString String that is the URI where we get the information from.
   * @return Song object which is the contents of the Json returned
   *         from the API called.
   * @throws URISyntaxException   exception where URI syntax is incorrect.
   * @throws IOException          exception where it failed to read/open
   *                              information.
   * @throws InterruptedException exception where connection to API is
   *                              interrupted.
   */
  private Song fetchSongApiData(String uriString)
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

    // making target type and then using Moshi to turn it into a Song object
    Type targetClassType = Types.newParameterizedType(Song.class);
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Song> dataAdapter = moshi.adapter(targetClassType);
    return dataAdapter.fromJson(response.body());
  }

  /**
   * Helper method that makes a request from API to get needed information and turns
   * it into a Recommendation object.
   *
   * @param uriString String that is the URI where we get the information from.
   * @return Recommendation object which is the contents of the Json returned
   *         from the API called.
   * @throws URISyntaxException   exception where URI syntax is incorrect.
   * @throws IOException          exception where it failed to read/open
   *                              information.
   * @throws InterruptedException exception where connection to API is
   *                              interrupted.
   */
  private Recommendation fetchRecommendationApiData(String uriString)
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

    // making target type and then using Moshi to turn it into a Recommendation object
    Type targetClassType = Types.newParameterizedType(Recommendation.class);
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Recommendation> dataAdapter = moshi.adapter(targetClassType);
    return dataAdapter.fromJson(response.body());
  }



  /**
   * Helper method that makes a request from API to get needed information and turns
   * it into a FeaturesProp object.
   *
   * @param uriString String that is the URI where we get the information from.
   * @return FeaturesProp object which is the contents of the Json returned
   *         from the API called.
   * @throws URISyntaxException   exception where URI syntax is incorrect.
   * @throws IOException          exception where it failed to read/open
   *                              information.
   * @throws InterruptedException exception where connection to API is
   *                              interrupted.
   */
  private FeaturesProp fetchFeaturesApiData(String uriString)
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

    // making target type and then using Moshi to turn it into a Song object
    Type targetClassType = Types.newParameterizedType(FeaturesProp.class);
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<FeaturesProp> dataAdapter = moshi.adapter(targetClassType);
    return dataAdapter.fromJson(response.body());
  }



}
