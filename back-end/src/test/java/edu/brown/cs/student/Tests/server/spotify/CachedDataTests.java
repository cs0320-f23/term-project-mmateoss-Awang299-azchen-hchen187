package edu.brown.cs.student.Tests.server.spotify;

import edu.brown.cs.student.Tests.server.spotify.tokens.TestTokenGenerator;
import edu.brown.cs.student.main.server.spotify.data.CachedSpotifyData;
import edu.brown.cs.student.main.server.spotify.data.SpotifyData;
import edu.brown.cs.student.main.server.spotify.records.audioFeaturesRecords.FeaturesProp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

/**
 * Class used to ensure that my CachedSpotifyData class functions properly.
 */
public class CachedDataTests {


  private String token;

  /**
   * Constructor for the testing class.
   */
  public CachedDataTests(){


  }


  /**
   * Method called before each test is run.
   */
  @BeforeEach
  public void beforeEach(){
    TestTokenGenerator generator = new TestTokenGenerator();
    this.token = generator.getToken();

  }


  /**
   * Ensuring that we can get the features of a group of songs based on their names
   * @throws Exception any exception thrown while the test is running
   */
  @Test
  public void testGetFeats() throws Exception{

    CachedSpotifyData data = new CachedSpotifyData();
    data.setToken(this.token);
    String[] names = new String[3];
    names[0] = "I Will Wait";
    //1fXiYSWmkKJfRKMegCkI11
    names[1] = "Lucid Dreams";
    //285pBltuF7vW8TeWk8hdRR
    names[2] = "Levels";
    //5UqCQaDshqbIk3pkhy4Pjg


    FeaturesProp feats = data.getFeatures(names);
    Assert.assertEquals(feats.audio_features().get(0).valence(), Float.valueOf("0.452"));
    Assert.assertEquals(feats.audio_features().get(1).danceability(), Float.valueOf("0.511"));
    Assert.assertEquals(feats.audio_features().get(2).liveness(), Float.valueOf("0.309"));

  }

}
