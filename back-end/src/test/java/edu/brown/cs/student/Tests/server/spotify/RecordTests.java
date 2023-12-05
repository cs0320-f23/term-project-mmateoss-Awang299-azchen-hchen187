package edu.brown.cs.student.Tests.server.spotify;

import edu.brown.cs.student.main.server.spotify.data.MockData;
import edu.brown.cs.student.main.server.spotify.records.audioFeaturesRecords.FeaturesProp;
import edu.brown.cs.student.main.server.spotify.records.recommendationRecords.Recommendation;
import edu.brown.cs.student.main.server.spotify.records.searchRecords.Song;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

/**
 * Class that tests that the records create the correct objects from the passed in json.
 */
public class RecordTests {



  /**
   * Method that tests that our Recommendation Records create objects with the
   * correct fields and values.
   */
  @Test
  public void testRecommendationRecords() throws Exception{
    MockData data = new MockData(
        "data/mockedSpotifyJsons/mockedSearch/mockedTrackSearch1.json",
        "data/mockedSpotifyJsons/"
            + "mockedRecommendations/mockedRecommendation1.json",
        "data/mockedSpotifyJsons/mockedAudioFeatures/mockedAudioFeats1.json");
    Recommendation rec = data.getRecommendation("", null, "");
    Assert.assertTrue(rec instanceof Recommendation);
    Assert.assertEquals(rec.tracks().get(0).id(),"5N9M7Ji7KYrmfJ6Jki3raU");
    Assert.assertEquals(rec.seeds().size(),1);
    Assert.assertEquals(rec.seeds().get(0).id(),"0c6xIDDpzE81m2q797ordA");
    Assert.assertEquals(rec.tracks().size(), 20);


  }

  /**
   * Method that tests that our Features Records create objects with the correct fields
   * and values.
   */
  @Test
  public void testFeaturesRecords() throws Exception{
    MockData data = new MockData(
        "data/mockedSpotifyJsons/mockedSearch/mockedTrackSearch1.json",
        "data/mockedSpotifyJsons/"
            + "mockedRecommendations/mockedRecommendation1.json",
        "data/mockedSpotifyJsons/mockedAudioFeatures/mockedAudioFeats1.json");
    FeaturesProp feat = data.getFeatures( null);
    Assert.assertTrue(feat instanceof FeaturesProp);
    Assert.assertEquals(feat.audio_features().size(), 3);
    Assert.assertEquals(feat.audio_features().get(0).acousticness(), Float.valueOf("0.000273"));
    Assert.assertEquals(feat.audio_features().get(0).valence(),Float.valueOf("0.211"));
    Assert.assertEquals(feat.audio_features().get(2).liveness(), Float.valueOf("0.0866"));
    Assert.assertEquals(feat.audio_features().get(2).danceability(), Float.valueOf("0.585"));


  }


  /**
   * Method that tests that our Search Records create objects with the correct fields
   * and values.
   */
  @Test
  public void testSearchRecords() throws Exception{
    MockData data = new MockData(
        "data/mockedSpotifyJsons/mockedSearch/mockedTrackSearch1.json",
        "data/mockedSpotifyJsons/"
            + "mockedRecommendations/mockedRecommendation1.json",
        "data/mockedSpotifyJsons/mockedAudioFeatures/mockedAudioFeats1.json");
    Song song = data.getSong( "");
    Assert.assertTrue(song instanceof Song);
    Assert.assertEquals(song.tracks().items().size(), 2);
    Assert.assertEquals(song.tracks().items().get(0).name(), "I Will Wait");
    Assert.assertEquals(song.tracks().items().get(1).id(), "4Vnbh54MMkRf2uZXcON3Av");
    Assert.assertEquals(song.tracks().items().get(0).uri(),
        "spotify:track:1fXiYSWmkKJfRKMegCkI11");
    Assert.assertEquals(song.tracks().items().get(1).popularity(), 44);
    Assert.assertEquals(song.tracks().items().get(0).preview_url(), "https://p.scdn.co/mp3"
        + "-preview/4d2f72a69bc3619e6e854912fd1275b6efbcc10c?cid=8199ced0f4aa4ceba1efbcedd7b6878a");

  }
}
