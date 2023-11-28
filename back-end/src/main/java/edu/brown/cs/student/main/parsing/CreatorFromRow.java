package edu.brown.cs.student.main.parsing;

import java.util.List;

/**
 * This interface defines a method that allows your Csv parser to convert each row into an object of
 * some arbitrary passed type.
 *
 * <p>Your parser class constructor should take a second parameter of this generic interface type.
 */
public interface CreatorFromRow<T> {

  /**
   * Method that will create something from an inputted List of Strings.
   *
   * @param row - the row that will be converted.
   * @return - object of type T that is specified by classes that implement.
   * @throws FactoryFailureException - exception that can be thrown if creation fails.
   */
  T create(List<String> row) throws FactoryFailureException;
}
