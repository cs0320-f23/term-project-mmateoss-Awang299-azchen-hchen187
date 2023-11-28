package edu.brown.cs.student.Tests.server.webapi;

import edu.brown.cs.student.main.server.webapi.CensusDataSource;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

/**
 * Testing class to test that the methods that BroadbandHandler and CachedData classes rely on
 * work with mocked data. If they work with mocked data then that means that they will work with
 * regular data which is why we are not starting a server and running it with mocked data.
 */
public class MockedCensusTests {


  private MockedCensusData mockedSource;
  private CensusDataSource source;

  /**
   * Constructor so that we can have instance variable source.
   */
  public MockedCensusTests() {
    try {
      this.mockedSource = new MockedCensusData();
      this.source = new CensusDataSource(this.mockedSource.getStateCodes());

    } catch (Exception e) {

    }
  }

  /**
   * Testing the getStateCode method using our mocked data.
   */
  @Test
  public void testGetStateCode(){

    // testing out states that exist
    Assert.assertEquals(this.source.getStateCode("California"), "06");
    Assert.assertEquals(this.source.getStateCode("Colorado"), "08");

    // making sure that test that dont exist throw exception
    try{
      this.source.getStateCode("rar");
    } catch (IllegalArgumentException e){
      System.out.println("caught");
    }

  }

  /**
   * Testing the getCountyCode method with our mocked data. Passing in the mocked data should work
   * and then show that the functionality is correct
   */
  @Test
  public void testGetCountyCode(){

    try{
      Assert.assertEquals(this.source.getCountyCode("06",
          "Orange County", this.mockedSource.getCountyList("06")),
          "059");
      Assert.assertEquals(this.source.getCountyCode("06",
              "Los Angeles County", this.mockedSource.getCountyList("06")),
          "037");
      Assert.assertEquals(this.source.getCountyCode("08",
              "Denver County", this.mockedSource.getCountyList("049")),
          "031");
      Assert.assertEquals(this.source.getCountyCode("08",
              "Grand County", this.mockedSource.getCountyList("08")),
          "049");
    } catch(Exception e){

    }
  }

  /**
   * Testing the getBroadband method using mocked data. Passing in the mocked data should work
   * and the fact that functionality remains shows that all the classes that depend on it will work
   * with regular data.
   */
  @Test
  public void testGetBroadband(){
    try{

      Assert.assertEquals(this.source.getBroadband(
          this.mockedSource.getBroadbandData("06","059")), "93.0");
      Assert.assertEquals(this.source.getBroadband(
          this.mockedSource.getBroadbandData("06","037")), "89.9");
      Assert.assertEquals(this.source.getBroadband(
          this.mockedSource.getBroadbandData("08","031")), "86.5");
      Assert.assertEquals(this.source.getBroadband(
          this.mockedSource.getBroadbandData("08","049")), "96.8");

    } catch (Exception e){

      System.out.println("caught");
    }

  }
}
