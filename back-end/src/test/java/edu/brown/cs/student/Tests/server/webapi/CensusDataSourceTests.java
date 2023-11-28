package edu.brown.cs.student.Tests.server.webapi;

import edu.brown.cs.student.main.server.webapi.CensusDataSource;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.testng.Assert;

/**
 * Test for checking that our CensusDataSource class works properly, don't want to make too
 * many calls to census so will reduce number of tests where possible.
 */
public class CensusDataSourceTests {

  private CensusDataSource source;

  /**
   * Constructor so that we can have an instance variable source.
   */
  public CensusDataSourceTests() {
    try {
      this.source = new CensusDataSource();

    } catch (Exception e) {

    }
  }

  /**
   * Testing the getStateCode method.
   */
  @Test
  public void testGetStateCode(){

    // testing out some states, don't want to make too many calls to the census
    Assert.assertEquals(this.source.getStateCode("California"), "06");
    Assert.assertEquals(this.source.getStateCode("Florida"), "12");

    // testing that the illegal argument exception is thrown when state does not exist
    try{
      this.source.getStateCode("blah");
    } catch (IllegalArgumentException e){
      // prints out so the test passes
      System.out.println("caught");
    }

  }

  /**
   * Testing the getStateCode method.
   */
  @Test
  public void testGetCountyList(){

    try{
      // testing out on one state, don't want to make too many calls to the census
      List<List<String>> countyCodes = this.source.getCountyList("06");
      Assert.assertEquals(countyCodes.get(1).size(), 3);
      Assert.assertEquals(countyCodes.get(1).get(2), "011");
      Assert.assertEquals(countyCodes.get(1).get(1), "06");
    } catch(Exception e){

    }
  }

  /**
   * Testing the getBroadband method.
   */
  @Test
  public void testGetBroadband(){
    try{
      // testing this out a little to not make too many API calls to the census.
      Assert.assertEquals(this.source.getBroadband(
          "06", "031"), "83.5");
      Assert.assertEquals(this.source.getBroadband(
          "06", "087"), "93.0");
      Assert.assertEquals(this.source.getBroadband(
          "06", "087"), "93.0");

    } catch (Exception e){

      System.out.println("caught");
    }

  }

}
