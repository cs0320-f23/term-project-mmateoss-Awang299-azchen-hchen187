package edu.brown.cs.student.Tests.server.webapi;

import com.beust.ah.A;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that mocks the census data to ensure that it works
 */
public class MockedCensusData {

private List<List<String>> states;

  /**
   * Constructor for the mocked class.
   */
  public MockedCensusData(){
    this.states = new ArrayList<>();
    List<String> list0 = List.of("state", "code");
    List<String> list1 = List.of("California","06");
    List<String> list2 = List.of("Colorado", "08");
    this.states.add(list0);
    this.states.add(list1);
    this.states.add(list2);

  }

  /**
   * Mocked method that returns our mocked states list.
   *
   * @return returns mocked list of states
   */
  public List<List<String>> getStateCodes(){
    return this.states;
  }

  /**
   * Method that mocks the CountyList
   * @param stateCode the census code for state.
   * @return the list of counties in that state.
   */
  public List<List<String>> getCountyList(String stateCode){

    List<List<String>> countyList = new ArrayList<>();
    if (stateCode.equals("06")){

      List<String> list1 = List.of("Los Angeles County, California","06","037");
      List<String> list2 = List.of("Orange County, California","06","059");
      countyList.add(list1);
      countyList.add(list2);

      return countyList;

    } else {

      List<String> list1 = List.of("Denver County, Colorado","08","031");
      List<String> list2 = List.of("Grand County, Colorado","08","049");
      countyList.add(list1);
      countyList.add(list2);

      return countyList;
    }


  }

  /**
   * Method that mocks the BraodbandData info.
   *
   * @param stateCode the census code for state.
   * @param countyCode the census code for county.
   * @return broadband data
   */

  public List<List<String>> getBroadbandData(String stateCode, String countyCode){

    List<List<String>> info = new ArrayList<>();
    info.add(List.of("Name","percent","stateCode","countyCode"));
    if(stateCode.equals("06")){
      if(countyCode.equals("037")){
        info.add(List.of("Los Angeles County, California","89.9","06","037"));
        return info;
      }
      if(countyCode.equals("059")){
        info.add(List.of("Orange County, California","93.0","06","059"));
        return info;
      }
    } else {
      if(countyCode.equals("031")){
        info.add(List.of("Denver County, Colorado","86.5","08","031"));
        return info;

      } if (countyCode.equals("049")){
        info.add(List.of("Grand County, Colorado","96.8","08","049"));
        return info;
      }

    }

    return info;
  }







}
