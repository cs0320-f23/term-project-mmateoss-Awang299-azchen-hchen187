package edu.brown.cs.student.main.server.spotify.data;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import edu.brown.cs.student.main.server.spotify.records.audioFeaturesRecords.FeaturesProp;
import edu.brown.cs.student.main.server.spotify.records.recommendationRecords.Recommendation;
import edu.brown.cs.student.main.server.spotify.records.searchRecords.Song;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Class that works as a proxy class for the data gotten from the spotify api.
 */
public class CachedSpotifyData implements IData {

  private SpotifyData data;
  private LoadingCache<String, Song> songCache;
  private String token;

  /**
   * Constructor for the CachedSpotifyData class. Serves as a proxy for the Spotify Data class
   * allowing us to cache the data, increasing performance and reducing number of API calls.
   */
  public CachedSpotifyData(){
    this.data = new SpotifyData();

    // building the cache that will hold a song object for specific song names
    this.songCache = CacheBuilder.newBuilder()
        .maximumSize(40)
        .expireAfterWrite(2, TimeUnit.MINUTES)
        .recordStats().build(
            new CacheLoader<String, Song>() {
              @Override
              public Song load(String songName) throws Exception {

                return getSong(songName);
              }
            }
        );

  }

  /**
   * Method that sets the instance variable token to whatever is passed in to it.
   *
   * @param token the Spotify authorization token
   * @return boolean saying if the token was set
   */
  @Override
  public boolean setToken(String token){
    if(token != null){
      this.token = token;
      return true;
    }
    return false;
  }


  /**
   * Method that gets a song object based on the name of the song.
   * @param songName the name of the song
   *
   * @return Spotify object for the name of the song
   *
   * @throws Exception
   */
  @Override
  public Song getSong(String songName) throws URISyntaxException, IOException, InterruptedException{
    return this.data.getSong(this.token,songName);
  }


  /**
   * Method that generates the values needed to generate recommendation
   * based on the metadata for the songs passed in.
   *
   * @return a string array containing all the values needed.
   */
  private String[] generateValues(FeaturesProp feats){

    String[] results = new String[11];
    float avgAcousticness = 0;
    float avgDancability = 0;
    float avgEnergy = 0;
    float avgSpeechiness = 0;
    float avgValence = 0;
    int divisor = 0;

    // getting a random track from the list to be our seed track
    Random rand = new Random();
    int randIdx = rand.nextInt(feats.audio_features().size());
    String seed_track = feats.audio_features().get(randIdx).id();

    // getting a sum of the values and then going to divide
    for(int i=0; i<feats.audio_features().size();i++){
      if(feats.audio_features().get(i) != null){
        avgAcousticness += feats.audio_features().get(i).acousticness();
        avgDancability += feats.audio_features().get(i).danceability();
        avgEnergy += feats.audio_features().get(i).energy();
        avgSpeechiness += feats.audio_features().get(i).speechiness();
        avgValence += feats.audio_features().get(i).valence();
        divisor += 1;
      }
    }

    // actually getting the averages of each one
    avgAcousticness = avgAcousticness / divisor;
    avgDancability = avgDancability / divisor;
    avgEnergy = avgEnergy / divisor;
    avgSpeechiness = avgSpeechiness / divisor;
    avgValence = avgValence / divisor;

    // generating overall values for the query
    if(avgAcousticness - 0.2 < 0){
      String min_acousticness = "0";
      results[0] = min_acousticness;
    } else{
      String min_acousticness = String.valueOf(avgAcousticness - 0.2);
      results[0] = min_acousticness;
    }
    if(avgAcousticness + 0.2 > 1){
      String max_acousticness = "1";
      results[1] = max_acousticness;
    } else{
      String max_acousticness = String.valueOf(avgAcousticness + 0.2);
      results[1] = max_acousticness;
    }
    if(avgDancability - 0.2 < 0){
      String min_danceability = "0";
      results[2] = min_danceability;
    } else{
      String min_danceability = String.valueOf(avgDancability - 0.2);
      results[2] = min_danceability;
    }
    if(avgDancability + 0.2 > 1){
      String max_danceability = "1";
      results[3] = max_danceability;
    } else{
      String max_danceability = String.valueOf(avgDancability + 0.2);
      results[3] = max_danceability;
    }
    if(avgEnergy - 0.2 < 0){
      String min_energy = "0";
      results[4] = min_energy;
    } else{
      String min_energy = String.valueOf(avgEnergy - 0.2);
      results[4] = min_energy;
    }
    if(avgEnergy + 0.2 > 1){
      String max_energy = "1";
      results[5] = max_energy;
    } else{
      String max_energy = String.valueOf(avgEnergy + 0.2);
      results[5] = max_energy;
    }
    if(avgSpeechiness - 0.2 < 0){
      String min_speechiness = "0";
      results[6] = min_speechiness;
    } else{
      String min_speechiness = String.valueOf(avgSpeechiness - 0.2);
      results[6] = min_speechiness;
    }
    if(avgSpeechiness + 0.2 > 1){
      String max_speechiness = "1";
      results[7] = max_speechiness;
    } else{
      String max_speechiness = String.valueOf(avgSpeechiness + 0.2);
      results[7] = max_speechiness;
    }
    if(avgValence - 0.2 < 0){
      String min_valence = "0";
      results[8] = min_valence;
    } else{
      String min_valence = String.valueOf(avgValence - 0.2);
      results[8] = min_valence;
    }
    if(avgValence + 0.2 > 1){
      String max_valence = "1";
      results[9] = max_valence;
    } else{
      String max_valence = String.valueOf(avgValence + 0.2);
      results[9] = max_valence;
    }

    results[10] = seed_track;

    return results;
  }


