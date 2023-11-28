package edu.brown.cs.student.main.server.csv;

import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Class that handles giving the User the Csv file to see.
 */
public class ViewCsvHandler implements Route {

  private CsvLoader csvLoader;

  /**
   * Constructor for ViewCsvHandler.
   *
   * @param csvLoader a CsvLoader that is used to convey data from the LoadCsvHandler
   *                 to this handler.
   */
  public ViewCsvHandler(CsvLoader csvLoader) {
    this.csvLoader = csvLoader;
  }

  /**
   * Handle method that returns the csv for the user to see.
   *
   * @param req request object used to get the query parameters.
   * @param res esponse object used to create a response.
   * @return returns what is mapped to the server. Usually the Csv but may be something else if
   *        error is caught.
   * @throws Exception vague exception, which is caught in order to not crash our server.
   */
  @Override
  public Object handle(Request req, Response res) throws Exception {

    // creating variables that we need
    Map<String, Object> responseMap = new HashMap<>();
    Moshi moshi = new Moshi.Builder().build();

    // getting all the query parameters that were passed in
    Set<String> paramsSet = req.queryParams();

    // making sure no query parameters were passed in
    if (paramsSet.size() != 0) {
      responseMap.put("result", "error_bad_request");
      responseMap.put("message", "No query parameters allowed");
      return moshi.adapter(Map.class).toJson(responseMap);
    }

    // getting the csv data from csvLoader
    try {
      List<List<String>> csvData = this.csvLoader.getCsvData();

      if (csvData == null) {
        responseMap.put("result", "error_data_source");
      } else {
        responseMap.put("result", "success");
        responseMap.put("data", csvData);
      }

      Type targetType = Types.newParameterizedType(Map.class, String.class, Object.class);
      // returning it to server
      return moshi.adapter(targetType).toJson(responseMap);

      // taking care of exceptions so as to not crash the server
    } catch (Exception e) {
      responseMap.put("result", "error_bad_request");
      return moshi.adapter(Map.class).toJson(responseMap);
    }
  }
}
