package edu.brown.cs.student.main.server.spotify.data;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.server.spotify.records.audioFeaturesRecords.FeaturesProp;
import edu.brown.cs.student.main.server.spotify.records.recommendationRecords.Recommendation;
import edu.brown.cs.student.main.server.spotify.records.searchRecords.Song;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Class that will be used to get the needed data from the spotify API, allowing our server to use
 * it accordingly.
 */
public class SpotifyData {

  /** Constructor for the SpotifyData class. */
  public SpotifyData() {}

  /**
   * Method that will allow the server to get a song object based on the name of the songs inputted
   *
   * @param songName the name of the song that we are searching for
   * @param token the Spotify token for the user
   * @return a song object based on the name of the song inputted
   * @throws URISyntaxException exception where URI syntax is incorrect.
   * @throws IOException exception where it failed to read/open information.
   * @throws InterruptedException exception where connection to API is interrupted.
   *     <p>Learned about replaceAll here: https://www.javatpoint.com/java-string-replaceall
   */
  public Song getSong(String token, String songName)
      throws URISyntaxException, IOException, InterruptedException {

    // Need to replace all spaces with %20 to allow it to be a correctly formed URL
    String correctedSongName = songName.replaceAll(" ", "%20");

    // Generate the UriString to make the recommendation
    // track: may possibly need to be track%3
    String uriString =
        "https://api.spotify.com/v1/search?q=track:" + correctedSongName + "&type=track";

    // return the song object
    return this.fetchSongApiData(uriString, token);
  }

  /**
   * Method that will allow the server to get a recommendation based on a seed song, and different
   * metadata values inputted.
   *
   * @param token Spotify token for the user
   * @param limit max number of recommended songs
   * @param max_acousticness max acousticness of songs recommended
   * @param min_acousticness min acousticness of songs recommended
   * @param max_danceability max dancability of songs recommended
   * @param max_energy max energy of songs recommended
   * @param max_speechiness max speechiness of songs reccomended
   * @param max_valence max valence of songs recommended
   * @param min_danceability min dancability of songs recommended
   * @param min_energy min energy of songs recommended
   * @param min_speechiness min speechiness of songs recommended
   * @param min_valence min valence of songs recommended
   * @param seed_tracks seed_tracks of songs recommended
   * @return a recommendation object.
   * @throws URISyntaxException exception where URI syntax is incorrect.
   * @throws IOException exception where it failed to read/open information.
   * @throws InterruptedException exception where connection to API is interrupted.
   */
  public Recommendation getRecommendation(
      String token,
      String limit,
      String seed_tracks,
      String min_acousticness,
      String max_acousticness,
      String min_danceability,
      String max_danceability,
      String min_energy,
      String max_energy,
      String min_speechiness,
      String max_speechiness,
      String min_valence,
      String max_valence)
      throws URISyntaxException, IOException, InterruptedException {

    String uriString =
        "https://api.spotify.com/v1/recommendations?"
            + "limit="
            + limit
            + "&seed_tracks="
            + seed_tracks
            + "&min_acousticness="
            + min_acousticness
            + "&max_acousticness="
            + max_acousticness
            + "&min_danceability="
            + min_danceability
            + "&max_danceability="
            + max_danceability
            + "&min_energy="
            + min_energy
            + "&max_energy="
            + max_energy
            + "&min_speechiness="
            + min_speechiness
            + "&max_speechiness="
            + max_speechiness
            + "&min_valence="
            + min_valence
            + "&max_valence="
            + max_valence;

    return this.fetchRecommendationApiData(uriString, token);
  }

  /**
   * Method that returns a song object solely based on any word passed in, could be the name of the
   * song or the name of the artist or just a letter.
   *
   * @param token API token to allow calls to the spotify api
   * @param prompt what is being searched for
   * @return A song object that contains multiple songs that match that.
   * @throws URISyntaxException exception where URI syntax is incorrect.
   * @throws IOException exception where it failed to read/open information.
   * @throws InterruptedException exception where connection to API is interrupted.
   */
  public Song getSongKeywords(String token, String prompt, String limit)
      throws URISyntaxException, IOException, InterruptedException {

    // Need to replace all spaces with %20 to allow it to be a correctly formed URL
    String correctedPrompt = prompt.replaceAll(" ", "%20");

    String uriString =
        "https://api.spotify.com/v1/search?q="
            + correctedPrompt
            + "&type=track%2Cartist&limit="
            + limit;

    return this.fetchSongApiData(uriString, token);
  }