  /**
   * Method that generates a recommendation based on all the metadata points inputted.
   *
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
   * @throws URISyntaxException   exception where URI syntax is incorrect.
   * @throws IOException          exception where it failed to read/open
   *                              information.
   * @throws InterruptedException exception where connection to API is
   *                              interrupted.
   */
  private Recommendation generateRecommendation(String limit, String seed_tracks,
      String min_acousticness, String max_acousticness, String min_danceability,
      String max_danceability, String min_energy, String max_energy,
      String min_speechiness, String max_speechiness, String min_valence,
      String  max_valence) throws InterruptedException, IOException, URISyntaxException{

    Recommendation rec = this.data.getRecommendation(this.token,limit,seed_tracks,
        min_acousticness,max_acousticness,min_danceability,max_danceability,min_energy,
        max_energy,min_speechiness,max_speechiness,min_valence,max_valence);
    return rec;
  }

  /**
   * Method called by the server to get a recommendation based on the passed in Array of
   * song names.
   *
   * @param limit
   * @param allNames
   * @return
   * @throws Exception
   */
  @Override
  public Recommendation getRecommendation(String limit, String[] allNames) throws
      URISyntaxException, IOException, InterruptedException, ExecutionException {

    FeaturesProp feats = this.getFeatures(allNames);
    String[] values = this.generateValues(feats);

    Recommendation rec = this.generateRecommendation(limit ,values[10],values[0],values[1],
        values[2],values[3],values[4],values[5],values[6],values[7],values[8],values[9]);

    return rec;
  }



  /**
   * Helper method that gets all the song ids and puts them into a string in order for getFeatures
   * to get all the features for the songs.
   *
   * @param allNames String or List<String> that contains all the names of the
   * @return a string that is comma separated, containing all the track ids for the songs
   * @throws URISyntaxException   exception where URI syntax is incorrect.
   * @throws IOException          exception where it failed to read/open
   *                              information.
   * @throws InterruptedException exception where connection to API is
   *                              interrupted.
   *
   */
  private String getSongIds(String[] allNames) throws URISyntaxException, IOException,
      InterruptedException, ExecutionException {

    // creating our final string to store all our ids
    String ids = "";


    int lastIdx = allNames.length-1;
    // loop through the list of song names and
    for(int i=0; i<allNames.length;i++){
      String songName = allNames[i];

      //before cache
      //Song songObj = this.getSong(token, songName);
      Song songObj = this.songCache.get(songName);

      if(i == lastIdx){
        ids = ids+songObj.tracks().items().get(0).id();
      }
      else{
        ids = ids+songObj.tracks().items().get(0).id()+",";
      }
    }

    // returning the built string with all the ids
    return ids;
  }


  /**
   * Method the gets a feature object containing info for all the songs.
   *
   * @param allNames array containing the names of all the songs inputted.
   *
   * @return a featureProp object containing features for each song
   *
   * @throws Exception
   */
  @Override
  public FeaturesProp getFeatures(String[] allNames) throws URISyntaxException, IOException, InterruptedException, ExecutionException{

    // getting the song ids for every song name available
    String songIds = this.getSongIds(allNames);
    // making an api request and returning the corresponding Feature
    FeaturesProp feat = this.data.getFeatures(this.token,songIds);
    return feat;


  }

}