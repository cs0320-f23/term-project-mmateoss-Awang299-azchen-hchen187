package edu.brown.cs.student.main.searching;

import edu.brown.cs.student.main.parsing.CreatorFromRow;
import edu.brown.cs.student.main.parsing.FactoryFailureException;
import java.util.List;

/**
 * Class that implements CreatorFromRow. Used by search class to tell its parser to return a list of
 * Strings from the inputted row.
 */
public class StringListCreator implements CreatorFromRow<List<String>> {

  /** Constructor for the StringListCreator. */
  public StringListCreator() {}

  /**
   * Method that creates what the StringListCreator makes, in this case is a List of Strings from a
   * List of Strings. Returning what is inputted exactly for the functionality needed in searching.
   *
   * @param row - A parsed row from whatever file is being read.
   * @return - returns the same row that was inputted, in order to work with searching.
   * @throws FactoryFailureException - error thrown when the return of the row fails.
   */
  @Override
  public List<String> create(List<String> row) throws FactoryFailureException {
    return row;
  }
}
