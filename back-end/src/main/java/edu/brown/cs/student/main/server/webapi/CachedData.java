package edu.brown.cs.student.main.server.webapi;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


/**
 * Class for the CachedData. Also serves as Proxy for the data between getting it and returning
 * it to the user with BroadbandHandler.
 */
public class CachedData {

  private LoadingCache<String, String[]> broadbandCache;
  private LoadingCache<String, List<List<String>>> countyCodesCache;
  private CensusDataSource source;

  /**
   * Constructor for the CachedData class. Instantiates the two Caches here.
   *
   * @param size the size of the Cache.
   * @param minutes the length of time the Cache will hold something before clearing.
   * @throws URISyntaxException exception thrown when URI is invalid.
   * @throws IOException exception when it failed to read/open information.
   * @throws InterruptedException exception where connection to API is interrupted.
   */
  public CachedData(int size, int minutes)
      throws URISyntaxException, IOException, InterruptedException {
    this.source = new CensusDataSource();

    // creating Cache that will hold Broadband info
    this.broadbandCache = CacheBuilder.newBuilder()
        .maximumSize(size)
        .expireAfterWrite(minutes, TimeUnit.MINUTES)
        .recordStats()
        .build(
            new CacheLoader<String, String[]>() {
              @Override
              public String[] load(String stateCountyPair) throws Exception {
                String[] broadbandInfo = new String[2];

                broadbandInfo[0] = source.getBroadband(
                    stateCountyPair.split(",")[0],
                    stateCountyPair.split(",")[1]);
                broadbandInfo[1] = LocalDateTime.now().toString();
                return broadbandInfo;
              }
            }
        );

    // creating Cache that will hold the county codes for a state
    this.countyCodesCache = CacheBuilder.newBuilder()
        .maximumSize(size)
        .expireAfterWrite(minutes, TimeUnit.MINUTES)
        .recordStats()
        .build(
            new CacheLoader<String, List<List<String>>>() {
              @Override
              public List<List<String>> load(String stateCode) throws Exception {
                return source.getCountyList(stateCode);
              }
            }
        );
  }

  /**
   * Method that returns the BroadBand info for a specific state and county.
   *
   * @param stateName the name of the State in which the county resides.
   * @param countyName the name of the County for which you want the information.
   * @return an Array that contains two things. In the 0th index, the broadband information for
   *        the requested county and state, and in the 1st index, the time the information was
   *        gotten from the Census API.
   * @throws ExecutionException exception thrown when it failed to retrieve from the Cache.
   */
  public String[] getBroadBandInfo(String stateName, String countyName) throws ExecutionException {
    // getting state code from the hashmap in the instance of CensusDataSource
    String stateCode = this.source.getStateCode(stateName);

    // getting the list of county codes for the state from the cache
    List<List<String>> countyCodes = this.countyCodesCache.get(stateCode);
    // finding the specific county code for the county
    String countyCode = null;
    for (int i = 1; i < countyCodes.size(); i++) {
      if (countyCodes.get(i).get(0).split(",")[0].equals(countyName)) {
        countyCode = countyCodes.get(i).get(2);
      }
    }
    if (countyCode == null) {
      // means that the county inputted does not exist therefore throw an error
      throw new IllegalArgumentException();
    }
    // returning the specific broadband and time from the broadband cache
    String stateCountyPair = stateCode + ',' + countyCode;
    return this.broadbandCache.get(stateCountyPair);
  }

}
