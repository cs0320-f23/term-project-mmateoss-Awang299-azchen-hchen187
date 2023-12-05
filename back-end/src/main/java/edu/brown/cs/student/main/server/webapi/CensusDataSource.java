package edu.brown.cs.student.main.server.webapi;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the CensusDataSource class. It contains all the logic for getting information from the
 * Census. Can be used independently, if someone using this code wants to avoid using a cache.
 */
public class CensusDataSource {
  private Map<String, String> stateCodesMap;

  /**
   * Constructor for the class. Does initial call to census API to get a list of all
   * the state codes.
   *
   * @throws URISyntaxException exception thrown when URI is invalid.
   * @throws IOException exception when it failed to read/open.
   * @throws InterruptedException exception when connection to API failed.
   */
  public CensusDataSource() throws URISyntaxException, IOException, InterruptedException {

    // fetching state codes from the census
    String uriString = "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*";
    List<List<String>> stateCodes = this.fetchApiData(uriString);

    // creating a hashmap from state to its code
    this.stateCodesMap = new HashMap<>();
    for (int i = 1; i < stateCodes.size(); i++) {
      this.stateCodesMap.put(stateCodes.get(i).get(0), stateCodes.get(i).get(1));
    }
  }

  /**
   * Constructor for the class. Takes in stateCodes to circumvent need for api call
   *
   * @throws URISyntaxException exception thrown when URI is invalid.
   * @throws IOException exception when it failed to read/open.
   * @throws InterruptedException exception when connection to API failed.
   */
  public CensusDataSource(List<List<String>> stateCodes) throws URISyntaxException,
      IOException, InterruptedException {

    // creating a hashmap from state to its code
    this.stateCodesMap = new HashMap<>();
    for (int i = 1; i < stateCodes.size(); i++) {
      this.stateCodesMap.put(stateCodes.get(i).get(0), stateCodes.get(i).get(1));
    }
  }

  /**
   * Returning the specific census state code for the state inputted.
   *
   * @param state the state whose state code you want.
   * @return the census state code.
   */
  public String getStateCode(String state) {
    if (!this.stateCodesMap.keySet().contains(state)) {
      // throw exception if the state inputted does not exist or is in the census
      throw new IllegalArgumentException();
    }
    return this.stateCodesMap.get(state);
  }

  /**
   * Method that returns the county code for a specific county within a state. Not used by the
   * cache class but left here in case a developer wants to go about doing something without a
   * cache.
   *
   * @param stateCode census code for the state.
   * @param countyName name of the county whose code you want.
   * @return the census code for the county
   * @throws URISyntaxException exception thrown when URI is invalid.
   * @throws IOException exception when it failed to read/open.
   * @throws InterruptedException exception when connection to API failed.
   */
  public String getCountyCode(String stateCode, String countyName)
      throws URISyntaxException, IOException, InterruptedException {

    // getting countyCodes list
    List<List<String>> countyCodes = this.getCountyList(stateCode);

    // finding the specific code for the wanted county
    String countyCode = null;
    for (int i = 1; i < countyCodes.size(); i++) {
      if (countyCodes.get(i).get(0).split(",")[0].equals(countyName)) {
        countyCode = countyCodes.get(i).get(2);
      }
    }

    return countyCode;
  }

  /**
   * Method that returns the county code for a specific county within a state. Passes in
   * countyCodes to circumvent api call. Not used by the cache class but left here in case
   * a developer wants to go about doing something without a cache.
   *
   * @param stateCode census code for the state.
   * @param countyName name of the county whose code you want.
   * @return the census code for the county
   * @throws URISyntaxException exception thrown when URI is invalid.
   * @throws IOException exception when it failed to read/open.
   * @throws InterruptedException exception when connection to API failed.
   */
  public String getCountyCode(String stateCode, String countyName, List<List<String>> countyCodes)
      throws URISyntaxException, IOException, InterruptedException {

    // finding the specific code for the wanted county
    String countyCode = null;
    for (int i = 1; i < countyCodes.size(); i++) {
      if (countyCodes.get(i).get(0).split(",")[0].equals(countyName)) {
        countyCode = countyCodes.get(i).get(2);
      }
    }
    if (countyCode == null) {
      // if the County inputted does not exist throw an error
      throw new IllegalArgumentException();
    }

    return countyCode;
  }

  /**
   * Method that gets the list of all Counties for a state.
   *
   * @param stateCode census code for the state whose counties you want.
   * @return the list of all the counties in the state.
   * @throws URISyntaxException exception thrown when URI is invalid.
   * @throws IOException exception when it failed to read/open.
   * @throws InterruptedException exception when connection to API failed.
   */
  public List<List<String>> getCountyList(String stateCode)
      throws URISyntaxException, IOException, InterruptedException {

    // returning result of the API call
    String uriString =
        "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=county:*&in=state:" + stateCode;
    return this.fetchApiData(uriString);
  }

  /**
   * Method that gets the broadband percentage for a specific state and county.
   *
   * @param stateCode the census code for the state where the county resides
   * @param countyCode the census code for the county
   * @return the broadband percentage at the specific state and county
   * @throws URISyntaxException exception thrown when URI is invalid.
   * @throws IOException exception when it failed to read/open.
   * @throws InterruptedException exception when connection to API failed.
   */
  public String getBroadband(String stateCode, String countyCode)
      throws URISyntaxException, IOException, InterruptedException {

    // getting broadband percentage from the census API
    String uriString =
        "https://api.census.gov/data/2021/acs/acs1/subject/variables?get=NAME,S2802_C03_022E&"
            + "for=county:" + countyCode
            + "&in=state:" + stateCode;

    return this.fetchApiData(uriString).get(1).get(1);
  }


  /**
   * Method that gets the broadband percentage for a specific state and county. Passes in
   * fetchedData to circumvent API calls.
   *
   * @return the broadband percentage at the specific state and county
   * @throws URISyntaxException exception thrown when URI is invalid.
   * @throws IOException exception when it failed to read/open.
   * @throws InterruptedException exception when connection to API failed.
   */
  public String getBroadband(List<List<String>> fetchedData)
      throws URISyntaxException, IOException, InterruptedException {

    return fetchedData.get(1).get(1);
  }

  /**
   * Helper method that makes a request from an API to get needed information for return of handle.
   *
   * @param uriString String that is the URI where we get the information from.
   * @return List of Lists of Strings which are the contents of the Json returned from the API.
   * @throws URISyntaxException exception where URI syntax is incorrect.
   * @throws IOException exception where it failed to read/open information.
   * @throws InterruptedException exception where connection to API is interrupted.
   */
  private List<List<String>> fetchApiData(String uriString)
      throws URISyntaxException, IOException, InterruptedException {
    //building a new HttpRequest
    HttpRequest buildRequest = HttpRequest.newBuilder()
        .uri(new URI(uriString))
        .GET()
        .build();
    // building a response with the HttpRequest
    HttpResponse<String> response = HttpClient.newBuilder()
        .build()
        .send(buildRequest, HttpResponse.BodyHandlers.ofString());

    // making target type and then using Moshi to turn it into a List of lists of strings
    Type targetClassType = Types.newParameterizedType(List.class,
        Types.newParameterizedType(List.class, String.class));
    Moshi moshi = new Moshi.Builder().build();
    JsonAdapter<List<List<String>>> dataAdapter = moshi.adapter(targetClassType);

    return dataAdapter.fromJson(response.body());
  }
}
