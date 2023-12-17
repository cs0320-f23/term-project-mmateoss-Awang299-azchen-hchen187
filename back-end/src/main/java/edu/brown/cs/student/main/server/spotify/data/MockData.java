package edu.brown.cs.student.main.server.spotify.data;

import edu.brown.cs.student.main.server.spotify.data.mockedJsonSources.FeatureDataSource;
import edu.brown.cs.student.main.server.spotify.data.mockedJsonSources.RecommendationDataSource;
import edu.brown.cs.student.main.server.spotify.data.mockedJsonSources.SongDataSource;
import edu.brown.cs.student.main.server.spotify.records.audioFeaturesRecords.FeaturesProp;
import edu.brown.cs.student.main.server.spotify.records.recommendationRecords.Recommendation;
import edu.brown.cs.student.main.server.spotify.records.searchRecords.Song;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that will be used to get mocked data, allowing our server to use it accordingly without
 * having to connect to the spotify api.
 */
public class MockData implements IData {

  private String mockedSongPath;
  private String mockedRecommendationPath;
  private String mockedFeaturesPath;

  /** Constructor for the MockData class. */
  public MockData(
      String mockedSongPath, String mockedRecommendationPath, String mockedFeaturesPath) {

    this.mockedSongPath = mockedSongPath;
    this.mockedRecommendationPath = mockedRecommendationPath;
    this.mockedFeaturesPath = mockedFeaturesPath;
  }

  /**
   * Method that allows the server to get a song object from the names of the songs inputted In this
   * case uses mockedData.
   *
   * @return song object for the name of the songs inputted.
   * @exception Exception IOException where the json file cant be read.
   */
  @Override
  public Song getSong(String songName) throws Exception {

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
  public Recommendation getRecommendation(String limit, String[] allNames, String variability)
      throws Exception {

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
  public FeaturesProp getFeatures(String[] allNames) throws Exception {

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
   */
  public String getSongIds(String[] allNames) throws Exception {

    String ids = "";
    int lastIdx = allNames.length - 1;
    // loop through the list of song names and
    for (int i = 0; i < allNames.length; i++) {
      String filePath = allNames[i];
      Song songObj = this.getSongSpecificIds(filePath);
      if (i == lastIdx) {
        ids = ids + songObj.tracks().items().get(0).id();
      } else {
        ids = ids + songObj.tracks().items().get(0).id() + ",";
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
  private Song getSongSpecificIds(String file) throws Exception {

    FileReader reader = new FileReader(file);
    SongDataSource songData = new SongDataSource(reader);
    songData.loadJson();
    return songData.getJsonObj();
  }

  /**
   * Empty method used to set the token in the non mocked version of this method
   *
   * @param token the spotify api token
   */
  @Override
  public boolean setToken(String token) {
    return false;
  }

  /**
   * Method that returns a list of list of strings containing a lot of information of the songs
   * returned for the user to use in order to get the valuable info to create the search bar.
   *
   * @param prompt what is searching for the song
   * @param limit max number of songs returned
   * @return list of list of strings with info for the songs
   * @throws Exception any exception thrown while creating the mocked response.
   */
  @Override
  public List<List<String>> getSongsPrompt(String prompt, String limit) throws Exception {
    FileReader reader = new FileReader(this.mockedSongPath);
    SongDataSource songData = new SongDataSource(reader);
    songData.loadJson();
    Song songs = songData.getJsonObj();
    List<List<String>> toReturn = new ArrayList<>();
    for (int i = 0; i < songs.tracks().items().size(); i++) {
      List<String> innerList = new ArrayList<>();
      innerList.add(songs.tracks().items().get(i).name());
      innerList.add(songs.tracks().items().get(i).artists().get(0).name());
      innerList.add(songs.tracks().items().get(i).id());
      innerList.add(songs.tracks().items().get(i).album().images().get(1).url());

      toReturn.add(innerList);
    }
    return toReturn;
  }

  /** Mocked method that returns the same recommendation object. */
  @Override
  public Recommendation postProcess(Recommendation rec, String[] names) {
    for (int i = 0; i < names.length; i++) {
      String name = names[i];
      name = name.replaceAll("%26", "&");
      name = name.replaceAll("%20", " ");
      name = name.replaceAll("%28", "(");
      name = name.replaceAll("%29", ")");
      name = name.replaceAll("\\+", " ");
      System.out.println(name);
    }
    return rec;
  }
}
