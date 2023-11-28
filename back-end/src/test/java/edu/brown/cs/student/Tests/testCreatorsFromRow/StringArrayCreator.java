package edu.brown.cs.student.Tests.testCreatorsFromRow;

import edu.brown.cs.student.main.parsing.CreatorFromRow;
import edu.brown.cs.student.main.parsing.FactoryFailureException;
import java.util.List;

/** Test class used to create an Array of Strings from a List of Strings */
public class StringArrayCreator implements CreatorFromRow<String[]> {

  /**
   * Method that creates what the StringCreator makes, in this case is a String[] from a
   * List<String>. Returning a row in array form.
   *
   * @param row - A parsed row from whatever file is being read
   * @return - a row in array form
   * @throws FactoryFailureException - error thrown when conversion fails
   */
  @Override
  public String[] create(List<String> row) throws FactoryFailureException {
    String[] stringArray = new String[row.size()];
    for (int i = 0; i < row.size(); i++) {
      stringArray[i] = row.get(i);
    }
    return stringArray;
  }
}
