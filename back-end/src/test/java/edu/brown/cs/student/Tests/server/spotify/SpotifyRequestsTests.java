package edu.brown.cs.student.Tests.server.spotify;

import edu.brown.cs.student.Tests.server.spotify.tokens.TestTokenGenerator;
import edu.brown.cs.student.main.server.spotify.data.SpotifyData;
import edu.brown.cs.student.main.server.spotify.records.searchRecords.Song;
import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

public class SpotifyRequestsTests {

  private String token;
  private SpotifyData data;

  public SpotifyRequestsTests(){

  }

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

  }


  /**
   * Method that tests that we are able to get recommendations from the Spotify API
   */
  @Test
  public void testGetRecommendation() throws URISyntaxException, IOException,
      InterruptedException{

  }

}
