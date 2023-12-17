package edu.brown.cs.student.main.server.lyrics.data;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import edu.brown.cs.student.main.server.spotify.records.audioFeaturesRecords.FeaturesProp;
import edu.brown.cs.student.main.server.spotify.records.recommendationRecords.Recommendation;
import edu.brown.cs.student.main.server.spotify.records.searchRecords.Song;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.swing.plaf.LayerUI;

/**
 * Class that works as a proxy class for the data gotten from the spotify api.
 */
public class CachedLyricsData implements ILyricsData {

  private LyricsData data;
  private LoadingCache<String, ArrayList<String[]>> cache;

  private LoadingCache<String[], List<List<String>>> searchingSongCache;

  /**
   * Constructor for the CachedSpotifyData class. Serves as a proxy for the
   * Spotify Data class
   * allowing us to cache the data, increasing performance and reducing number of
   * API calls.
   */
  public CachedLyricsData() {
    this.data = new LyricsData();

    // building the cache that will hold a song object for specific song names
    this.cache = CacheBuilder.newBuilder()
        .maximumSize(40)
        .expireAfterWrite(2, TimeUnit.MINUTES)
        .recordStats().build(
            new CacheLoader<String, ArrayList<String[]>>() {

              @Override
              public ArrayList<String[]> load(String[] queries) throws Exception {
                return this.data.getLyrics(trackID);
              }
            });

    // building the cache that will hold the list of list of strings for the search
    // bar
    this.searchingSongCache = CacheBuilder.newBuilder()
        .maximumSize(40)
        .expireAfterWrite(3, TimeUnit.MINUTES)
        .recordStats().build(
            new CacheLoader<String[], List<List<String>>>() {
              @Override
              public List<List<String>> load(String[] queries) throws Exception {
                String prompt = queries[0];
                String limit = queries[1];
                return buildGetSongsPrompts(prompt, limit);
              }
            }

        );

  }

  @Override
  public ArrayList<String[]> getLyrics(String trackID) throws Exception {
    return this.data.getLyrics(trackID);
  }

  @Override
  public Boolean LyricsExist(String trackID) {
    return this.data.LyricsExist(trackID);
  }

}
