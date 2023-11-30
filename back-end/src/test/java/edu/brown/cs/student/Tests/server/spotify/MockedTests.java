package edu.brown.cs.student.Tests.server.spotify;

import edu.brown.cs.student.Tests.server.spotify.tokens.TestTokenGenerator;
import edu.brown.cs.student.main.server.spotify.data.MockData;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

/**
 * Class that tests different methods with mockedData to ensure that they work as intended for when
 * we are using real data.
 */
public class MockedTests {


  /**
   * Method that tests that the getSongIds method works for the actual implementation. This allows
   * us to be confident that it will work when using non-mocked data.
   *
   * @throws Exception IOException where the json files cant be read.
   */
  @Test
  public void testGetSongIds() throws Exception{
    MockData data = new MockData(
        "data/mockedSpotifyJsons/mockedSearch/mockedTrackSearch1.json",
        "data/mockedSpotifyJsons/"
            + "mockedRecommendations/mockedRecommendation1.json",
        "data/mockedSpotifyJsons/mockedAudioFeatures/mockedAudioFeats1.json");
    String[] filePaths = new String[3];
    filePaths[0] = "data/mockedSpotifyJsons/mockedSearch/mockedTrackSearch1.json";
    filePaths[1] = "data/mockedSpotifyJsons/mockedSearch/mockedTrackSearch2.json";
    filePaths[2] = "data/mockedSpotifyJsons/mockedSearch/mockedTrackSearch3.json";

    String ids = data.getSongIds(filePaths);
    String actualIds = "1fXiYSWmkKJfRKMegCkI11,4MjDJD8cW7iVeWInc2Bdyj,5gB2IrxOCX2j9bMnHKP38i";
    Assert.assertEquals(ids,actualIds);

  }


  /**
   * Method that tests that the token generation is correct
   */
  @Test
  public void testGetToken(){

    TestTokenGenerator generator = new TestTokenGenerator();
    String token = generator.getToken();
    Assert.assertTrue(token != null);
  }
}