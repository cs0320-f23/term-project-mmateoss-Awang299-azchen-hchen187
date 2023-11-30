package edu.brown.cs.student.Tests.server.spotify;

import edu.brown.cs.student.Tests.server.spotify.tokens.TestTokenGenerator;
import edu.brown.cs.student.main.server.spotify.data.SpotifyData;
import edu.brown.cs.student.main.server.spotify.records.audioFeaturesRecords.FeaturesProp;
import edu.brown.cs.student.main.server.spotify.records.recommendationRecords.Recommendation;
import edu.brown.cs.student.main.server.spotify.records.searchRecords.Song;
import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

/**
 * Class that test that we can make correct requests to the spotify API
 */
public class SpotifyRequestsTests {

  private String token;
  private SpotifyData data;

  /**
   * Constructor for the class
   */
  public SpotifyRequestsTests(){

  }

  /**
   * Method called before each test is run
   */
  @BeforeEach
  public void beforeTest(){
    TestTokenGenerator generator = new TestTokenGenerator();
    this.token = generator.getToken();
    this.data = new SpotifyData();
  }


  /**
   * Method that tests that we are able to get songs from the spotify API
   */
  @Test
  public void testGetSong() throws URISyntaxException, IOException,
      InterruptedException{

    Song songResp = this.data.getSong(this.token, "I Will Wait");
    Assert.assertEquals(songResp.tracks().items().get(0).id(),"1fXiYSWmkKJfRKMegCkI11");

  }


  /**
   * Method that tests that we are able to get features of songs from the Spotify API
   */
  @Test
  public void testGetFeatures() throws URISyntaxException, IOException,
      InterruptedException{
    // all the song names
    String[] names = new String[3];
    names[0] = "I Will Wait";
    //1fXiYSWmkKJfRKMegCkI11
    names[1] = "Lucid Dreams";
    //285pBltuF7vW8TeWk8hdRR
    names[2] = "Levels";
    //5UqCQaDshqbIk3pkhy4Pjg

    FeaturesProp features = this.data.getFeatures(this.token, names);

    Assert.assertEquals(features.audio_features().get(0).valence(), Float.valueOf("0.452"));
    Assert.assertEquals(features.audio_features().get(1).danceability(), Float.valueOf("0.511"));
    Assert.assertEquals(features.audio_features().get(2).liveness(), Float.valueOf("0.309"));

  }


  /**
   * Method that tests that we are able to get recommendations from the Spotify API
   */
  @Test
  public void testGetRecommendation() throws URISyntaxException, IOException,
      InterruptedException{

    String seedTrack = "1fXiYSWmkKJfRKMegCkI11";
    Recommendation rec = this.data.getRecommendation(this.token,"3",seedTrack,
        "0.2", "1","0.4","1",
        "0.6", "1","0","1","0.5",
        "1" );


    Assert.assertTrue(rec.tracks().get(0).name() != null);
    Assert.assertTrue(rec.tracks().get(1).id() != null);
    Assert.assertTrue(rec.tracks().get(2).duration_ms() != 0);
    Assert.assertTrue(rec.seeds().get(0).afterFilteringSize() >= 0);
    Assert.assertEquals(rec.tracks().size(), 3);
    Assert.assertEquals(rec.seeds().size(), 1);

  }

}
