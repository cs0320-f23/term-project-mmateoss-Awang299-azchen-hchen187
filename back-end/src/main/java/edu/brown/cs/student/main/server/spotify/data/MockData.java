package edu.brown.cs.student.main.server.spotify.data;

import edu.brown.cs.student.main.server.spotify.records.recommendationRecords.Recommendation;
import edu.brown.cs.student.main.server.spotify.records.searchRecords.Song;

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
   * Method that allows the user to get a song object from the names of the songs inputted
   *
   * @return song object for the name of the songs inputted.
   */
  @Override
  public Song getSong() {
    // TODO Auto-generated method stub




    throw new UnsupportedOperationException("Unimplemented method 'getSong'");
  }

  /**
   *
   * @return
   */
  @Override
  public Recommendation getRecommendation() {
    // TODO Auto-generated method stub



    throw new UnsupportedOperationException("Unimplemented method 'getRecommendation'");
  }
}
