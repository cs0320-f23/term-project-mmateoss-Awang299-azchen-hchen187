package edu.brown.cs.student.main.server.csv;

import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.parsing.FactoryFailureException;
import edu.brown.cs.student.main.searching.Search;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Class that takes care of all the searching functionality of the Csv part of the API.
 */
public class SearchCsvHandler implements Route {

  private CsvLoader csvLoader;

  /**
   * Constructor for the SearchCsvHandler class.
   *
   * @param csvLoader a CsvLoader that is used to convey data from the
   *                 LoadCsvHandler to this handler.
   */
  public SearchCsvHandler(CsvLoader csvLoader) {
    this.csvLoader = csvLoader;
  }


  /**
   * Handle method that takes care of the functionality for this handler.
   * Here it does all the logic for the searching mechanism where it does different
   * searches based on the query parameters passed
   * in by the user.
   *
   * @param req request object used to get the query parameters.
   * @param res response object used to create a response.
   * @return returns what is mapped to the server. Usually result of the search but may be something
   *        else if error is caught.
   * @throws Exception vague exception, which is caught in order to not crash our server.
   */
  @Override
  public Object handle(Request req, Response res) throws Exception {

    // initializing needed variables
    Map<String, Object> responseMap = new HashMap<>();
    Moshi moshi = new Moshi.Builder().build();

    // getting all the query parameters
    Set<String> params = req.queryParams();
    String target = req.queryParams("target");
    String colName = req.queryParams("colName");
    String colIdx = req.queryParams("colIdx");

    // all possible query parameters size
    Set<String> possibleParams = Set.of("target", "colName", "colIdx");

    for (String param : params) {
      if (!possibleParams.contains(param)) {
        responseMap.put("result", "error_bad_request");
        responseMap.put("parameters", params);
        responseMap.put("message", "invalid parameters");
        return moshi.adapter(Map.class).toJson(responseMap);
      }
    }

    try {
      if (!this.csvLoader.getIsLoaded()) {
        responseMap.put("result", "error_data_source");
        responseMap.put("parameters", params);
        responseMap.put("message", "load in csv file before searching");
        return moshi.adapter(Map.class).toJson(responseMap);
      }

      // creating a Search object to be able to search the Csv file that was loaded
      Search searcher = new Search(this.csvLoader.getCsvParser(), target);
      List<List<String>> searchResult = new ArrayList<>();

      // checking different conditions based on the query parameters passed in, tell us which
      // search to do.

      if (colName == null && colIdx == null && target != null) {
        searchResult = searcher.basicSearch();
      } else if (colName == null && colIdx != null) {
        try {
          searchResult = searcher.colSearch(colIdx);
        } catch (IndexOutOfBoundsException e) {
          responseMap.put("result", "error_bad_request");
          responseMap.put("message", "Make sure you are searching for a column index that exists");
          return moshi.adapter(Map.class).toJson(responseMap);
        }

      } else if (colName != null && colIdx == null) {
        try {
          searchResult = searcher.headerSearch(colName);
        } catch (NoSuchElementException e) {
          responseMap.put("result", "error_bad_request");
          responseMap.put("message", "Acceptable headers: "
              + this.csvLoader.getCsvParser().getHeader());
          return moshi.adapter(Map.class).toJson(responseMap);
        }
      } else {
        responseMap.put("result", "error_bad_request");
        responseMap.put("parameters", params);
        return moshi.adapter(Map.class).toJson(responseMap);
      }

      // adding the results of the searches to the response map and serializing it to put on server.
      responseMap.put("result", "success");
      responseMap.put("data", searchResult);

      for (String param : params) {
        responseMap.put(param, req.queryParams(param));
      }

      return moshi.adapter(Map.class).toJson(responseMap);

      // catching possible exceptions and returning map describing errors to the server so as to
      // not crash the server
    } catch (FactoryFailureException e) {
      responseMap.put("result", "error_creating_row_object");
      responseMap.put("parameters", params);
      return moshi.adapter(Map.class).toJson(responseMap);
    } catch (Exception e) {
      responseMap.put("result", e);
      responseMap.put("parameters", params);
      return moshi.adapter(Map.class).toJson(responseMap);
    }
  }
}
