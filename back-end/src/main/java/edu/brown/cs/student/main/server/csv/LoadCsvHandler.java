package edu.brown.cs.student.main.server.csv;

import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.parsing.CreatorFromRow;
import edu.brown.cs.student.main.parsing.Parser;
import edu.brown.cs.student.main.searching.StringListCreator;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Class that handles loading the Csv. Allows a user to load a Csv file for other classes to use.
 */
public class LoadCsvHandler implements Route {
  private CsvLoader csvLoader;

  /**
   * Constructor for LoadCsvHandler class.
   *
   * @param csvLoader a CsvLoader that is used to convey data from
   *                  the LoadCsvHandler to this handler.
   */
  public LoadCsvHandler(CsvLoader csvLoader) {
    this.csvLoader = csvLoader;
  }

  /**
   * Handle method that takes care of the functionality for this handler. Allows for a user to
   * load a Csv file through a request.
   *
   * @param req request object used to get the query parameters.
   * @param res response object used to create a response.
   * @return returns what is mapped to the server.
   * @throws Exception vague exception, which is caught in order to not crash our server.
   */
  @Override
  public Object handle(Request req, Response res) throws Exception {

    // creating the variables that we need
    Map<String, Object> responseMap = new HashMap<>();
    Moshi moshi = new Moshi.Builder().build();

    // getting the query parameters
    Set<String> params = req.queryParams();
    String filePath = req.queryParams("filePath");
    String hasHeader = req.queryParams("hasHeader");

    // checking conditions that would make this a bad_request
    if (filePath == null || hasHeader == null || params.size() != 2) {
      responseMap.put("result", "error_bad_request");
      responseMap.put("filepath", filePath);
      return moshi.adapter(Map.class).toJson(responseMap);
    }
    // reading in the file and parsing it
    try {
      FileReader fr = new FileReader(filePath);
      CreatorFromRow<List<String>> creator = new StringListCreator();
      Parser<List<String>> csvParser = new Parser<>(fr, hasHeader, creator);

      // use CsvLoader object to be able to get this information to other handlers
      this.csvLoader.setCsvData(csvParser.parse());
      this.csvLoader.setCsvParser(csvParser);

      responseMap.put("result", "success");
      responseMap.put("filePath", filePath);
      return moshi.adapter(Map.class).toJson(responseMap);

      // returning an error to the server if the file is not found
    } catch (FileNotFoundException e) {
      responseMap.put("result", "error_datasource");
      responseMap.put("filepath", filePath);
      return moshi.adapter(Map.class).toJson(responseMap);
    }
  }
}