  /**
   * Method that returns a featuresProp object for all the inputted songs
   *
   * @param token the Spotify token for the user
   * @return the featureProp object containing info for all the songs
   * @throws URISyntaxException exception where URI syntax is incorrect.
   * @throws IOException exception where it failed to read/open information.
   * @throws InterruptedException exception where connection to API is interrupted.
   */
  public FeaturesProp getFeatures(String token, String ids)
      throws URISyntaxException, IOException, InterruptedException {

    // String ids = this.getSongIds(allNames, token);
    String uriString = "https://api.spotify.com/v1/audio-features?ids=" + ids;
    return this.fetchFeaturesApiData(uriString, token);
  }

  /**
   * Helper method that makes a request from API to get needed information and turns it into a Song
   * object.
   *
   * @param uriString String that is the URI where we get the information from.
   * @return Song object which is the contents of the Json returned from the API called.
   * @throws URISyntaxException exception where URI syntax is incorrect.
   * @throws IOException exception where it failed to read/open information.
   * @throws InterruptedException exception where connection to API is interrupted.
   */
  private Song fetchSongApiData(String uriString, String token)
      throws URISyntaxException, IOException, InterruptedException {
    // building a new HttpRequest
    HttpRequest buildRequest =
        HttpRequest.newBuilder()
            .uri(new URI(uriString))
            .header("Authorization", "Bearer " + token)
            .GET()
            .build();
    // building a response with the HttpRequest
    HttpResponse<String> response =
        HttpClient.newBuilder().build().send(buildRequest, HttpResponse.BodyHandlers.ofString());

    // using Moshi to turn it into a Song object
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Song> dataAdapter = moshi.adapter(Song.class);
    return dataAdapter.fromJson(response.body());
  }

  /**
   * Helper method that makes a request from API to get needed information and turns it into a
   * Recommendation object.
   *
   * @param uriString String that is the URI where we get the information from.
   * @return Recommendation object which is the contents of the Json returned from the API called.
   * @throws URISyntaxException exception where URI syntax is incorrect.
   * @throws IOException exception where it failed to read/open information.
   * @throws InterruptedException exception where connection to API is interrupted.
   */
  private Recommendation fetchRecommendationApiData(String uriString, String token)
      throws URISyntaxException, IOException, InterruptedException {
    // building a new HttpRequest
    HttpRequest buildRequest =
        HttpRequest.newBuilder()
            .uri(new URI(uriString))
            .header("Authorization", "Bearer " + token)
            .GET()
            .build();
    // building a response with the HttpRequest
    HttpResponse<String> response =
        HttpClient.newBuilder().build().send(buildRequest, HttpResponse.BodyHandlers.ofString());

    // using Moshi to turn it into a Recommendation object

    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<Recommendation> dataAdapter = moshi.adapter(Recommendation.class);
    return dataAdapter.fromJson(response.body());
  }

  /**
   * Helper method that makes a request from API to get needed information and turns it into a
   * FeaturesProp object.
   *
   * @param uriString String that is the URI where we get the information from.
   * @return FeaturesProp object which is the contents of the Json returned from the API called.
   * @throws URISyntaxException exception where URI syntax is incorrect.
   * @throws IOException exception where it failed to read/open information.
   * @throws InterruptedException exception where connection to API is interrupted.
   */
  private FeaturesProp fetchFeaturesApiData(String uriString, String token)
      throws URISyntaxException, IOException, InterruptedException {
    // building a new HttpRequest
    HttpRequest buildRequest =
        HttpRequest.newBuilder()
            .uri(new URI(uriString))
            .header("Authorization", "Bearer " + token)
            .GET()
            .build();
    // building a response with the HttpRequest
    HttpResponse<String> response =
        HttpClient.newBuilder().build().send(buildRequest, HttpResponse.BodyHandlers.ofString());

    // using Moshi to turn it into a Song object
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<FeaturesProp> dataAdapter = moshi.adapter(FeaturesProp.class);
    return dataAdapter.fromJson(response.body());
  }
}
