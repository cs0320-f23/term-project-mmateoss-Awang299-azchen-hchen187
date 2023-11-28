package edu.brown.cs.student.Tests.testCreatorsFromRow;

import edu.brown.cs.student.main.parsing.CreatorFromRow;
import edu.brown.cs.student.main.parsing.FactoryFailureException;
import java.util.List;

/** Test class to check that parser throws error correctly. */
public class ErrorThrowerCreator implements CreatorFromRow<String> {

  /**
   * Create method that will be used to ensure that an error is thrown by the program.
   *
   * @param row - the row that will be converted.
   * @return - should be nothing because an error will be thrown
   * @throws FactoryFailureException - tells that something failed to be created
   */
  @Override
  public String create(List<String> row) throws FactoryFailureException {
    throw new FactoryFailureException("Failed", row);
  }
}
