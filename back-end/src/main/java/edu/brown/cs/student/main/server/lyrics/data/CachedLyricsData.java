package edu.brown.cs.student.main.server.lyrics.data;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Class that works as a proxy class for the data gotten from the spotify api.
 */
public class CachedLyricsData implements ILyricsData {

  private LyricsData data;
  private LoadingCache<String, ArrayList<String[]>> getLyricsCache;

  private LoadingCache<String, Boolean> lyricsExistsCache;

  /**
   * Constructor for the CachedLyricsData class. Serves as a proxy for the
   * LyricsData class
   * allowing us to cache the data, increasing performance and reducing number of
   * API calls.
   */
  public CachedLyricsData() {
    this.data = new LyricsData();

    // building the cache that will hold a song object for specific song names
    this.getLyricsCache = CacheBuilder.newBuilder()
        .maximumSize(40)
        .expireAfterWrite(2, TimeUnit.MINUTES)
        .recordStats().build(
            new CacheLoader<String, ArrayList<String[]>>() {

              @Override
              public ArrayList<String[]> load(String trackID) throws Exception {
                try {
                  return getLyricsWrapper(trackID);
                } catch (Exception e) {
                  // Caches error messages if lyrics aren't found
                  ArrayList<String[]> res = new ArrayList<String[]>();
                  String[] error = new String[] { e.getMessage() };
                  res.add(error);
                  return res;
                }
              }
            });

    this.lyricsExistsCache = CacheBuilder.newBuilder()
        .maximumSize(40)
        .expireAfterWrite(2, TimeUnit.MINUTES)
        .recordStats().build(
            new CacheLoader<String, Boolean>() {

              @Override
              public Boolean load(String trackID) throws Exception {
                return lyricsExistWrapper(trackID);
              }

            });

  }

  @Override
  public ArrayList<String[]> getLyrics(String trackID) throws Exception {
    ArrayList<String[]> res = this.getLyricsCache.get(trackID);
    if (res.size() == 1 && res.get(0).length == 1) {
      throw new Exception(res.get(0)[0]);
    }
    return res;
  }

  @Override
  public Boolean LyricsExist(String trackID) {
    // If cache contains trackID as a key, return true
    try {
      return this.lyricsExistsCache.get(trackID);
    } catch (ExecutionException e) {
      return this.lyricsExistWrapper(trackID);
    }
  }

  private ArrayList<String[]> getLyricsWrapper(String trackID) throws Exception {
    return this.data.getLyrics(trackID);
  }

  private Boolean lyricsExistWrapper(String trackID) {
    return this.data.LyricsExist(trackID);
  }

}
