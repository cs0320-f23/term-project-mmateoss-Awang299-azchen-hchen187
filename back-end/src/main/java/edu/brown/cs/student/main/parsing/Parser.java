package edu.brown.cs.student.main.parsing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Class where the parsing of whatever is inputted occurs. Can accept any reader and convert the
 * rows of it into type T which is specified by the user.
 *
 * @param <T> - what the rows of the Csv are converted into.
 */
public class Parser<T> {

  /** Instance variables that are used throughout the class. */
  private BufferedReader reader;

  private static Pattern regex;
  private String header;
  private CreatorFromRow<T> creator;
  private List<String> headerList;
  private String line;
  private ArrayList<T> csvData;

  /**
   * Constructor for the Parser class.
   *
   * @param inputReader - the reader that is being used to read whatever is passed in.
   * @param header - String that is either "true", or "false", telling the program if there is a
   *     header present.
   * @param creator - creator that will dictate what each row is converted into.
   */
  public Parser(Reader inputReader, String header, CreatorFromRow<T> creator) {
    this.reader = new BufferedReader(inputReader);
    // this regex was provided to me in the handout by the CS0320 team.
    this.regex = Pattern.compile(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*(?![^\\\"]*\\\"))");
    this.header = header;
    this.creator = creator;
    this.line = "";
    this.headerList = new ArrayList<>();
    this.csvData = null;
  }

  /**
   * Method that parses the input into an ArrayList of type T, which can be searched.
   *
   * @return - an ArrayList of type T where T depends on CreatorFromRow that is passed in.
   * @throws FactoryFailureException if the creator fails to create something from the row.
   */
  public ArrayList<T> parse() throws FactoryFailureException, IOException {

    // create a main List which will be the overarching list that contains other lists or objects
    ArrayList<T> mainList = new ArrayList<>();

    if (this.csvData != null) {
      return this.csvData;
    } else {
      this.getHeader();
      this.line = this.reader.readLine();
      // loop through until there are no more lines in the file
      while (this.line != null) {
        // create inner list
        List<String> innerList = new ArrayList<>();
        // split line using the regex
        innerList = Arrays.asList(this.regex.split(this.line));

        for (int i = 0; i < innerList.size(); i++) {
          innerList.set(i, postprocess(innerList.get(i)));
        }

        // creator creates the item that will go in the Arraylist
        T innerItem = this.creator.create(innerList);
        mainList.add(innerItem);

        // add the new item and move on to the next line
        this.line = this.reader.readLine();
      }
    }
    this.csvData = mainList;
    return mainList;
  }

  /**
   * Helper method that executes if there is a header. Removing the header so that parse does not
   * have the creator try to parse something from it and reach an error.
   *
   * @return - A list of the header so that it can be used for searching the header in search.
   */
  public List<String> getHeader() throws IOException {
    // happens if there is a header
    // case where already looped
    if(this.csvData != null){
      return this.headerList;
    }
    if (this.header.equals("true")) {

      // reads the first line of the file
      this.line = this.reader.readLine();
      this.headerList = List.of(this.regex.split(this.line));

    }
    // returns the headerList to aid in testing
    return this.headerList;
  }

  /**
   * Elimiate a single instance of leading or trailing double-quote, and replace pairs of double quotes with singles.
   *
   * @param arg the string to process
   * @return the postprocessed string
   */
  public static String postprocess(String arg) {
    return arg
        // Remove extra spaces at beginning and end of the line
        .trim()
        // Remove a beginning quote, if present
        .replaceAll("^\"", "")
        // Remove an ending quote, if present
        .replaceAll("\"$", "")
        // Replace double-double-quotes with double-quotes
        .replaceAll("\"\"", "\"");
  }
}
