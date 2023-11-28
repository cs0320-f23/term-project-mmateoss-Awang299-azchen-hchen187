package edu.brown.cs.student.main.server.csv;

import edu.brown.cs.student.main.parsing.Parser;
import java.util.ArrayList;
import java.util.List;

/**
 * Class used for the functionality of conveying data from one Handler to the other.
 */
public class CsvLoader {
  private boolean isLoaded;
  private List<List<String>> csvData;
  private Parser<List<String>> csvParser;

  /**
   * Constructor for the CsvLoader class.
   */
  public CsvLoader() {
    this.isLoaded = false;
    this.csvData = new ArrayList<List<String>>();
  }

  /**
   * Method that sets the instance variable csvData to the parameter that is passed in, letting
   * this class know that theCsvData has been loaded and allowing for it to return it to others
   * through a getter method.
   *
   * @param csvData the parsed Csv file (parsed in the LoadCsvHandler).
   * @return a boolean saying if the CsvData has been set.
   */
  public boolean setCsvData(List<List<String>> csvData) {
    if (csvData == null || csvData.isEmpty()) {
      this.isLoaded = false;
      return false;
    } else {
      this.isLoaded = true;
      this.csvData = csvData;
      return true;
    }
  }

  /**
   * Method that sets the instance variable csvParser to the Parser that
   * is passed in as a parameter.
   * This allows for it to then be sent to other classes through a getter method (needed by
   * SearchCsv handler in order to be able to create a Search instance).
   *
   * @param csvParser the Parser that was created by LoadCsvHandler.
   * @return a boolean telling whether the csvParser was set.
   */
  public boolean setCsvParser(Parser csvParser) {
    if (csvParser != null) {
      this.csvParser = csvParser;
      return true;
    } else {
      return false;
    }
  }

  /**
   * Getter method that returns the csvData, allowing for it to be used (either searched or
   * just returned to the server by the different handlers).
   *
   * @return the csvData (parsed csv file).
   */
  public List<List<String>> getCsvData() {
    if (this.isLoaded) {
      return this.csvData;
    } else {
      return null;
    }
  }

  /**
   * Getter method that returns the CsvParser.
   *
   * @return CsvParser object.
   */
  public Parser getCsvParser() {
    if (this.csvParser != null) {
      return this.csvParser;
    } else {
      return null;
    }
  }

  /**
   * Getter method that returns the CsvParser.
   *
   * @return CsvParser object.
   */
  public boolean getIsLoaded() {
    return this.isLoaded;
  }
}
