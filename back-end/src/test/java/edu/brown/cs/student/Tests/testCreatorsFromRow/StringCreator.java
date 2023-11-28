package edu.brown.cs.student.Tests.testCreatorsFromRow;

import edu.brown.cs.student.main.parsing.CreatorFromRow;
import edu.brown.cs.student.main.parsing.FactoryFailureException;
import java.util.List;

/**
 * Class used for testing that my parser uses create method correctly for any implementation of
 * CreatorFromRow. Creates a string from the inputted row
 */
public class StringCreator implements CreatorFromRow<String> {

  /**
   * Method that creates what the StringCreator makes, in this case is a String from a List<String>.
   * Returning what is located at first index of inputted list. Used for testing.
   *
   * @param row - A parsed row from whatever file is being read
   * @return - returns what is located at first index of inputted list
   * @throws FactoryFailureException - error thrown when the return of the row fails.
   */
  @Override
  public String create(List<String> row) throws FactoryFailureException {
    return row.get(0);
  }
}
