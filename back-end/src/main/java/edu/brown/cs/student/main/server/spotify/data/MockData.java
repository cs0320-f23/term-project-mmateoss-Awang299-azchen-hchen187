package edu.brown.cs.student.main.server.spotify.data;

import edu.brown.cs.student.main.server.spotify.data.mockedJsonSources.FeatureDataSource;
import edu.brown.cs.student.main.server.spotify.data.mockedJsonSources.RecommendationDataSource;
import edu.brown.cs.student.main.server.spotify.data.mockedJsonSources.SongDataSource;
import edu.brown.cs.student.main.server.spotify.records.audioFeaturesRecords.FeaturesProp;
import edu.brown.cs.student.main.server.spotify.records.recommendationRecords.Recommendation;
import edu.brown.cs.student.main.server.spotify.records.searchRecords.Song;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Class that will be used to get mocked data, allowing
 * our server to use it accordingly without having to connect to the spotify api.
 */
public class MockData implements IData {

  private String mockedSongPath;
  private String mockedRecommendationPath;
  private String mockedFeaturesPath;


  /**
   * Constructor for the MockData class.
   */
  public MockData(String mockedSongPath,
      String mockedRecommendationPath, String mockedFeaturesPath) {

    this.mockedSongPath = mockedSongPath;
    this.mockedRecommendationPath = mockedRecommendationPath;
    this.mockedFeaturesPath = mockedFeaturesPath;

  }

  /**
   * Method that allows the server to get a song object from the names of the songs inputted
   * In this case uses mockedData.
   *
   * @return song object for the name of the songs inputted.
   * @exception Exception IOException where the json file cant be read.
   */
  @Override
  public Song getSong(String token, String songName) throws Exception{

    FileReader reader = new FileReader(this.mockedSongPath);
    SongDataSource songData = new SongDataSource(reader);
    songData.loadJson();
    return songData.getJsonObj();

  }


  /**
   * Method used to get a recommendation object from mocked Data.
   *
   * @return a mocked recommendation object
   * @exception Exception IOException where the json file cant be read.
   */
  @Override
  public Recommendation getRecommendation(String token, String limit, String seed_tracks,
      String min_acousticness, String max_acousticness, String min_danceability,
      String max_danceability, String min_energy, String max_energy,
      String min_speechiness, String max_speechiness, String min_valence,
      String  max_valence) throws Exception{

    FileReader reader = new FileReader(this.mockedRecommendationPath);
    RecommendationDataSource dataSource = new RecommendationDataSource(reader);
    dataSource.loadJson();
    return dataSource.getJsonObj();

  }


  /**
   * Method used to get a mocked FeatureProps object.
   *
   * @return a mocked featureProps object
   * @throws Exception IO Exception where the json cant be read.
   */
  @Override
  public FeaturesProp getFeatures(String token, String[] allNames) throws Exception {

    FileReader reader = new FileReader(this.mockedFeaturesPath);
    FeatureDataSource dataSource = new FeatureDataSource(reader);
    dataSource.loadJson();
    return dataSource.getJsonObj();
  }


  /**
   * Helper method that gets all the song ids and puts them into a string in order for getFeatures
   * to get all the features for the songs. This method is for testing purposes in order to make
   * sure that the method will work in the non mocked case.
   *
   * @param allNames String or List<String> that contains all the names of the
   * @return a string that is comma separated, containing all the track ids for the songs
   *
   */
  public String getSongIds(String[] allNames) throws Exception {

    String ids = "";
    int lastIdx = allNames.length-1;
    // loop through the list of song names and
    for(int i=0; i<allNames.length;i++){
      String filePath = allNames[i];
      Song songObj = this.getSongSpecificIds(filePath);
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
   * Helper method that creates a song object from a specific filePath to be used in the mocked
   * getSongIds method, allowing for us to test that method.
   *
   * @param file the filePath for a specific json file for the songObj
   * @return a songObj for the specific song
   * @throws Exception IOException where the
   */
  private Song getSongSpecificIds(String file) throws Exception{

    FileReader reader = new FileReader(file);
    SongDataSource songData = new SongDataSource(reader);
    songData.loadJson();
    return songData.getJsonObj();

  }
}
