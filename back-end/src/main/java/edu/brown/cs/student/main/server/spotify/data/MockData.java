package edu.brown.cs.student.main.server.spotify.data;

import edu.brown.cs.student.main.server.spotify.data.mockedJsonSources.FeatureDataSource;
import edu.brown.cs.student.main.server.spotify.data.mockedJsonSources.RecommendationDataSource;
import edu.brown.cs.student.main.server.spotify.data.mockedJsonSources.SongDataSource;
import edu.brown.cs.student.main.server.spotify.records.audioFeaturesRecords.FeaturesProp;
import edu.brown.cs.student.main.server.spotify.records.recommendationRecords.Recommendation;
import edu.brown.cs.student.main.server.spotify.records.searchRecords.Song;
import java.io.FileReader;
import java.io.IOException;

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
  public Song getSong() throws Exception{

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
  public Recommendation getRecommendation() throws Exception{

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
  public FeaturesProp getFeatures() throws Exception {

    FileReader reader = new FileReader(this.mockedFeaturesPath);
    FeatureDataSource dataSource = new FeatureDataSource(reader);
    dataSource.loadJson();
    return dataSource.getJsonObj();
  }
}
