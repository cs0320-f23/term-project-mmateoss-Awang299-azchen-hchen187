package edu.brown.cs.student.Tests.server.webapi;

import edu.brown.cs.student.main.server.webapi.CachedData;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

/**
 * Testing class that tests the functionality of our CachedData class.
 */
public class CachedDataTests {

  /**
   * Method that tests the getBroadbandInfo method.
   *
   * @throws URISyntaxException exception where URI syntax is incorrect.
   * @throws IOException exception where it failed to read/open information.
   * @throws InterruptedException exception where connection to API is interrupted.
   */
  @Test
  public void testGetBroadbandInfo()
      throws URISyntaxException, IOException, InterruptedException, ExecutionException {
    CachedData cache = new CachedData(20, 2);

    // checking to see that it can get the broadband
    Assert.assertEquals(cache.getBroadBandInfo("Texas", "Bexar County")[0],
        "87.6");
    Assert.assertEquals(cache.getBroadBandInfo("California",
            "Orange County")[0], "93.0");


    // checking to see that time is the same
    String timeOne = cache.getBroadBandInfo("Texas", "Bexar County")[1];
    String timeTwo = cache.getBroadBandInfo("Texas", "Bexar County")[1];
    String timeThree = cache.getBroadBandInfo("Texas", "Bexar County")[1];
    Assert.assertEquals(timeOne, timeTwo);
    Assert.assertEquals(timeOne, timeThree);
    Assert.assertEquals(timeTwo, timeTwo);



  }


  /**
   * Method that tests the getBroadbandInfo method with a bad state inputted.
   *
   * @throws URISyntaxException exception where URI syntax is incorrect.
   * @throws IOException exception where it failed to read/open information.
   * @throws InterruptedException exception where connection to API is interrupted.
   */
  @Test
  public void testGetBroadbandInfoBadState() throws URISyntaxException, IOException, InterruptedException {
    CachedData cache = new CachedData(20, 2);

    // checking to see that it throws errors

    try{
      cache.getBroadBandInfo("nah","Bexar County");

    } catch (IllegalArgumentException e){
      // should print caught
      System.out.println("Caught");

    } catch (ExecutionException e) {

    }


  }



  /**
   * Method that tests the getBroadbandInfo with a bad county inputted.
   *
   * @throws URISyntaxException exception where URI syntax is incorrect.
   * @throws IOException exception where it failed to read/open information.
   * @throws InterruptedException exception where connection to API is interrupted.
   */
  @Test
  public void testGetBroadbandInfoBadCounty() throws URISyntaxException, IOException,
      InterruptedException {
    CachedData cache = new CachedData(20, 2);

    // checking to see that it throws errors

    try{
      cache.getBroadBandInfo("Texas","nah");

    } catch (IllegalArgumentException e){
      // should print caught
      System.out.println("Caught");

    } catch (ExecutionException e) {

    }
  }

  /**
   * Method that tests the getBroadbandInfo with a null state
   *
   * @throws URISyntaxException exception where URI syntax is incorrect.
   * @throws IOException exception where it failed to read/open information.
   * @throws InterruptedException exception where connection to API is interrupted.
   */
  @Test
  public void testGetBroadbandInfoNullState() throws URISyntaxException, IOException,
      InterruptedException {
    CachedData cache = new CachedData(20, 2);

    // checking to see that it throws errors
    try{
      cache.getBroadBandInfo(null,"Bexar County");

    } catch (IllegalArgumentException e){
      // should print caught
      System.out.println("Caught");

    } catch (ExecutionException e) {

    }
  }

  /**
   * Method that tests the getBroadbandInfo with a null county
   *
   * @throws URISyntaxException exception where URI syntax is incorrect.
   * @throws IOException exception where it failed to read/open information.
   * @throws InterruptedException exception where connection to API is interrupted.
   */
  @Test
  public void testGetBroadbandInfoNullCounty() throws URISyntaxException, IOException,
      InterruptedException {
    CachedData cache = new CachedData(20, 2);

    // checking to see that it throws errors
    try{
      cache.getBroadBandInfo("Texas",null);

    } catch (IllegalArgumentException e){
      // should print Caught
      System.out.println("Caught");

    } catch (ExecutionException e) {

    }
  }

}
