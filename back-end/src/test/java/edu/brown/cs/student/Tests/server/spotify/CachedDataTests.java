package edu.brown.cs.student.Tests.server.spotify;

import edu.brown.cs.student.main.server.spotify.tokens.TokenGenerator;
import edu.brown.cs.student.main.server.CachedSpotifyData;
import edu.brown.cs.student.main.server.spotify.records.audioFeaturesRecords.FeaturesProp;
import edu.brown.cs.student.main.server.spotify.records.recommendationRecords.Recommendation;
import java.util.List;
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
  public CachedDataTests() {

  }

  /**
   * Method called before each test is run.
   */
  @BeforeEach
  public void beforeEach() throws Exception {
    TokenGenerator generator = new TokenGenerator();
    this.token = generator.getToken();

  }

  /**
   * Ensuring that we can get the features of a group of songs based on their
   * names
   * 
   * @throws Exception any exception thrown while the test is running
   */
  @Test
  public void testGetFeats() throws Exception {

    CachedSpotifyData data = new CachedSpotifyData();
    data.setToken(this.token);
    String[] names = new String[3];
    names[0] = "I Will Wait";
    // 1fXiYSWmkKJfRKMegCkI11
    names[1] = "Lucid Dreams";
    // 285pBltuF7vW8TeWk8hdRR
    names[2] = "Levels";
    // 5UqCQaDshqbIk3pkhy4Pjg

    FeaturesProp feats = data.getFeatures(names);
    Assert.assertEquals(feats.audio_features().get(0).valence(), Float.valueOf("0.452"));
    Assert.assertEquals(feats.audio_features().get(1).danceability(), Float.valueOf("0.511"));
    Assert.assertEquals(feats.audio_features().get(2).liveness(), Float.valueOf("0.562"));

  }

  /**
   * Ensuring that the overall recommendation works with our cached data class
   *
   * @throws Exception
   */
  @Test
  public void testGenerateRecommendationSingleTrack() throws Exception {

    CachedSpotifyData data = new CachedSpotifyData();
    data.setToken(this.token);
    String[] allNames = new String[1];
    allNames[0] = "Enchanted";
    // id: 3sW3oSbzsfecv9XoUdGs7h
    // acousticness: 0.012
    // dancability: 0.505
    // energy: 0.532
    // valence: 0.216
    // speechiness: 0.0265

    String limit = "3";
    Recommendation rec = data.getRecommendation(limit, allNames, "0.2");
    Assert.assertEquals(rec.tracks().size(), 3);
    Assert.assertTrue(rec.seeds().get(0).afterFilteringSize() > 10);

  }

  /**
   * Method that tests that we can correctly generate a playlist with more than
   * one song used for
   * generation.
   *
   * @throws Exception
   */
  @Test
  public void testGenerateRecommendationMultiTrack() throws Exception {

    CachedSpotifyData data = new CachedSpotifyData();
    data.setToken(this.token);
    String[] allNames = new String[2];
    allNames[0] = "Enchanted";
    // id: 3sW3oSbzsfecv9XoUdGs7h
    // acousticness: 0.012
    // dancability: 0.505
    // energy: 0.532
    // valence: 0.216
    // speechiness: 0.0265

    allNames[1] = "All too Well";
    // id: 5enxwA8aAbwZbf5qCHORXi
    // acousticness: 0.274
    // dancability: 0.631
    // energy: 0.518
    // valence: 0.205
    // speechiness: 0.0303

    // avg acou: 0.143
    // avg dan: 0.568
    // avg ene: 0.525
    // avg val: 0.2105
    // avg spe: 0.0284

    String limit = "5";
    Recommendation rec = data.getRecommendation(limit, allNames, "0.2");
    Assert.assertEquals(rec.seeds().get(0).initialPoolSize(), 500);
    Assert.assertTrue(rec.seeds().get(0).afterFilteringSize() > 10);

  }

  /**
   * Method that test the getSongsPrompt method from the
   * 
   * @throws Exception
   */
  @Test
  public void testGetSongsPrompt() throws Exception {
    CachedSpotifyData data = new CachedSpotifyData();
    data.setToken(this.token);
    String limit = "5";
    String prompt = "the weekend";
    List<List<String>> results = data.getSongsPrompt(prompt, limit);
    Assert.assertEquals(results.get(0).get(0), "The Weekend");
    Assert.assertEquals(results.get(0).get(1), "SZA");
    Assert.assertEquals(results.get(1).get(3),
        "https://i.scdn.co/image/ab67616d00001e027fcead687e99583072cc217b");

  }

}
